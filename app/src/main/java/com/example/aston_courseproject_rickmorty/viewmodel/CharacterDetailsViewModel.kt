package com.example.aston_courseproject_rickmorty.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import com.example.aston_courseproject_rickmorty.MainViewModel
import com.example.aston_courseproject_rickmorty.fragments.EpisodeDetailsFragment
import com.example.aston_courseproject_rickmorty.fragments.LocationDetailsFragment
import com.example.aston_courseproject_rickmorty.model.Episode
import com.example.aston_courseproject_rickmorty.model.Location
import com.example.aston_courseproject_rickmorty.model.database.EpisodeDb
import com.example.aston_courseproject_rickmorty.model.database.ItemsDatabase
import com.example.aston_courseproject_rickmorty.model.database.LocationDb
import com.example.aston_courseproject_rickmorty.model.dto.CharacterDto
import com.example.aston_courseproject_rickmorty.model.dto.EpisodeForListDto
import com.example.aston_courseproject_rickmorty.model.dto.LocationForListDto
import com.example.aston_courseproject_rickmorty.repository.CharacterDetailsRepository
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
class CharacterDetailsViewModel(
    characterID: Int, val mainViewModel: MainViewModel,
    val database: ItemsDatabase,
   internetChecker: InternetConnectionChecker
) : ViewModel() {
    var retrofitServices: RetrofitServices = Common.retrofitService
    private val repository = CharacterDetailsRepository(retrofitServices, database)
    //private val dataSource = repository.getEpisodeFromNetwork()

    val character = MutableStateFlow(ApiState(Status.LOADING, CharacterDto(), ""))
    val origin = MutableStateFlow(ApiState(Status.LOADING, LocationForListDto(), ""))
    val location = MutableStateFlow(ApiState(Status.LOADING, LocationForListDto(), ""))
    val episodes = MutableStateFlow(ApiState(Status.LOADING, mutableListOf<EpisodeForListDto>(), ""))
    private val network: Boolean = internetChecker.isOnline()

    init {
        getCharacter(characterID)
    }

    private fun getCharacter(characterID: Int) {
        character.value = ApiState.loading()
        viewModelScope.launch {
            val gottenCharacter: Flow<ApiState<CharacterDto>> = if (network) {
                repository.getCharacter(characterID)
            } else {
                repository.getCharacterDb(characterID)
            }
            gottenCharacter
                .catch {
                    character.value = ApiState.error(it.message.toString())
                }
                .collect {
                    character.value = ApiState.success(it.data)
                    if (network) {
                        val episodesId = Separators.separateIdFromUrlEpisode(character.value.data?.episode)
                        getEpisodes(episodesId)
                    } else {
                        val characterId = character.value.data?.id
                        getEpisodesDb(characterId!!)
                    }
                    if (character.value.data?.location?.name != "unknown") {
                        val locationId = Separators.separateIdFromUrlLocation(character.value.data?.location?.url)
                        if (network) {
                            getLocation(locationId, location)
                        } else {
                            getLocationDb(locationId, location)
                        }
                    } else {
                        location.value = ApiState.success(LocationForListDto(id = -1, name = "unknown", type = "", dimension = ""))
                    }
                    if (character.value.data?.origin?.name != "unknown") {
                        val originId = Separators.separateIdFromUrlLocation(character.value.data?.origin?.url)
                        if (network) {
                            getLocation(originId, origin)
                        } else {
                            getLocationDb(originId, origin)
                        }
                    } else {
                        origin.value = ApiState.success(LocationForListDto(id = -1, name = "unknown", type = "", dimension = ""))
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
                saveEpisodesInDb(it.data!!)
                episodes.value = ApiState.success(EpisodeForListDto.episodeToForListDto(it.data))
            }
    }

    private suspend fun getEpisodesDb(characterID: Int) {
        repository.getEpisodeListDb(characterID)
            .catch {
                episodes.value = ApiState.error(it.message.toString())
            }
            .collect {
                episodes.value = ApiState.success(EpisodeForListDto.episodeToForListDto(it.data!!))
            }
    }

    private suspend fun getLocation(locationId: Int, loc: MutableStateFlow<ApiState<LocationForListDto>>) {
        repository.getLocation(locationId)
            .catch {
                loc.value = ApiState.error(it.message.toString())
            }
            .collect {
                saveLocationInDb(it.data!!)
                loc.value = ApiState.success(LocationForListDto.locationToForListDto(it.data))
            }
    }

    private suspend fun getLocationDb(locationId: Int, loc: MutableStateFlow<ApiState<LocationForListDto>>) {
        repository.getLocationDb(locationId)
            .catch {
                loc.value = ApiState.error(it.message.toString())
            }
            .collect {
                loc.value = if (it.data != null) {
                    ApiState.success(LocationForListDto.locationToForListDto(it.data))
                } else {
                    ApiState.success(LocationForListDto(0, "", "", ""))
                }
            }
    }

    private suspend fun saveEpisodesInDb(episodesList: MutableList<Episode>) {
        database.getEpisodeDao().insertAll(EpisodeDb.episodeToDb(episodesList))
        val listOfCharacterToEpisodes = Converters.convertToECJoin(episodesList)
        database.getEpisodeCharacterJoinDao().insertAll(listOfCharacterToEpisodes)
    }

    private suspend fun saveLocationInDb(location: Location) {
        database.getLocationDao().insertAll(LocationDb.locationToDb(mutableListOf(location)))
        val listOfCharacterToEpisodes = Converters.convertToLCJoin(mutableListOf(location))
        database.getLocationCharacterJoinDao().insertAll(listOfCharacterToEpisodes)
    }

    fun openFragment(episode: EpisodeForListDto?) {
        if (episode?.name != "") {
            val fragment: Fragment = EpisodeDetailsFragment.newInstance(episode?.id!!)
            mainViewModel.changeCurrentDetailsFragment(fragment)
        }
    }

    fun openFragment(location: LocationForListDto?) {
        if (location?.name != "unknown" && location?.name != "") {
            val fragment: Fragment = LocationDetailsFragment.newInstance(location?.id!!)
            mainViewModel.changeCurrentDetailsFragment(fragment)
        }
    }
}