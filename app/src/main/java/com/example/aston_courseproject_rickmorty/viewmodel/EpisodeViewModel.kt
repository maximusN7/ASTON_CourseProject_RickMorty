package com.example.aston_courseproject_rickmorty.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.aston_courseproject_rickmorty.MainViewModel
import com.example.aston_courseproject_rickmorty.fragments.CharacterDetailsFragment
import com.example.aston_courseproject_rickmorty.fragments.EpisodeDetailsFragment
import com.example.aston_courseproject_rickmorty.fragments.dialogs.CharacterFilterDialog
import com.example.aston_courseproject_rickmorty.fragments.dialogs.EpisodeFilterDialog
import com.example.aston_courseproject_rickmorty.model.*
import com.example.aston_courseproject_rickmorty.retrofit.Common
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices
import kotlinx.coroutines.flow.Flow
@ExperimentalPagingApi
class EpisodeViewModel(val mainViewModel: MainViewModel, private val dialogProcessor: EpisodeFilterDialog) : ViewModel() {

    /*val episodeModel = EpisodeModel()
    val episodeList = MutableLiveData<MutableList<Episode>>()

    init {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                episodeList.value = episodeModel.getEpisodeList()
            },
            1000 // value in milliseconds
        )
    }*/

    val episodeList: Flow<PagingData<Episode>> = Pager (PagingConfig(pageSize = 20, maxSize = 100),
        pagingSourceFactory = { EpisodePagingSource() }).flow.cachedIn(viewModelScope)

    fun openFragment(episode: Episode?) {
        val fragment: Fragment = EpisodeDetailsFragment.newInstance(episode?.id!!)
        mainViewModel.changeCurrentDetailsFragment(fragment)
    }

    fun openFilterDialog() {
        dialogProcessor.showDialog()
    }
}