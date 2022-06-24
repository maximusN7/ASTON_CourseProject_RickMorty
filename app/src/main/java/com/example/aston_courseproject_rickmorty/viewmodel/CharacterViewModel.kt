package com.example.aston_courseproject_rickmorty.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.aston_courseproject_rickmorty.MainViewModel
import com.example.aston_courseproject_rickmorty.fragments.CharacterDetailsFragment
import com.example.aston_courseproject_rickmorty.fragments.EpisodeDetailsFragment
import com.example.aston_courseproject_rickmorty.model.Character
import com.example.aston_courseproject_rickmorty.model.CharacterModel
import com.example.aston_courseproject_rickmorty.model.CharacterPagingSource
import com.example.aston_courseproject_rickmorty.model.Episode
import kotlinx.coroutines.flow.Flow

class CharacterViewModel(val mainViewModel: MainViewModel) : ViewModel() {
    /*val characterModel = CharacterModel()
    val characterList = MutableLiveData<MutableList<Character>>()
    var retrofitServices: RetrofitServices

    init {
        retrofitServices = Common.retrofitService
        Handler(Looper.getMainLooper()).postDelayed(
            {
                characterList.value = characterModel.getCharacterList()
            },
            1000 // value in milliseconds
        )
    }*/

    val characterList: Flow<PagingData<Character>> = Pager (PagingConfig(pageSize = 20, maxSize = 120),
        pagingSourceFactory = {CharacterPagingSource()}).flow.cachedIn(viewModelScope)

    fun openFragment(character: Character?) {
        val fragment: Fragment = CharacterDetailsFragment.newInstance(character?.id!!)
        mainViewModel.changeCurrentDetailsFragment(fragment)
    }
}