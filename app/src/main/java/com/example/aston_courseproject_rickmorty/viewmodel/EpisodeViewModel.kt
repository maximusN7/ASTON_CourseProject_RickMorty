package com.example.aston_courseproject_rickmorty.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aston_courseproject_rickmorty.model.Episode
import com.example.aston_courseproject_rickmorty.model.EpisodeModel

class EpisodeViewModel : ViewModel() {

    val episodeModel = EpisodeModel()
    val episodeList = MutableLiveData<MutableList<Episode>>()

    init {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                episodeList.value = episodeModel.getEpisodeList()
            },
            1000 // value in milliseconds
        )
    }
}