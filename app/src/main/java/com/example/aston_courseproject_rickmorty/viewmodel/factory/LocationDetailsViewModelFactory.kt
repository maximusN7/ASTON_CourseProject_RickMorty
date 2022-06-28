package com.example.aston_courseproject_rickmorty.viewmodel.factory

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.example.aston_courseproject_rickmorty.MainViewModel
import com.example.aston_courseproject_rickmorty.MainViewModelFactory
import com.example.aston_courseproject_rickmorty.viewmodel.LocationDetailsViewModel
@ExperimentalPagingApi
class LocationDetailsViewModelFactory(private val locationID: Int, context: Context, owner: FragmentActivity) : ViewModelProvider.Factory {
    private var mainViewModel: MainViewModel = ViewModelProvider(owner, MainViewModelFactory(context))[MainViewModel::class.java]

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LocationDetailsViewModel(locationID, mainViewModel) as T
    }
}