package com.example.aston_courseproject_rickmorty.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.aston_courseproject_rickmorty.model.EpisodePagingSource
import com.example.aston_courseproject_rickmorty.model.mediator.EpisodeRemoteMediator
import com.example.aston_courseproject_rickmorty.model.database.ItemsDatabase
import com.example.aston_courseproject_rickmorty.model.dto.EpisodeForListDto
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices
import kotlinx.coroutines.flow.Flow

class EpisodeRepository(
    private val mService: RetrofitServices,
    private val database: ItemsDatabase
) {

    @ExperimentalPagingApi
    fun getEpisodesFromNetwork(): Flow<PagingData<EpisodeForListDto>> {
        return Pager(PagingConfig(pageSize = 20, maxSize = 60),
            pagingSourceFactory = { EpisodePagingSource(mService) }).flow
    }

    @ExperimentalPagingApi
    fun getEpisodesFromDb(): Flow<PagingData<EpisodeForListDto>> {
        return Pager(PagingConfig(pageSize = 20, maxSize = 60),
            pagingSourceFactory = { database.getEpisodeDao().getAll() }).flow
    }

    @ExperimentalPagingApi
    fun getEpisodesFromMediator(): Flow<PagingData<EpisodeForListDto>> {
        return Pager(PagingConfig(pageSize = 20, maxSize = 60),
            remoteMediator = EpisodeRemoteMediator(mService, database),
            pagingSourceFactory = { database.getEpisodeDao().getAll() }).flow
    }
}