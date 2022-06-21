package com.example.aston_courseproject_rickmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LocationDetailsViewModelFactory(val locationID: Int) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LocationDetailsViewModel(locationID) as T
    }
}