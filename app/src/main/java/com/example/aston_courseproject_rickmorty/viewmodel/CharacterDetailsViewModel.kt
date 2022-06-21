package com.example.aston_courseproject_rickmorty.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aston_courseproject_rickmorty.model.*

class CharacterDetailsViewModel(characterID: Int) : ViewModel() {
    val characterModel = CharacterDetailsModel(characterID)
    val currentCharacter = MutableLiveData<Character>()
    val currentOrigin = MutableLiveData<Location>()
    val currentLocation = MutableLiveData<Location>()
    val episodeList = MutableLiveData<MutableList<Episode>>()

    init {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                currentCharacter.value = characterModel.getOneCharacter()
                currentOrigin.value = characterModel.getOrigin()
                currentLocation.value = characterModel.getLocation()
                episodeList.value = characterModel.getSeveralEpisodes()
            },
            1000 // value in milliseconds
        )
    }
}