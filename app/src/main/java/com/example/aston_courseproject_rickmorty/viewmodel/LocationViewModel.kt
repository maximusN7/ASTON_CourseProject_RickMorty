package com.example.aston_courseproject_rickmorty.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aston_courseproject_rickmorty.model.Location
import com.example.aston_courseproject_rickmorty.model.LocationModel

class LocationViewModel : ViewModel() {

    val locationModel = LocationModel()
    val locationList = MutableLiveData<MutableList<Location>>()

    init {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                locationList.value = locationModel.getLocationList()
            },
            1000 // value in milliseconds
        )
    }
}