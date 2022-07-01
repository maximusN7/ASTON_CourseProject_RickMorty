package com.example.aston_courseproject_rickmorty.repository

import com.example.aston_courseproject_rickmorty.model.Episode
import com.example.aston_courseproject_rickmorty.model.Location
import com.example.aston_courseproject_rickmorty.model.database.EpisodeForListDb
import com.example.aston_courseproject_rickmorty.model.database.ItemsDatabase
import com.example.aston_courseproject_rickmorty.model.database.LocationForListDb
import com.example.aston_courseproject_rickmorty.model.dto.CharacterDto
import com.example.aston_courseproject_rickmorty.retrofit.ApiState
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CharacterDetailsRepository(
    private val mService: RetrofitServices,
    private val database: ItemsDatabase
) {

    suspend fun getCharacter(characterId: Int): Flow<ApiState<CharacterDto>> {
        return flow {
            val character = CharacterDto.characterToDto(mService.getOneCharacter(characterId))
            emit(ApiState.success(character))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCharacterDb(characterId: Int): Flow<ApiState<CharacterDto>> {
        return flow {
            val character = CharacterDto.characterToDto(database.getCharacterDao().getOneById(characterId), database)
            emit(ApiState.success(character))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getEpisodeList(episodesId: String): Flow<ApiState<MutableList<Episode>>> {
        return flow {
            val episodes = mService.getSeveralEpisodes(episodesId)
            emit(ApiState.success(episodes))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getEpisodeListDb(characterId: Int): Flow<ApiState<MutableList<EpisodeForListDb>>> {
        return flow {
            val episodesArray = database.getCharacterEpisodeJoinDao().getEpisodesIdForCharacter(characterId)
            val episodes = mutableListOf<EpisodeForListDb>()
            for (i in episodesArray.indices) {
                episodes.add(database.getEpisodeDao().getOneForListById(episodesArray[i]))
            }
            emit(ApiState.success(episodes))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getLocation(locationId: Int): Flow<ApiState<Location>> {
        return flow {
            val location = mService.getOneLocation(locationId)
            emit(ApiState.success(location))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getLocationDb(locationId: Int): Flow<ApiState<LocationForListDb>> {
        return flow {
            val location = database.getLocationDao().getOneForListById(locationId)
            emit(ApiState.success(location))
        }.flowOn(Dispatchers.IO)
    }
}