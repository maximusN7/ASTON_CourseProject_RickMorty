package com.example.aston_courseproject_rickmorty.repository

import com.example.aston_courseproject_rickmorty.model.Character
import com.example.aston_courseproject_rickmorty.model.Location
import com.example.aston_courseproject_rickmorty.model.database.ItemsDatabase
import com.example.aston_courseproject_rickmorty.retrofit.ApiState
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class LocationDetailsRepository(
    private val mService: RetrofitServices,
    private val database: ItemsDatabase
) {

    suspend fun getLocation(locationId: Int): Flow<ApiState<Location>> {
        return flow {
            val location = mService.getOneLocation(locationId)
            emit(ApiState.success(location))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCharacterList(charactersId: String): Flow<ApiState<MutableList<Character>>> {
        return flow {
            val characters = mService.getSeveralCharacters(charactersId)
            emit(ApiState.success(characters))
        }.flowOn(Dispatchers.IO)
    }

}