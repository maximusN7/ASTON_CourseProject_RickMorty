package com.example.aston_courseproject_rickmorty.viewmodel.factory

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.example.aston_courseproject_rickmorty.MainViewModel
import com.example.aston_courseproject_rickmorty.MainViewModelFactory
import com.example.aston_courseproject_rickmorty.fragments.dialogs.CharacterFilterDialog
import com.example.aston_courseproject_rickmorty.model.database.CharacterDatabase
import com.example.aston_courseproject_rickmorty.viewmodel.CharacterViewModel

@ExperimentalPagingApi
class CharacterViewModelFactory(context: Context, appContext: Context, owner: FragmentActivity) : ViewModelProvider.Factory {
    private var mainViewModel: MainViewModel = ViewModelProvider(owner, MainViewModelFactory(context))[MainViewModel::class.java]
    private var dialogProcessor = CharacterFilterDialog(context)
    private var database = CharacterDatabase.getDatabase(appContext)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CharacterViewModel(mainViewModel, dialogProcessor, database) as T
    }
}