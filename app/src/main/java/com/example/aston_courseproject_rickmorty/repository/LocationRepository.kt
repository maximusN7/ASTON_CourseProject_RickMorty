package com.example.aston_courseproject_rickmorty.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.aston_courseproject_rickmorty.model.LocationForList
import com.example.aston_courseproject_rickmorty.model.LocationPagingSource
import com.example.aston_courseproject_rickmorty.model.database.ItemsDatabase
import com.example.aston_courseproject_rickmorty.model.database.LocationRemoteMediator
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices
import kotlinx.coroutines.flow.Flow

class LocationRepository(
    private val mService: RetrofitServices,
    private val database: ItemsDatabase
) {

    @ExperimentalPagingApi
    fun getLocationsFromNetwork(): Flow<PagingData<LocationForList>> {
        return Pager(PagingConfig(pageSize = 20, maxSize = 60),
            pagingSourceFactory = { LocationPagingSource(mService) }).flow
    }

    @ExperimentalPagingApi
    fun getLocationsFromDb(): Flow<PagingData<LocationForList>> {
        return Pager(PagingConfig(pageSize = 20, maxSize = 60),
            pagingSourceFactory = { database.getLocationDao().getAll() } ).flow
    }

    @ExperimentalPagingApi
    fun getLocationsFromMediator(): Flow<PagingData<LocationForList>> {
        return Pager(PagingConfig(pageSize = 20, maxSize = 60),
            remoteMediator = LocationRemoteMediator(mService, database),
            pagingSourceFactory = { database.getLocationDao().getAll() } ).flow
    }
}