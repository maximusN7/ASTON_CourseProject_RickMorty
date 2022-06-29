package com.example.aston_courseproject_rickmorty.repository

import com.example.aston_courseproject_rickmorty.model.Character
import com.example.aston_courseproject_rickmorty.model.Episode
import com.example.aston_courseproject_rickmorty.model.Location
import com.example.aston_courseproject_rickmorty.model.database.ItemsDatabase
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

    suspend fun getCharacter(characterId: Int): Flow<ApiState<Character>> {
        return flow {
            val character = mService.getOneCharacter(characterId)
            emit(ApiState.success(character))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getEpisodeList(episodesId: String): Flow<ApiState<MutableList<Episode>>> {
        return flow {
            val characters = mService.getSeveralEpisodes(episodesId)
            emit(ApiState.success(characters))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getLocation(locationId: Int): Flow<ApiState<Location>> {
        return flow {
            val location = mService.getOneLocation(locationId)
            emit(ApiState.success(location))
        }.flowOn(Dispatchers.IO)
    }
}