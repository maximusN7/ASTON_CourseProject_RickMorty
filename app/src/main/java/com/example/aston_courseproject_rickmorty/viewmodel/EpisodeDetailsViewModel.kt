package com.example.aston_courseproject_rickmorty.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.aston_courseproject_rickmorty.MainViewModel
import com.example.aston_courseproject_rickmorty.fragments.CharacterDetailsFragment
import com.example.aston_courseproject_rickmorty.fragments.EpisodeDetailsFragment
import com.example.aston_courseproject_rickmorty.model.*
import com.example.aston_courseproject_rickmorty.model.database.ItemsDatabase
import com.example.aston_courseproject_rickmorty.repository.EpisodeDetailsRepository
import com.example.aston_courseproject_rickmorty.repository.EpisodeRepository
import com.example.aston_courseproject_rickmorty.retrofit.ApiState
import com.example.aston_courseproject_rickmorty.retrofit.Common
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices
import com.example.aston_courseproject_rickmorty.retrofit.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class EpisodeDetailsViewModel(
    episodeID: Int,
    val mainViewModel: MainViewModel,
    val database: ItemsDatabase
) : ViewModel() {
    val episodeModel = EpisodeDetailsModel(episodeID)
    val currentEpisode = MutableLiveData<Episode?>()
    val characterList = MutableLiveData<MutableList<Character>>()
    var retrofitServices: RetrofitServices = Common.retrofitService
    private val repository = EpisodeDetailsRepository(retrofitServices, database)
    //private val dataSource = repository.getEpisodeFromNetwork()

    val episode = MutableStateFlow(ApiState(Status.LOADING, Episode(), ""))
    val characters = MutableStateFlow(ApiState(Status.LOADING, mutableListOf<Character>(), ""))

    init {
        getEpisode(episodeID)
    }

    private fun getEpisode(episodeID: Int) {
        episode.value = ApiState.loading()
        viewModelScope.launch {
            repository.getEpisode(episodeID)
                .catch {
                    episode.value = ApiState.error(it.message.toString())
                }
                .collect {
                    episode.value = ApiState.success(it.data)
                    val charactersId = separateIdFromUrl(episode.value.data?.characters)
                    when {
                        charactersId != "" -> getCharacters(charactersId)
                        charactersId.contains(",") -> {
                            getCharacters(charactersId)
                        }
                        else -> characters.value = ApiState.success(mutableListOf())
                    }
                }
        }
    }

    private suspend fun getCharacters(needIds: String) {
        var needIdsLocal = needIds
        if (!needIdsLocal.contains(",")) needIdsLocal += ","
        repository.getCharacterList(needIdsLocal)
            .catch {
                characters.value = ApiState.error(it.message.toString())
            }
            .collect {
                characters.value = ApiState.success(it.data)
            }
    }

    private fun separateIdFromUrl(urlArray: Array<String>?): String {
        var str = ""
        if (urlArray == null) return ""
        for (url in urlArray) {
            val baseUrl = "https://rickandmortyapi.com/api/character/"
            str += "${url.substring(baseUrl.length)},"
        }

        return str.dropLast(1)
    }

    fun openFragment(character: Character?) {
        val fragment: Fragment = CharacterDetailsFragment.newInstance(character?.id!!)
        mainViewModel.changeCurrentDetailsFragment(fragment)
    }
}