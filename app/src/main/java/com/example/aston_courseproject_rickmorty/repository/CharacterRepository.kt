package com.example.aston_courseproject_rickmorty.repository

import androidx.paging.*
import com.example.aston_courseproject_rickmorty.model.Character
import com.example.aston_courseproject_rickmorty.model.CharacterForList
import com.example.aston_courseproject_rickmorty.model.CharacterPagingSource
import com.example.aston_courseproject_rickmorty.model.database.CharacterDatabase
import com.example.aston_courseproject_rickmorty.model.database.CharacterRemoteMediator
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices
import kotlinx.coroutines.flow.Flow

class CharacterRepository(
    private val mService: RetrofitServices,
    private val database: CharacterDatabase
) {

    @ExperimentalPagingApi
    fun getCharactersFromNetwork(): Flow<PagingData<CharacterForList>> {
        return Pager(PagingConfig(pageSize = 20, maxSize = 60),
            pagingSourceFactory = { CharacterPagingSource(mService) }).flow
    }

    @ExperimentalPagingApi
    fun getCharactersFromDb(): Flow<PagingData<CharacterForList>> {
        return Pager(PagingConfig(pageSize = 20, maxSize = 60),
            pagingSourceFactory = { database.getCharacterDao().getAll() } ).flow
    }

    @ExperimentalPagingApi
    fun getCharactersFromMediator(): Flow<PagingData<CharacterForList>> {
        return Pager(PagingConfig(pageSize = 20, maxSize = 60),
            remoteMediator = CharacterRemoteMediator(mService, database),
            pagingSourceFactory = { database.getCharacterDao().getAll() } ).flow
    }
}