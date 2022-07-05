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
import com.example.aston_courseproject_rickmorty.fragments.LocationDetailsFragment
import com.example.aston_courseproject_rickmorty.fragments.dialogs.Filter
import com.example.aston_courseproject_rickmorty.fragments.dialogs.LocationFilterDialog
import com.example.aston_courseproject_rickmorty.model.*
import com.example.aston_courseproject_rickmorty.model.database.ItemsDatabase
import com.example.aston_courseproject_rickmorty.model.dto.LocationForListDto
import com.example.aston_courseproject_rickmorty.repository.LocationRepository
import com.example.aston_courseproject_rickmorty.retrofit.Common
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class LocationViewModel(
    val mainViewModel: MainViewModel,
    val database: ItemsDatabase,
    filterList: MutableList<Filter>
) : ViewModel() {

    var retrofitServices: RetrofitServices = Common.retrofitService
    private val repository = LocationRepository(retrofitServices, database)
    private val dataSource = repository.getLocationsFromMediator(
        filterList[0].stringToFilter,
        filterList[1].stringToFilter,
        filterList[2].stringToFilter
    )
    val typeFilter = MutableLiveData<Filter>()
    val dimensionFilter = MutableLiveData<Filter>()

    val locations: Flow<PagingData<LocationForListDto>> by lazy {
        dataSource.cachedIn(viewModelScope)
    }

    fun openFragment(location: LocationForListDto?) {
        val fragment: Fragment = LocationDetailsFragment.newInstance(location?.id!!)
        mainViewModel.changeCurrentDetailsFragment(fragment)
    }

    fun onApplyClick(dialog: Dialog) {
        val checkType = dialog.findViewById<CheckBox>(R.id.checkBoxType)
        val checkDimension = dialog.findViewById<CheckBox>(R.id.checkBoxDimension)
        val editType = dialog.findViewById<EditText>(R.id.editTextType)
        val editDimension = dialog.findViewById<EditText>(R.id.editTextDimension)
        typeFilter.value = Filter(checkType.isChecked, editType.text.toString())
        dimensionFilter.value = Filter(checkDimension.isChecked, editDimension.text.toString())

        dialog.dismiss()
    }
}