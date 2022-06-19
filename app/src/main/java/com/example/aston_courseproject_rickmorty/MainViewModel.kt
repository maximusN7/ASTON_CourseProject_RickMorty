package com.example.aston_courseproject_rickmorty

import android.provider.Settings.Global.getString
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aston_courseproject_rickmorty.fragments.CharacterFragment
import com.example.aston_courseproject_rickmorty.fragments.EpisodeFragment
import com.example.aston_courseproject_rickmorty.fragments.LocationFragment


class MainViewModel(val mResourceProvider: ResourceProvider) : ViewModel() {

    val titleString = MutableLiveData<String>()
    val currentFragment = MutableLiveData<Fragment>()

    init {
        titleString.value = mResourceProvider.getString("menu_character")
        currentFragment.value = CharacterFragment()
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun changeCurrentFragment(fragment: Fragment) {
        titleString.value = when (fragment) {
            is CharacterFragment -> mResourceProvider.getString("menu_character")
            is EpisodeFragment -> mResourceProvider.getString("menu_episode")
            is LocationFragment -> mResourceProvider.getString("menu_location")
            else -> ""
        }
        currentFragment.value = fragment
    }
}