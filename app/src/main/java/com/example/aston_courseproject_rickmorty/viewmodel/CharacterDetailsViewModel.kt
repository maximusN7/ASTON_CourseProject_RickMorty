package com.example.aston_courseproject_rickmorty.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aston_courseproject_rickmorty.model.Character
import com.example.aston_courseproject_rickmorty.model.CharacterDetailsModel
import com.example.aston_courseproject_rickmorty.model.CharacterModel

class CharacterDetailsViewModel(characterID: Int) : ViewModel() {
    val characterModel = CharacterDetailsModel(characterID)
    val currentCharacter = MutableLiveData<Character>()

    init {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                currentCharacter.value = characterModel.getOneCharacter()
            },
            500 // value in milliseconds
        )
    }
}