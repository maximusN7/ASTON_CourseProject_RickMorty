package com.example.aston_courseproject_rickmorty.viewmodel.factory

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.example.aston_courseproject_rickmorty.MainViewModel
import com.example.aston_courseproject_rickmorty.MainViewModelFactory
import com.example.aston_courseproject_rickmorty.fragments.dialogs.LocationFilterDialog
import com.example.aston_courseproject_rickmorty.viewmodel.LocationViewModel
@ExperimentalPagingApi
class LocationViewModelFactory(context: Context, owner: FragmentActivity) : ViewModelProvider.Factory {
    private var mainViewModel: MainViewModel = ViewModelProvider(owner, MainViewModelFactory(context))[MainViewModel::class.java]
    private var dialogProcessor = LocationFilterDialog(context)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LocationViewModel(mainViewModel, dialogProcessor) as T
    }
}