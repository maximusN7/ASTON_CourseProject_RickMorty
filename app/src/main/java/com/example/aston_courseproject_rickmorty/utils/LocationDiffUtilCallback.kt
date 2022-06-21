package com.example.aston_courseproject_rickmorty.utils

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.example.aston_courseproject_rickmorty.model.Character
import com.example.aston_courseproject_rickmorty.model.Location

class LocationDiffUtilCallback(
    private val oldList: List<Location>,
    private val newList: MutableList<Location>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldContact: Location = oldList[oldItemPosition]
        val newContact: Location = newList[newItemPosition]
        return oldContact.id == newContact.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldContact: Location = oldList[oldItemPosition]
        val newContact: Location = newList[newItemPosition]

        return (oldContact.name == newContact.name
                && oldContact.type == newContact.type
                && oldContact.dimension == newContact.dimension)
    }
}