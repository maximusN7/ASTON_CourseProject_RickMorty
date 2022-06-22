package com.example.aston_courseproject_rickmorty.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.aston_courseproject_rickmorty.model.*
import com.example.aston_courseproject_rickmorty.retrofit.Common
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices
import kotlinx.coroutines.flow.Flow

class EpisodeViewModel : ViewModel() {

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

    var retrofitServices: RetrofitServices = Common.retrofitService

    val episodeList: Flow<PagingData<Episode>> = Pager (PagingConfig(pageSize = 20, maxSize = 100),
        pagingSourceFactory = { EpisodePagingSource(retrofitServices) }).flow.cachedIn(viewModelScope)
}