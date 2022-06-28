package com.example.aston_courseproject_rickmorty.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.aston_courseproject_rickmorty.MainViewModel
import com.example.aston_courseproject_rickmorty.fragments.CharacterDetailsFragment
import com.example.aston_courseproject_rickmorty.fragments.dialogs.CharacterFilterDialog
import com.example.aston_courseproject_rickmorty.model.CharacterForList
import com.example.aston_courseproject_rickmorty.model.database.ItemsDatabase
import com.example.aston_courseproject_rickmorty.repository.CharacterRepository
import com.example.aston_courseproject_rickmorty.retrofit.Common
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class CharacterViewModel(val mainViewModel: MainViewModel, private val dialogProcessor: CharacterFilterDialog, val database: ItemsDatabase) : ViewModel() {

    var retrofitServices: RetrofitServices = Common.retrofitService
    private val repository = CharacterRepository(retrofitServices, database)
    private val dataSource = repository.getCharactersFromMediator()

    val characters: Flow<PagingData<CharacterForList>> by lazy {
        dataSource.cachedIn(viewModelScope)
    }

    fun openFragment(character: CharacterForList?) {
        val fragment: Fragment = CharacterDetailsFragment.newInstance(character?.id!!)
        mainViewModel.changeCurrentDetailsFragment(fragment)
    }

    fun openFilterDialog() {
        dialogProcessor.showDialog()
    }
}