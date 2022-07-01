package com.example.aston_courseproject_rickmorty.repository

import androidx.paging.*
import com.example.aston_courseproject_rickmorty.model.CharacterPagingSource
import com.example.aston_courseproject_rickmorty.model.database.ItemsDatabase
import com.example.aston_courseproject_rickmorty.model.mediator.CharacterRemoteMediator
import com.example.aston_courseproject_rickmorty.model.dto.CharacterForListDto
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices
import kotlinx.coroutines.flow.Flow

class CharacterRepository(
    private val mService: RetrofitServices,
    private val database: ItemsDatabase
) {

    @ExperimentalPagingApi
    fun getCharactersFromNetwork(): Flow<PagingData<CharacterForListDto>> {
        return Pager(PagingConfig(pageSize = 20, maxSize = 60),
            pagingSourceFactory = { CharacterPagingSource(mService) }).flow
    }

    @ExperimentalPagingApi
    fun getCharactersFromDb(): Flow<PagingData<CharacterForListDto>> {
        return Pager(PagingConfig(pageSize = 20, maxSize = 60),
            pagingSourceFactory = { database.getCharacterDao().getAll() } ).flow
    }

    @ExperimentalPagingApi
    fun getCharactersFromMediator(): Flow<PagingData<CharacterForListDto>> {
        return Pager(PagingConfig(pageSize = 20, maxSize = 60),
            remoteMediator = CharacterRemoteMediator(mService, database),
            pagingSourceFactory = { database.getCharacterDao().getAll() } ).flow
    }
}