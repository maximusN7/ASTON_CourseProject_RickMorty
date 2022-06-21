package com.example.aston_courseproject_rickmorty.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aston_courseproject_rickmorty.model.*

class EpisodeDetailsViewModel(episodeID: Int) : ViewModel() {
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
}