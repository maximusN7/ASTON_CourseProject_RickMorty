package com.example.aston_courseproject_rickmorty.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.example.aston_courseproject_rickmorty.MainViewModel
import com.example.aston_courseproject_rickmorty.fragments.CharacterDetailsFragment
import com.example.aston_courseproject_rickmorty.fragments.EpisodeDetailsFragment
import com.example.aston_courseproject_rickmorty.model.*

@ExperimentalPagingApi
class EpisodeDetailsViewModel(episodeID: Int, val mainViewModel: MainViewModel) : ViewModel() {
    val episodeModel = EpisodeDetailsModel(episodeID)
    val currentEpisode = MutableLiveData<Episode>()
    val characterList = MutableLiveData<MutableList<Character>>()

    init {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                currentEpisode.value = episodeModel.getOneEpisode()
                characterList.value = episodeModel.getSeveralCharacters()
            },
            1000 // value in milliseconds
        )
    }

    fun openFragment(character: Character?) {
        val fragment: Fragment = CharacterDetailsFragment.newInstance(character?.id!!)
        mainViewModel.changeCurrentDetailsFragment(fragment)
    }
}