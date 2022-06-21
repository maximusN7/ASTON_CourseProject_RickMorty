package com.example.aston_courseproject_rickmorty.recycler_view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aston_courseproject_rickmorty.R
import com.example.aston_courseproject_rickmorty.model.Location

class LocationRecyclerAdapter(private val context: Context, private val locationList: MutableList<Location>, val itemClickListener: LocationViewHolder.ItemClickListener) :
    RecyclerView.Adapter<LocationRecyclerAdapter.LocationViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.location_cell, parent, false)
        return LocationViewHolder(itemView, itemClickListener)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val listItem = locationList[position]
        holder.bind(listItem)

        with (holder) {
            txtViewName.text = locationList[position].name
            txtViewType.text = locationList[position].type
            txtViewDimension.text = locationList[position].dimension
        }
    }

    override fun getItemCount() = locationList.size


    class LocationViewHolder(itemView: View, val itemClickListener: ItemClickListener): RecyclerView.ViewHolder(itemView) {
        val txtViewName: TextView = itemView.findViewById(R.id.textView_name)
        val txtViewType: TextView = itemView.findViewById(R.id.textView_type)
        val txtViewDimension: TextView = itemView.findViewById(R.id.textView_dimension)

        fun bind(listItem: Location) {
            itemView.setOnClickListener {
                itemClickListener.onItemClick(listItem)
            }
        }

        interface ItemClickListener {

            fun onItemClick(location: Location)
        }
    }

}