package com.example.aston_courseproject_rickmorty.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.aston_courseproject_rickmorty.model.Location
import com.example.aston_courseproject_rickmorty.model.LocationForList

class LocationDiffUtilCallback(
    private val oldList: List<LocationForList>,
    private val newList: MutableList<LocationForList>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldLocation: LocationForList = oldList[oldItemPosition]
        val newLocation: LocationForList = newList[newItemPosition]
        return oldLocation.id == newLocation.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldLocation: LocationForList = oldList[oldItemPosition]
        val newLocation: LocationForList = newList[newItemPosition]

        return (oldLocation.name == newLocation.name
                && oldLocation.type == newLocation.type
                && oldLocation.dimension == newLocation.dimension)
    }
}