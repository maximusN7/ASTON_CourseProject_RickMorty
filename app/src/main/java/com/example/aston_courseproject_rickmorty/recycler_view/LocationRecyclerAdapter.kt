package com.example.aston_courseproject_rickmorty.recycler_view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aston_courseproject_rickmorty.R
import com.example.aston_courseproject_rickmorty.model.Location

class LocationRecyclerAdapter(private val locationList: MutableList<Location>, val itemClickListener: LocationViewHolder.ItemClickListener) :
    RecyclerView.Adapter<LocationRecyclerAdapter.LocationViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.cell_location, parent, false)
        return LocationViewHolder(itemView, itemClickListener)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val listItem = locationList[position]
        holder.bind(listItem)

        with (holder) {
            txtViewName.text = listItem.name ?: ""
            txtViewType.text = listItem.type ?: ""
            txtViewDimension.text = listItem.dimension ?: ""
        }

        if (holder.txtViewName.text == "") {
            holder.cellProgressBar.visibility = View.VISIBLE
        } else {
            holder.cellProgressBar.visibility = View.GONE
        }
    }

    override fun getItemCount() = locationList.size


    class LocationViewHolder(itemView: View, val itemClickListener: ItemClickListener): RecyclerView.ViewHolder(itemView) {
        val txtViewName: TextView = itemView.findViewById(R.id.textView_name)
        val txtViewType: TextView = itemView.findViewById(R.id.textView_type)
        val txtViewDimension: TextView = itemView.findViewById(R.id.textView_dimension)
        val cellProgressBar: ProgressBar = itemView.findViewById(R.id.cell_progressbar)

        fun bind(listItem: Location?) {
            itemView.setOnClickListener {
                itemClickListener.onItemClick(listItem)
            }
        }

        interface ItemClickListener {

            fun onItemClick(location: Location?)
        }
    }

}