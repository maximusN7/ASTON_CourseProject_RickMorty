package com.example.aston_courseproject_rickmorty.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.aston_courseproject_rickmorty.model.*
import com.example.aston_courseproject_rickmorty.retrofit.Common
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices
import kotlinx.coroutines.flow.Flow

class LocationViewModel : ViewModel() {

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

    var retrofitServices: RetrofitServices = Common.retrofitService

    val locationList: Flow<PagingData<Location>> = Pager (PagingConfig(pageSize = 20, maxSize = 100),
        pagingSourceFactory = { LocationPagingSource(retrofitServices) }).flow.cachedIn(viewModelScope)
}