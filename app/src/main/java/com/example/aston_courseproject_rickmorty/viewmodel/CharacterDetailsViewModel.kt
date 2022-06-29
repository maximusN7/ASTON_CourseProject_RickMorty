package com.example.aston_courseproject_rickmorty.viewmodel

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import com.example.aston_courseproject_rickmorty.MainViewModel
import com.example.aston_courseproject_rickmorty.fragments.EpisodeDetailsFragment
import com.example.aston_courseproject_rickmorty.fragments.LocationDetailsFragment
import com.example.aston_courseproject_rickmorty.model.*
import com.example.aston_courseproject_rickmorty.model.database.ItemsDatabase
import com.example.aston_courseproject_rickmorty.repository.CharacterDetailsRepository
import com.example.aston_courseproject_rickmorty.repository.EpisodeDetailsRepository
import com.example.aston_courseproject_rickmorty.retrofit.ApiState
import com.example.aston_courseproject_rickmorty.retrofit.Common
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices
import com.example.aston_courseproject_rickmorty.retrofit.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class CharacterDetailsViewModel(
    characterID: Int, val mainViewModel: MainViewModel,
    val database: ItemsDatabase
) : ViewModel() {
    val characterModel = CharacterDetailsModel(characterID)
    val currentCharacter = MutableLiveData<Character>()
    val currentOrigin = MutableLiveData<Location>()
    val currentLocation = MutableLiveData<Location>()
    val episodeList = MutableLiveData<MutableList<Episode>>()
    var retrofitServices: RetrofitServices = Common.retrofitService
    private val repository = CharacterDetailsRepository(retrofitServices, database)
    //private val dataSource = repository.getEpisodeFromNetwork()

    val character = MutableStateFlow(ApiState(Status.LOADING, Character(), ""))
    val origin = MutableStateFlow(ApiState(Status.LOADING, LocationForList(), ""))
    val location = MutableStateFlow(ApiState(Status.LOADING, LocationForList(), ""))
    val episodes = MutableStateFlow(ApiState(Status.LOADING, mutableListOf<Episode>(), ""))

    init {
        getCharacer(characterID)
    }

    private fun getCharacer(characterID: Int) {
        character.value = ApiState.loading()
        viewModelScope.launch {
            repository.getCharacter(characterID)
                .catch {
                    character.value = ApiState.error(it.message.toString())
                }
                .collect {
                    character.value = ApiState.success(it.data)
                    val charactersId = separateIdFromUrl(character.value.data?.episode)
                    when {
                        charactersId != "" -> getEpisodes(charactersId)
                        charactersId.contains(",") -> {
                            getEpisodes(charactersId)
                        }
                        else -> episodes.value = ApiState.success(mutableListOf())
                    }
                    if (character.value.data?.location?.name != "unknown") {
                        val locationId = separateIdFromLocationUrl(character.value.data?.location?.url)
                        getLocation(locationId, location)
                    } else {
                        location.value = ApiState.success(LocationForList.convertLocationForList(Location(id = -1, name = "unknown", type = "", dimension = "")))
                    }
                    if (character.value.data?.origin?.name != "unknown") {
                        val originId = separateIdFromLocationUrl(character.value.data?.origin?.url)
                        getLocation(originId, origin)
                    } else {
                        origin.value = ApiState.success(LocationForList.convertLocationForList(Location(id = -1, name = "unknown", type = "", dimension = "")))
                    }
                }
        }
    }

    private suspend fun getEpisodes(needIds: String) {
        var needIdsLocal = needIds
        if (!needIdsLocal.contains(",")) needIdsLocal += ","
        repository.getEpisodeList(needIdsLocal)
            .catch {
                episodes.value = ApiState.error(it.message.toString())
            }
            .collect {
                episodes.value = ApiState.success(it.data)
            }
    }

    private suspend fun getLocation(locationId: Int, loc: MutableStateFlow<ApiState<LocationForList>>) {
        repository.getLocation(locationId)
            .catch {
                loc.value = ApiState.error(it.message.toString())
            }
            .collect {
                loc.value = ApiState.success(LocationForList.convertLocationForList(it.data))
            }
    }

    private fun separateIdFromUrl(urlArray: Array<String>?): String {
        var str = ""
        if (urlArray == null) return ""
        for (url in urlArray) {
            val baseUrl = "https://rickandmortyapi.com/api/episode/"
            str += "${url!!.substring(baseUrl.length)},"
        }

        return str.dropLast(1)
    }

    private fun separateIdFromLocationUrl(urlArray: String?): Int {
        var str = ""
        val baseUrl = "https://rickandmortyapi.com/api/location/"
        str += urlArray!!.substring(baseUrl.length)
        return str.toInt()
    }

    fun openFragment(episode: Episode?) {
        val fragment: Fragment = EpisodeDetailsFragment.newInstance(episode?.id!!)
        mainViewModel.changeCurrentDetailsFragment(fragment)
    }

    fun openFragment(location: LocationForList?) {
        if (location?.name != "unknown") {
            val fragment: Fragment = LocationDetailsFragment.newInstance(location?.id!!)
            mainViewModel.changeCurrentDetailsFragment(fragment)
        }
    }
}