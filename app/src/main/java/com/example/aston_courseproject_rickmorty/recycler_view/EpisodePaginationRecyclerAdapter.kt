package com.example.aston_courseproject_rickmorty.recycler_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.aston_courseproject_rickmorty.R
import com.example.aston_courseproject_rickmorty.model.Episode

class EpisodePaginationRecyclerAdapter(private val itemClickListener: EpisodeViewHolder.ItemClickListener) :
    PagingDataAdapter<Episode, EpisodePaginationRecyclerAdapter.EpisodeViewHolder>(
        DiffUtilCallback()
    ) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.episode_cell, parent, false)
        return EpisodeViewHolder(itemView, itemClickListener)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val listItem = getItem(position)
        holder.bind(listItem!!)

        with (holder) {
            txtViewName.text = listItem.name
            txtViewEpisode.text = listItem.episode
            txtViewAirDate.text = listItem.air_date
        }
    }

    class EpisodeViewHolder(itemView: View, val itemClickListener: ItemClickListener): RecyclerView.ViewHolder(itemView) {
        val txtViewName: TextView = itemView.findViewById(R.id.textView_name)
        val txtViewEpisode: TextView = itemView.findViewById(R.id.textView_episode)
        val txtViewAirDate: TextView = itemView.findViewById(R.id.textView_airdate)

        fun bind(listItem: Episode) {
            itemView.setOnClickListener {
                itemClickListener.onItemClick(listItem)
            }
        }

        interface ItemClickListener {

            fun onItemClick(episode: Episode)
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<Episode>() {
        override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {
            return (oldItem.name == newItem.name
                    && oldItem.episode == newItem.episode
                    && oldItem.air_date == newItem.air_date)
        }

    }

}