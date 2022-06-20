package com.example.aston_courseproject_rickmorty.recycler_view


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.aston_courseproject_rickmorty.R
import com.example.aston_courseproject_rickmorty.fragments.CharacterDetailsFragment
import com.example.aston_courseproject_rickmorty.model.Character
import com.squareup.picasso.Picasso


class CharacterRecyclerAdapter(private val context: Context, private val characterList: MutableList<Character>, val itemClickListener: CharacterViewHolder.ItemClickListener) :
    RecyclerView.Adapter<CharacterRecyclerAdapter.CharacterViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.character_cell, parent, false)
        return CharacterViewHolder(itemView, itemClickListener)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val listItem = characterList[position]
        holder.bind(listItem)

        Picasso.get().load(characterList[position].image).into(holder.imageView)
        with (holder) {
            txtViewName.text = characterList[position].name
            txtViewSpecies.text = characterList[position].species
            txtViewStatus.text = characterList[position].status
            txtViewGender.text = characterList[position].gender
        }
    }

    override fun getItemCount() = characterList.size


    class CharacterViewHolder(itemView: View, val itemClickListener: ItemClickListener): RecyclerView.ViewHolder(itemView) {
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


}