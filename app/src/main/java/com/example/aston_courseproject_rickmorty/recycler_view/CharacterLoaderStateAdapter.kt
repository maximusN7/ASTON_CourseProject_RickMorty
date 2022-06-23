package com.example.aston_courseproject_rickmorty.recycler_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aston_courseproject_rickmorty.R

class CharacterLoaderStateAdapter : LoadStateAdapter<CharacterLoaderStateAdapter.ItemViewHolder>() {

    override fun getStateViewType(loadState: LoadState) = when (loadState) {
        is LoadState.NotLoading -> error("Not supported")
        LoadState.Loading -> PROGRESS
        is LoadState.Error -> ERROR
    }

    override fun onBindViewHolder(holder: ItemViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ItemViewHolder {
        return when(loadState) {
            LoadState.Loading -> LoaderViewHolderGood.getInstance(parent)
            is LoadState.Error -> LoaderViewHolderBad.getInstance(parent)
            is LoadState.NotLoading -> error("Not supported")
        }
    }

    private companion object {
        private const val ERROR = 1
        private const val PROGRESS = 0
    }

    class LoaderViewHolderGood(view: View) : ItemViewHolder(view) {

        companion object {
            fun getInstance(parent: ViewGroup): LoaderViewHolderGood {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.item_character_loader, parent, false)
                return LoaderViewHolderGood(view)
            }
        }

        private val progressLayout: ConstraintLayout = view.findViewById(R.id.mlLoader)

        override fun bind(loadState: LoadState) {
            val pbView = progressLayout.findViewById<ProgressBar>(R.id.pbLoader)
            pbView.visibility = View.VISIBLE
        }
    }

    class LoaderViewHolderBad(view: View) : ItemViewHolder(view) {

        companion object {
            fun getInstance(parent: ViewGroup): LoaderViewHolderBad {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.item_character_loader, parent, false)
                return LoaderViewHolderBad(view)
            }
        }

        private val progressLayout: ConstraintLayout = view.findViewById(R.id.mlLoader)

        override fun bind(loadState: LoadState) {
            val pbView = progressLayout.findViewById<ProgressBar>(R.id.pbLoader)
            pbView.visibility = View.INVISIBLE
        }
    }

    abstract class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        abstract fun bind(loadState: LoadState)
    }
}