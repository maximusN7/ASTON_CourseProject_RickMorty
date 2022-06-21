package com.example.aston_courseproject_rickmorty.recycler_view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aston_courseproject_rickmorty.R
import com.example.aston_courseproject_rickmorty.model.Episode

class EpisodeRecyclerAdapter(private val context: Context, private val locationList: MutableList<Episode>, val itemClickListener: EpisodeViewHolder.ItemClickListener) :
    RecyclerView.Adapter<EpisodeRecyclerAdapter.EpisodeViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.episode_cell, parent, false)
        return EpisodeViewHolder(itemView, itemClickListener)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val listItem = locationList[position]
        holder.bind(listItem)

        with (holder) {
            txtViewName.text = locationList[position].name
            txtViewEpisode.text = locationList[position].episode
            txtViewAirDate.text = locationList[position].air_date
        }
    }

    override fun getItemCount() = locationList.size


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
}