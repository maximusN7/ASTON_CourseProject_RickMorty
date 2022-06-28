package com.example.aston_courseproject_rickmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.aston_courseproject_rickmorty.model.Character
import com.example.aston_courseproject_rickmorty.model.CharacterForList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

abstract class BaseCharacterViewModel : ViewModel() {
    abstract val dataSource: Flow<PagingData<CharacterForList>>

    val characters: Flow<PagingData<CharacterForList>> by lazy {
        dataSource.cachedIn(viewModelScope)
    }
}