package com.example.aston_courseproject_rickmorty.viewmodel

import android.app.Dialog
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.aston_courseproject_rickmorty.MainViewModel
import com.example.aston_courseproject_rickmorty.R
import com.example.aston_courseproject_rickmorty.fragments.CharacterDetailsFragment
import com.example.aston_courseproject_rickmorty.fragments.dialogs.CharacterFilterDialog
import com.example.aston_courseproject_rickmorty.fragments.dialogs.Filter
import com.example.aston_courseproject_rickmorty.model.database.ItemsDatabase
import com.example.aston_courseproject_rickmorty.model.dto.CharacterForListDto
import com.example.aston_courseproject_rickmorty.repository.CharacterRepository
import com.example.aston_courseproject_rickmorty.retrofit.Common
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class CharacterViewModel(
    val mainViewModel: MainViewModel,
    private val dialogProcessor: CharacterFilterDialog,
    val database: ItemsDatabase,
    private val filterList: MutableList<Filter>
) : ViewModel() {

    var retrofitServices: RetrofitServices = Common.retrofitService
    private val repository = CharacterRepository(retrofitServices, database)
    private val dataSource = repository.getCharactersFromMediator(
        filterList[0].stringToFilter,
        filterList[1].stringToFilter,
        filterList[2].stringToFilter,
        filterList[3].stringToFilter,
        filterList[4].stringToFilter
    )
    val statusFilter = MutableLiveData<Filter>()
    val speciesFilter = MutableLiveData<Filter>()
    val typeFilter = MutableLiveData<Filter>()
    val genderFilter = MutableLiveData<Filter>()

    val characters: Flow<PagingData<CharacterForListDto>> by lazy {
        dataSource.cachedIn(viewModelScope)
    }

    fun openFragment(character: CharacterForListDto?) {
        val fragment: Fragment = CharacterDetailsFragment.newInstance(character?.id!!)
        mainViewModel.changeCurrentDetailsFragment(fragment)
    }

    fun openFilterDialog() {
        dialogProcessor.showDialog(filterList[1], filterList[2], filterList[3], filterList[4])
    }

    fun onApplyClick(dialog: Dialog) {
        val checkSpecies = dialog.findViewById<CheckBox>(R.id.checkBoxSpecies)
        val checkType = dialog.findViewById<CheckBox>(R.id.checkBoxType)
        val checkStatus = dialog.findViewById<CheckBox>(R.id.checkBoxStatus)
        val checkGender = dialog.findViewById<CheckBox>(R.id.checkBoxGender)

        val editSpecies = dialog.findViewById<EditText>(R.id.editTextSpecies)
        val editType = dialog.findViewById<EditText>(R.id.editTextType)
        val radioGroupStatus = dialog.findViewById<RadioGroup>(R.id.radioGroupStatus)
        val radioGroupGender = dialog.findViewById<RadioGroup>(R.id.radioGroupGender)

        when(val checkedRadioButtonId = radioGroupStatus.checkedRadioButtonId) {
            -1 -> {
                statusFilter.value = Filter(checkStatus.isChecked, "")
            }
            else -> {
                val selectedRadioButton = dialog.findViewById<RadioButton>(checkedRadioButtonId)
                statusFilter.value = Filter(checkStatus.isChecked, selectedRadioButton.text.toString())
            }
        }
        when(val checkedRadioButtonId = radioGroupGender.checkedRadioButtonId) {
            -1 -> {
                genderFilter.value = Filter(checkGender.isChecked, "")
            }
            else -> {
                val selectedRadioButton = dialog.findViewById<RadioButton>(checkedRadioButtonId)
                genderFilter.value = Filter(checkGender.isChecked, selectedRadioButton.text.toString())
            }
        }
        speciesFilter.value = Filter(checkSpecies.isChecked, editSpecies.text.toString())
        typeFilter.value = Filter(checkType.isChecked, editType.text.toString())

        dialog.dismiss()
    }
}