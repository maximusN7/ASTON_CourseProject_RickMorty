package com.example.aston_courseproject_rickmorty.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import com.example.aston_courseproject_rickmorty.MainViewModel
import com.example.aston_courseproject_rickmorty.fragments.CharacterDetailsFragment
import com.example.aston_courseproject_rickmorty.model.*
import com.example.aston_courseproject_rickmorty.model.database.CharacterDb
import com.example.aston_courseproject_rickmorty.model.database.CharacterEpisodeJoin
import com.example.aston_courseproject_rickmorty.model.database.ItemsDatabase
import com.example.aston_courseproject_rickmorty.model.dto.CharacterForListDto
import com.example.aston_courseproject_rickmorty.model.dto.EpisodeDto
import com.example.aston_courseproject_rickmorty.repository.EpisodeDetailsRepository
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
class EpisodeDetailsViewModel(
    episodeID: Int,
    val mainViewModel: MainViewModel,
    val database: ItemsDatabase,
    internetChecker: InternetConnectionChecker
) : ViewModel() {
    var retrofitServices: RetrofitServices = Common.retrofitService
    private val repository = EpisodeDetailsRepository(retrofitServices, database)

    val episode = MutableStateFlow(ApiState(Status.LOADING, EpisodeDto(), ""))
    val characters =
        MutableStateFlow(ApiState(Status.LOADING, mutableListOf<CharacterForListDto>(), ""))
    private val network: Boolean = internetChecker.isOnline()

    init {
        getEpisode(episodeID)
    }

    private fun getEpisode(episodeID: Int) {
        episode.value = ApiState.loading()
        viewModelScope.launch {
            val gottenEpisode: Flow<ApiState<EpisodeDto>> = if (network) {
                repository.getEpisode(episodeID)
            } else {
                repository.getEpisodeDb(episodeID)
            }
            gottenEpisode
                .catch {
                    episode.value = ApiState.error(it.message.toString())
                }
                .collect {
                    episode.value = ApiState.success(it.data)
                    if (network) {
                        val charactersId = Separators.separateIdFromUrlCharacter(episode.value.data?.characters)
                        getCharacters(charactersId)
                    } else {
                        val episodeId = episode.value.data?.id
                        getCharactersDb(episodeId!!)
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
                saveInDb(it.data!!)
                characters.value =
                    ApiState.success(CharacterForListDto.characterToForListDto(it.data))
            }
    }

    private suspend fun getCharactersDb(episodeID: Int) {
        repository.getCharacterListDb(episodeID)
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