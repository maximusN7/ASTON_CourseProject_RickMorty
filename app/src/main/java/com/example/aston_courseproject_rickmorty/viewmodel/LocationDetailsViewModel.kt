package com.example.aston_courseproject_rickmorty.viewmodel

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import com.example.aston_courseproject_rickmorty.MainViewModel
import com.example.aston_courseproject_rickmorty.fragments.CharacterDetailsFragment
import com.example.aston_courseproject_rickmorty.model.Character
import com.example.aston_courseproject_rickmorty.model.database.CharacterDb
import com.example.aston_courseproject_rickmorty.model.database.ItemsDatabase
import com.example.aston_courseproject_rickmorty.model.dto.CharacterForListDto
import com.example.aston_courseproject_rickmorty.model.dto.LocationDto
import com.example.aston_courseproject_rickmorty.repository.LocationDetailsRepository
import com.example.aston_courseproject_rickmorty.retrofit.ApiState
import com.example.aston_courseproject_rickmorty.retrofit.Common
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices
import com.example.aston_courseproject_rickmorty.retrofit.Status
import com.example.aston_courseproject_rickmorty.utils.Converters
import com.example.aston_courseproject_rickmorty.utils.InternetConnectionChecker
import com.example.aston_courseproject_rickmorty.utils.Separators
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class LocationDetailsViewModel(
    locationID: Int,
    val mainViewModel: MainViewModel,
    val database: ItemsDatabase,
    internetChecker: InternetConnectionChecker
) : ViewModel() {
    var retrofitServices: RetrofitServices = Common.retrofitService
    private val repository = LocationDetailsRepository(retrofitServices, database)

    val location = MutableStateFlow(ApiState(Status.LOADING, LocationDto(), ""))
    val characters =
        MutableStateFlow(ApiState(Status.LOADING, mutableListOf<CharacterForListDto>(), ""))
    private val network: Boolean = internetChecker.isOnline()

    init {
        getLocation(locationID)
    }

    private fun getLocation(locationID: Int) {
        location.value = ApiState.loading()
        viewModelScope.launch {
            val gottenLocation: Flow<ApiState<LocationDto>> = if (network) {
                repository.getLocation(locationID)
            } else {
                repository.getLocationDb(locationID)
            }
            gottenLocation
                .catch {
                    location.value = ApiState.error(it.message.toString())
                }
                .collect {
                    location.value = ApiState.success(it.data)
                    if (network) {
                        val charactersId = Separators.separateIdFromUrlCharacter(location.value.data?.residents)
                        getCharacters(charactersId)
                    } else {
                        val episodeId = location.value.data?.id
                        getCharactersDb(episodeId!!)
                    }
                    /*when {
                        charactersId != "" -> getCharacters(charactersId)
                        charactersId.contains(",") -> {
                            getCharacters(charactersId)
                        }
                        else -> characters.value = ApiState.success(mutableListOf())
                    }*/
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
                saveInDb(it.data!!)
                characters.value =
                    ApiState.success(CharacterForListDto.characterToForListDto(it.data))
            }
    }

    private suspend fun getCharactersDb(locationID: Int) {
        repository.getCharacterListDb(locationID)
            .catch {
                characters.value = ApiState.error(it.message.toString())
            }
            .collect {
                characters.value =
                    ApiState.success(CharacterForListDto.characterToForListDto(it.data!!))
            }
    }

    private suspend fun saveInDb(characterList: MutableList<Character>) {
        database.getCharacterDao().insertAll(CharacterDb.characterToDb(characterList))
        val listOfCharacterToEpisodes = Converters.convertToCEJoin(characterList)
        database.getCharacterEpisodeJoinDao().insertAll(listOfCharacterToEpisodes)
    }

    fun openFragment(character: CharacterForListDto?) {
        if (character?.name != "") {
            val fragment: Fragment = CharacterDetailsFragment.newInstance(character?.id!!)
            mainViewModel.changeCurrentDetailsFragment(fragment)
        }
    }
}