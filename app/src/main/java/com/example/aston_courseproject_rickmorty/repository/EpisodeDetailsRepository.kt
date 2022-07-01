package com.example.aston_courseproject_rickmorty.repository

import com.example.aston_courseproject_rickmorty.model.Character
import com.example.aston_courseproject_rickmorty.model.database.CharacterForListDb
import com.example.aston_courseproject_rickmorty.model.database.ItemsDatabase
import com.example.aston_courseproject_rickmorty.model.dto.EpisodeDto
import com.example.aston_courseproject_rickmorty.retrofit.ApiState
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class EpisodeDetailsRepository(
    private val mService: RetrofitServices,
    private val database: ItemsDatabase
) {

    suspend fun getEpisode(episodeId: Int): Flow<ApiState<EpisodeDto>> {
        return flow {
            val episode = EpisodeDto.episodeToDto(mService.getOneEpisode(episodeId))
            emit(ApiState.success(episode))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCharacterList(charactersId: String): Flow<ApiState<MutableList<Character>>> {
        return flow {
            val characters = mService.getSeveralCharacters(charactersId)
            emit(ApiState.success(characters))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getEpisodeDb(episodeId: Int): Flow<ApiState<EpisodeDto>> {
        return flow {
            val episode = EpisodeDto.episodeToDto(database.getEpisodeDao().getOneById(episodeId), database)
            emit(ApiState.success(episode))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCharacterListDb(episodeId: Int): Flow<ApiState<MutableList<CharacterForListDb>>> {
        return flow {
            val charactersArray = database.getEpisodeCharacterJoinDao().getCharactersIdForEpisode(episodeId)
            val characters = mutableListOf<CharacterForListDb>()
            for (i in charactersArray.indices) {
                characters.add(database.getCharacterDao().getOneForListById(charactersArray[i]))
            }
            emit(ApiState.success(characters))
        }.flowOn(Dispatchers.IO)
    }
}