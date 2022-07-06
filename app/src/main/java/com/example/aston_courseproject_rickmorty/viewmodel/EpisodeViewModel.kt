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
import com.example.aston_courseproject_rickmorty.model.dto.CharacterForListDto
import com.example.aston_courseproject_rickmorty.model.dto.EpisodeForListDto
import com.example.aston_courseproject_rickmorty.repository.EpisodeRepository
import com.example.aston_courseproject_rickmorty.retrofit.Common
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class EpisodeViewModel(
    private val dataSource: Flow<PagingData<EpisodeForListDto>>
) : ViewModel() {

    val episodeCodeFilter = MutableLiveData<Filter>()

    val episodes: Flow<PagingData<EpisodeForListDto>> by lazy {
        dataSource.cachedIn(viewModelScope)
    }

    fun onApplyClick(dialog: Dialog) {
        val checkEpisodeCode = dialog.findViewById<CheckBox>(R.id.checkBoxEpisodeCode)
        val editEpisodeCode = dialog.findViewById<EditText>(R.id.editTextEpisodeCode)
        episodeCodeFilter.value = Filter(checkEpisodeCode.isChecked, editEpisodeCode.text.toString())

        dialog.dismiss()
    }
}