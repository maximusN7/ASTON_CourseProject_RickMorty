package com.example.aston_courseproject_rickmorty.viewmodel.factory


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.example.aston_courseproject_rickmorty.repository.LocationDetailsRepository
import com.example.aston_courseproject_rickmorty.utils.InternetConnectionChecker
import com.example.aston_courseproject_rickmorty.viewmodel.LocationDetailsViewModel

@ExperimentalPagingApi
class LocationDetailsViewModelFactory(
    private val locationID: Int,
    val repository: LocationDetailsRepository,
    private val internetChecker: InternetConnectionChecker
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LocationDetailsViewModel(locationID, repository, internetChecker) as T
    }
}