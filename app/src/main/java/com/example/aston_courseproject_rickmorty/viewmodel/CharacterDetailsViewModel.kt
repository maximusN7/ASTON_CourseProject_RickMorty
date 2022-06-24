package com.example.aston_courseproject_rickmorty.viewmodel

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aston_courseproject_rickmorty.MainViewModel
import com.example.aston_courseproject_rickmorty.fragments.EpisodeDetailsFragment
import com.example.aston_courseproject_rickmorty.fragments.LocationDetailsFragment
import com.example.aston_courseproject_rickmorty.model.*

class CharacterDetailsViewModel(characterID: Int, val mainViewModel: MainViewModel) : ViewModel() {
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

    fun openFragment(episode: Episode?) {
        val fragment: Fragment = EpisodeDetailsFragment.newInstance(episode?.id!!)
        mainViewModel.changeCurrentDetailsFragment(fragment)
    }

    fun openFragment(location: Location?) {
        if (location?.name != "unknown") {
            val fragment: Fragment = LocationDetailsFragment.newInstance(location?.id!!)
            mainViewModel.changeCurrentDetailsFragment(fragment)
        }
    }
}