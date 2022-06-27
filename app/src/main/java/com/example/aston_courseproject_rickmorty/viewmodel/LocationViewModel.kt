package com.example.aston_courseproject_rickmorty.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.aston_courseproject_rickmorty.MainViewModel
import com.example.aston_courseproject_rickmorty.fragments.CharacterDetailsFragment
import com.example.aston_courseproject_rickmorty.fragments.LocationDetailsFragment
import com.example.aston_courseproject_rickmorty.fragments.dialogs.EpisodeFilterDialog
import com.example.aston_courseproject_rickmorty.fragments.dialogs.LocationFilterDialog
import com.example.aston_courseproject_rickmorty.model.*
import com.example.aston_courseproject_rickmorty.retrofit.Common
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices
import kotlinx.coroutines.flow.Flow

class LocationViewModel(val mainViewModel: MainViewModel, private val dialogProcessor: LocationFilterDialog) : ViewModel() {

    /*val locationModel = LocationModel()
    val locationList = MutableLiveData<MutableList<Location>>()

    init {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                locationList.value = locationModel.getLocationList()
            },
            1000 // value in milliseconds
        )
    }*/

    val locationList: Flow<PagingData<Location>> = Pager (PagingConfig(pageSize = 20, maxSize = 100),
        pagingSourceFactory = { LocationPagingSource() }).flow.cachedIn(viewModelScope)

    fun openFragment(location: Location?) {
        val fragment: Fragment = LocationDetailsFragment.newInstance(location?.id!!)
        mainViewModel.changeCurrentDetailsFragment(fragment)
    }

    fun openFilterDialog() {
        dialogProcessor.showDialog()
    }
}