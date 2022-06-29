package com.example.aston_courseproject_rickmorty.viewmodel

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import com.example.aston_courseproject_rickmorty.MainViewModel
import com.example.aston_courseproject_rickmorty.fragments.CharacterDetailsFragment
import com.example.aston_courseproject_rickmorty.model.Character
import com.example.aston_courseproject_rickmorty.model.Location
import com.example.aston_courseproject_rickmorty.model.LocationDetailsModel
import com.example.aston_courseproject_rickmorty.model.database.ItemsDatabase
import com.example.aston_courseproject_rickmorty.repository.LocationDetailsRepository
import com.example.aston_courseproject_rickmorty.retrofit.ApiState
import com.example.aston_courseproject_rickmorty.retrofit.Common
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices
import com.example.aston_courseproject_rickmorty.retrofit.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class LocationDetailsViewModel(
    locationID: Int,
    val mainViewModel: MainViewModel,
    val database: ItemsDatabase
) : ViewModel() {
    val locationModel = LocationDetailsModel(locationID)
    val currentLocation = MutableLiveData<Location>()
    val characterList = MutableLiveData<MutableList<Character>>()
    var retrofitServices: RetrofitServices = Common.retrofitService
    private val repository = LocationDetailsRepository(retrofitServices, database)
    //private val dataSource = repository.getLocationFromNetwork()

    val location = MutableStateFlow(ApiState(Status.LOADING, Location(), ""))
    val characters = MutableStateFlow(ApiState(Status.LOADING, mutableListOf<Character>(), ""))

    init {
        getLocation(locationID)
    }

    private fun getLocation(locationID: Int) {
        location.value = ApiState.loading()
        viewModelScope.launch {
            repository.getLocation(locationID)
                .catch {
                    location.value = ApiState.error(it.message.toString())
                }
                .collect {
                    location.value = ApiState.success(it.data)
                    val charactersId = separateIdFromUrl(location.value.data?.residents)
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