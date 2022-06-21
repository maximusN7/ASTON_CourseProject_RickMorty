package com.example.aston_courseproject_rickmorty.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.aston_courseproject_rickmorty.model.Episode

class EpisodeDiffUtilCallback(
    private val oldList: List<Episode>,
    private val newList: MutableList<Episode>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEpisode: Episode = oldList[oldItemPosition]
        val newEpisode: Episode = newList[newItemPosition]
        return oldEpisode.id == newEpisode.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEpisode: Episode = oldList[oldItemPosition]
        val newEpisode: Episode = newList[newItemPosition]

        return (oldEpisode.name == newEpisode.name
                && oldEpisode.episode == newEpisode.episode
                && oldEpisode.air_date == newEpisode.air_date)
    }
}