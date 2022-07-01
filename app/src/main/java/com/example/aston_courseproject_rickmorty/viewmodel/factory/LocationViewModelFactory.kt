package com.example.aston_courseproject_rickmorty.viewmodel.factory

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.example.aston_courseproject_rickmorty.MainViewModel
import com.example.aston_courseproject_rickmorty.MainViewModelFactory
import com.example.aston_courseproject_rickmorty.fragments.dialogs.LocationFilterDialog
import com.example.aston_courseproject_rickmorty.model.database.ItemsDatabase
import com.example.aston_courseproject_rickmorty.viewmodel.LocationViewModel

@ExperimentalPagingApi
class LocationViewModelFactory(context: Context, appContext: Context, owner: FragmentActivity) : ViewModelProvider.Factory {
    private var mainViewModel: MainViewModel = ViewModelProvider(owner, MainViewModelFactory(context))[MainViewModel::class.java]
    private var dialogProcessor = LocationFilterDialog(context)
    private var database = ItemsDatabase.getDatabase(appContext)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LocationViewModel(mainViewModel, dialogProcessor, database) as T
    }
}