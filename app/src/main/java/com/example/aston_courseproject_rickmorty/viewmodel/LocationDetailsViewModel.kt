package com.example.aston_courseproject_rickmorty.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aston_courseproject_rickmorty.model.Character
import com.example.aston_courseproject_rickmorty.model.Location
import com.example.aston_courseproject_rickmorty.model.LocationDetailsModel

class LocationDetailsViewModel(locationID: Int) : ViewModel() {
    val locationModel = LocationDetailsModel(locationID)
    val currentLocation = MutableLiveData<Location>()
    val characterList = MutableLiveData<MutableList<Character>>()

    init {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                currentLocation.value = locationModel.getOneLocation()
                characterList.value = locationModel.getSeveralCharacters()
            },
            1000 // value in milliseconds
        )
    }
}