package com.example.aston_courseproject_rickmorty.viewmodel

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aston_courseproject_rickmorty.model.Character
import com.example.aston_courseproject_rickmorty.model.CharacterModel

class CharacterViewModel : ViewModel() {
    val characterModel = CharacterModel()
    val characterList = MutableLiveData<MutableList<Character>>()

    init {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                Log.e("AAA", "000")
                characterList.value = characterModel.getCharacterList()
                Log.e("AAA", "222")
            },
            1000 // value in milliseconds
        )
    }
}