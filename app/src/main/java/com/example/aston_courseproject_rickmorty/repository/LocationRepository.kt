package com.example.aston_courseproject_rickmorty.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.aston_courseproject_rickmorty.model.database.ItemsDatabase
import com.example.aston_courseproject_rickmorty.model.mediator.LocationRemoteMediator
import com.example.aston_courseproject_rickmorty.model.dto.LocationForListDto
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices
import kotlinx.coroutines.flow.Flow

class LocationRepository(
    private val mService: RetrofitServices,
    private val database: ItemsDatabase
) {

    @ExperimentalPagingApi
    fun getLocationsFromMediator(
        name: String,
        type: String,
        dimension: String
    ): Flow<PagingData<LocationForListDto>> {
        return Pager(PagingConfig(pageSize = 20, maxSize = 60),
            remoteMediator = LocationRemoteMediator(
                mService,
                database,
                mutableListOf(name, type, dimension)
            ),
            pagingSourceFactory = {
                database.getLocationDao().getSeveralForFilter(name, type, dimension)
            }).flow
    }
}