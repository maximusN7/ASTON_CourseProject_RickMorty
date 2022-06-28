package com.example.aston_courseproject_rickmorty.viewmodel

import androidx.fragment.app.Fragment
import androidx.paging.*
import com.example.aston_courseproject_rickmorty.MainViewModel
import com.example.aston_courseproject_rickmorty.fragments.CharacterDetailsFragment
import com.example.aston_courseproject_rickmorty.fragments.dialogs.CharacterFilterDialog
import com.example.aston_courseproject_rickmorty.model.CharacterForList
import com.example.aston_courseproject_rickmorty.model.database.CharacterDatabase
import com.example.aston_courseproject_rickmorty.repository.CharacterRepository
import com.example.aston_courseproject_rickmorty.retrofit.Common
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices

@ExperimentalPagingApi
class CharacterViewModel(val mainViewModel: MainViewModel, private val dialogProcessor: CharacterFilterDialog, val database: CharacterDatabase) : BaseCharacterViewModel() {

    var retrofitServices: RetrofitServices = Common.retrofitService
    private val repository = CharacterRepository(retrofitServices, database)
    override val dataSource = repository.getCharactersFromMediator()

    fun openFragment(character: CharacterForList?) {
        val fragment: Fragment = CharacterDetailsFragment.newInstance(character?.id!!)
        mainViewModel.changeCurrentDetailsFragment(fragment)
    }

    fun openFilterDialog() {
        dialogProcessor.showDialog()
    }
}