package com.example.aston_courseproject_rickmorty.viewmodel

import android.app.Dialog
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.aston_courseproject_rickmorty.MainViewModel
import com.example.aston_courseproject_rickmorty.R
import com.example.aston_courseproject_rickmorty.fragments.EpisodeDetailsFragment
import com.example.aston_courseproject_rickmorty.fragments.dialogs.Filter
import com.example.aston_courseproject_rickmorty.model.database.ItemsDatabase
import com.example.aston_courseproject_rickmorty.model.dto.EpisodeForListDto
import com.example.aston_courseproject_rickmorty.repository.EpisodeRepository
import com.example.aston_courseproject_rickmorty.retrofit.Common
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class EpisodeViewModel(
    val mainViewModel: MainViewModel,
    val database: ItemsDatabase,
    filterList: MutableList<Filter>
) : ViewModel() {

    var retrofitServices: RetrofitServices = Common.retrofitService
    private val repository = EpisodeRepository(retrofitServices, database)
    private var dataSource = repository.getEpisodesFromMediator(
            filterList[0].stringToFilter,
            filterList[1].stringToFilter
        )
    val episodeCodeFilter = MutableLiveData<Filter>()

    val episodes: Flow<PagingData<EpisodeForListDto>> by lazy {
        dataSource.cachedIn(viewModelScope)
    }

    fun openFragment(episode: EpisodeForListDto?) {
        val fragment: Fragment = EpisodeDetailsFragment.newInstance(episode?.id!!)
        mainViewModel.changeCurrentDetailsFragment(fragment)
    }

    fun onApplyClick(dialog: Dialog) {
        val checkEpisodeCode = dialog.findViewById<CheckBox>(R.id.checkBoxEpisodeCode)
        val editEpisodeCode = dialog.findViewById<EditText>(R.id.editTextEpisodeCode)
        episodeCodeFilter.value = Filter(checkEpisodeCode.isChecked, editEpisodeCode.text.toString())

        dialog.dismiss()
    }
}