package com.example.aston_courseproject_rickmorty

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModelFactory(val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val mResourceProvider = ResourceProvider(context)
        return MainViewModel(mResourceProvider) as T
    }
}