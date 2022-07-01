package com.example.aston_courseproject_rickmorty.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.aston_courseproject_rickmorty.MainViewModel
import com.example.aston_courseproject_rickmorty.fragments.LocationDetailsFragment
import com.example.aston_courseproject_rickmorty.fragments.dialogs.LocationFilterDialog
import com.example.aston_courseproject_rickmorty.model.*
import com.example.aston_courseproject_rickmorty.model.database.ItemsDatabase
import com.example.aston_courseproject_rickmorty.model.dto.LocationForListDto
import com.example.aston_courseproject_rickmorty.repository.LocationRepository
import com.example.aston_courseproject_rickmorty.retrofit.Common
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class LocationViewModel(val mainViewModel: MainViewModel, private val dialogProcessor: LocationFilterDialog, val database: ItemsDatabase) : ViewModel() {

    var retrofitServices: RetrofitServices = Common.retrofitService
    private val repository = LocationRepository(retrofitServices, database)
    private val dataSource = repository.getLocationsFromMediator()

    val locations: Flow<PagingData<LocationForListDto>> by lazy {
        dataSource.cachedIn(viewModelScope)
    }

    fun openFragment(location: LocationForListDto?) {
        val fragment: Fragment = LocationDetailsFragment.newInstance(location?.id!!)
        mainViewModel.changeCurrentDetailsFragment(fragment)
    }

    fun openFilterDialog() {
        dialogProcessor.showDialog()
    }
}