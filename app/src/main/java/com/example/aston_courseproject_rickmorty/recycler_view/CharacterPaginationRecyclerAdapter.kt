package com.example.aston_courseproject_rickmorty.recycler_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.aston_courseproject_rickmorty.R
import com.example.aston_courseproject_rickmorty.model.Character
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

class CharacterPaginationRecyclerAdapter(private val itemClickListener: CharacterViewHolder.ItemClickListener) :
    PagingDataAdapter<Character, CharacterPaginationRecyclerAdapter.CharacterViewHolder>(
        DiffUtilCallback()
    ) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.character_cell, parent, false)
        return CharacterViewHolder(itemView, itemClickListener)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val listItem = getItem(position)
        holder.bind(listItem!!)

        Picasso.get().load(listItem.image).transform(CropCircleTransformation()).into(holder.imageView)
        with (holder) {
            txtViewName.text = listItem.name
            txtViewSpecies.text = listItem.species
            txtViewStatus.text = listItem.status
            txtViewGender.text = listItem.gender
        }
    }

    class CharacterViewHolder(itemView: View, private val itemClickListener: ItemClickListener): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView_avatar)
        val txtViewName: TextView = itemView.findViewById(R.id.textView_name)
        val txtViewSpecies: TextView = itemView.findViewById(R.id.textView_species)
        val txtViewStatus: TextView = itemView.findViewById(R.id.textView_status)
        val txtViewGender: TextView = itemView.findViewById(R.id.textView_gender)

        fun bind(listItem: Character) {
            itemView.setOnClickListener {
                itemClickListener.onItemClick(listItem)
            }
        }

        interface ItemClickListener {

            fun onItemClick(character: Character)
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return (oldItem.name == newItem.name
                    && oldItem.species == newItem.species
                    && oldItem.status == newItem.status
                    && oldItem.gender == newItem.gender)
        }

    }

}