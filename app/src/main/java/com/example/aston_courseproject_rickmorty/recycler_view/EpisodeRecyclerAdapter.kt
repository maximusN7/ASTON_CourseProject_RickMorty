package com.example.aston_courseproject_rickmorty.recycler_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aston_courseproject_rickmorty.R
import com.example.aston_courseproject_rickmorty.model.Episode

class EpisodeRecyclerAdapter(private val episodeList: MutableList<Episode>, val itemClickListener: EpisodeViewHolder.ItemClickListener) :
    RecyclerView.Adapter<EpisodeRecyclerAdapter.EpisodeViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.episode_cell, parent, false)
        return EpisodeViewHolder(itemView, itemClickListener)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val listItem = episodeList[position]
        holder.bind(listItem)

        with (holder) {
            txtViewName.text = listItem.name ?: ""
            txtViewEpisode.text = listItem.episode ?: ""
            txtViewAirDate.text = listItem.air_date ?: ""
        }
        if (holder.txtViewName.text == "") {
            holder.cellProgressBar.visibility = View.VISIBLE
        } else {
            holder.cellProgressBar.visibility = View.GONE
        }
    }

    override fun getItemCount() = episodeList.size


    class EpisodeViewHolder(itemView: View, val itemClickListener: ItemClickListener): RecyclerView.ViewHolder(itemView) {
        val txtViewName: TextView = itemView.findViewById(R.id.textView_name)
        val txtViewEpisode: TextView = itemView.findViewById(R.id.textView_episode)
        val txtViewAirDate: TextView = itemView.findViewById(R.id.textView_airdate)
        val cellProgressBar: ProgressBar = itemView.findViewById(R.id.cell_progressbar)

        fun bind(listItem: Episode?) {
            itemView.setOnClickListener {
                itemClickListener.onItemClick(listItem)
            }
        }

        interface ItemClickListener {

            fun onItemClick(episode: Episode?)
        }
    }
}