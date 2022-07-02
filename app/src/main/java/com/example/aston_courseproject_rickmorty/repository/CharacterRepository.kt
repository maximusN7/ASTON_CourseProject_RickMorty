package com.example.aston_courseproject_rickmorty.repository

import androidx.paging.*
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
    fun getCharactersFromMediator(
        name: String,
        status: String,
        species: String,
        type: String,
        gender: String
    ): Flow<PagingData<CharacterForListDto>> {
        return Pager(PagingConfig(pageSize = 20, maxSize = 60),
            remoteMediator = CharacterRemoteMediator(
                mService,
                database,
                mutableListOf(name, status, species, type, gender)
            ),
            pagingSourceFactory = {
                database.getCharacterDao().getSeveralForFilter(name, status, species, type, gender)
            }).flow
    }
}