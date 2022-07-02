package com.example.aston_courseproject_rickmorty.repository

import androidx.paging.*
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
    fun getEpisodesFromMediator(
        name: String,
        episode: String
    ): Flow<PagingData<EpisodeForListDto>> {
        return Pager(PagingConfig(pageSize = 20, maxSize = 60),
            remoteMediator = EpisodeRemoteMediator(
                mService, database,
                mutableListOf(name, episode)
            ),
            pagingSourceFactory = {
                database.getEpisodeDao().getSeveralForFilter(name, episode)
            }).flow
    }
}