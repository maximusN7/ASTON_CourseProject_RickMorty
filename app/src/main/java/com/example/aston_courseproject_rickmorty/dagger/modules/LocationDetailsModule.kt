package com.example.aston_courseproject_rickmorty.dagger.modules

import androidx.paging.ExperimentalPagingApi
import com.example.aston_courseproject_rickmorty.model.repository.LocationDetailsRepository
import com.example.aston_courseproject_rickmorty.utils.InternetConnectionChecker
import com.example.aston_courseproject_rickmorty.viewmodel.factory.LocationDetailsViewModelFactory
import dagger.Module
import dagger.Provides

@ExperimentalPagingApi
@Module
class LocationDetailsModule {

    @Provides
    fun provideLocationDetailsViewModelFactory(locationId: Int, repository: LocationDetailsRepository, internetChecker: InternetConnectionChecker): LocationDetailsViewModelFactory {
        return LocationDetailsViewModelFactory(locationId, repository, internetChecker)
    }
}