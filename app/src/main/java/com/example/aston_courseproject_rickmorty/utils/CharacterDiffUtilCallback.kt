package com.example.aston_courseproject_rickmorty.utils

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.example.aston_courseproject_rickmorty.model.Character

class CharacterDiffUtilCallback(
    private val oldList: List<Character>,
    private val newList: MutableList<Character>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldContact: Character = oldList[oldItemPosition]
        val newContact: Character = newList[newItemPosition]
        return oldContact.id == newContact.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldContact: Character = oldList[oldItemPosition]
        val newContact: Character = newList[newItemPosition]

        return (oldContact.name == newContact.name
                && oldContact.species == newContact.species
                && oldContact.status == newContact.status
                && oldContact.gender == newContact.gender)
    }
}