package com.example.aston_courseproject_rickmorty.viewmodel.factory

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.example.aston_courseproject_rickmorty.MainViewModel
import com.example.aston_courseproject_rickmorty.MainViewModelFactory
import com.example.aston_courseproject_rickmorty.fragments.dialogs.Filter
import com.example.aston_courseproject_rickmorty.model.database.ItemsDatabase
import com.example.aston_courseproject_rickmorty.viewmodel.CharacterViewModel

@ExperimentalPagingApi
class CharacterViewModelFactory(context: Context, appContext: Context, owner: FragmentActivity, private val filterList: MutableList<Filter>) : ViewModelProvider.Factory {
    private var mainViewModel: MainViewModel = ViewModelProvider(owner, MainViewModelFactory(context))[MainViewModel::class.java]
    private var database = ItemsDatabase.getDatabase(appContext)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CharacterViewModel(mainViewModel, database, filterList) as T
    }
}