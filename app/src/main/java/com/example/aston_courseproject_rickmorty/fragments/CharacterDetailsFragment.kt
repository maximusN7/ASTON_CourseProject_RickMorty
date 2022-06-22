package com.example.aston_courseproject_rickmorty.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aston_courseproject_rickmorty.MainViewModel
import com.example.aston_courseproject_rickmorty.MainViewModelFactory
import com.example.aston_courseproject_rickmorty.R
import com.example.aston_courseproject_rickmorty.model.Character
import com.example.aston_courseproject_rickmorty.model.Episode
import com.example.aston_courseproject_rickmorty.model.Location
import com.example.aston_courseproject_rickmorty.recycler_view.EpisodeRecyclerAdapter
import com.example.aston_courseproject_rickmorty.recycler_view.LocationRecyclerAdapter
import com.example.aston_courseproject_rickmorty.utils.EpisodeDiffUtilCallback
import com.example.aston_courseproject_rickmorty.utils.LocationDiffUtilCallback
import com.example.aston_courseproject_rickmorty.utils.RecyclerDecorator
import com.example.aston_courseproject_rickmorty.viewmodel.CharacterDetailsViewModel
import com.example.aston_courseproject_rickmorty.viewmodel.CharacterDetailsViewModelFactory
import com.squareup.picasso.Picasso


private const val ARG_CHARACTER_ID = "characterId"

/**
 * A simple [Fragment] subclass.
 * Use the [CharacterDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CharacterDetailsFragment : Fragment(), EpisodeRecyclerAdapter.EpisodeViewHolder.ItemClickListener, LocationRecyclerAdapter.LocationViewHolder.ItemClickListener {
    private var characterId: Int? = null

    private lateinit var viewModel: CharacterDetailsViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var listForRecycler: MutableList<Episode>
    private lateinit var listForRecyclerOrigin: MutableList<Location>
    private lateinit var listForRecyclerLocation: MutableList<Location>
    private lateinit var recyclerEpisodesList: RecyclerView
    private lateinit var recyclerLocation: RecyclerView
    private lateinit var recyclerOrigin: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            characterId = it.getInt(ARG_CHARACTER_ID)
        }

        viewModel = ViewModelProvider(this, CharacterDetailsViewModelFactory(characterId!!))[CharacterDetailsViewModel::class.java]

        mainViewModel = ViewModelProvider(requireActivity(), MainViewModelFactory(requireContext()))[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_character_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentCharacter.observe(viewLifecycleOwner) {
            updateView(it)
        }
        viewModel.currentLocation.observe(viewLifecycleOwner) {
            listForRecyclerLocation.clear()
            listForRecyclerLocation.add(it)
            notifyWithDiffUtil(recyclerLocation)
        }
        viewModel.currentOrigin.observe(viewLifecycleOwner) {
            listForRecyclerOrigin.clear()
            listForRecyclerOrigin.add(it)
            notifyWithDiffUtil(recyclerOrigin)
        }
        viewModel.episodeList.observe(viewLifecycleOwner) {
            listForRecycler.clear()
            listForRecycler.addAll(it)
            notifyWithDiffUtil(recyclerEpisodesList)
        }
    }

    private fun updateView(currentCharacter: Character) {
        val imageView = view?.findViewById<ImageView>(R.id.imageView_avatar)
        val textViewName = view?.findViewById<TextView>(R.id.textView_name)
        val textViewStatus = view?.findViewById<TextView>(R.id.textView_status)
        val textViewSpecies = view?.findViewById<TextView>(R.id.textView_species)
        val textViewType = view?.findViewById<TextView>(R.id.textView_type)
        val textViewGender = view?.findViewById<TextView>(R.id.textView_gender)

        Picasso.get().load(currentCharacter.image).into(imageView)
        textViewName?.text = currentCharacter.name
        textViewStatus?.text = currentCharacter.status
        textViewSpecies?.text = currentCharacter.species
        textViewType?.text = currentCharacter.type
        textViewGender?.text = currentCharacter.gender

        initEpisodesRecyclerView()
        initOriginRecyclerView()
        initLocationRecyclerView()
    }

    private fun initEpisodesRecyclerView() {
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        val sidePadding = 5
        val topPadding = 5
        val mAdapter = EpisodeRecyclerAdapter(
            (activity as AppCompatActivity),
            listForRecycler, this
        )
        recyclerEpisodesList = requireView().findViewById(R.id.recycler_episodes)
        recyclerEpisodesList.apply {
            setHasFixedSize(true)
            layoutManager = mLayoutManager
            addItemDecoration(RecyclerDecorator(sidePadding, topPadding))
            adapter = mAdapter
        }

        notifyWithDiffUtil(recyclerEpisodesList)
    }

    private fun initOriginRecyclerView() {
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        val sidePadding = 5
        val topPadding = 5
        val mAdapter = LocationRecyclerAdapter(
            (activity as AppCompatActivity),
            listForRecyclerOrigin, this
        )
        recyclerOrigin= requireView().findViewById(R.id.recycler_origin)
        recyclerOrigin.apply {
            setHasFixedSize(true)
            layoutManager = mLayoutManager
            addItemDecoration(RecyclerDecorator(sidePadding, topPadding))
            adapter = mAdapter
        }

        notifyWithDiffUtil(recyclerOrigin)
    }

    private fun initLocationRecyclerView() {
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        val sidePadding = 5
        val topPadding = 5
        val mAdapter = LocationRecyclerAdapter(
            (activity as AppCompatActivity),
            listForRecyclerLocation, this
        )
        recyclerLocation = requireView().findViewById(R.id.recycler_location)
        recyclerLocation.apply {
            setHasFixedSize(true)
            layoutManager = mLayoutManager
            addItemDecoration(RecyclerDecorator(sidePadding, topPadding))
            adapter = mAdapter
        }

        notifyWithDiffUtil(recyclerLocation)
    }

    private fun notifyWithDiffUtil(recyclerView: RecyclerView) {
        when (recyclerView) {
            recyclerLocation -> {
                val locationDiffUtilCallback = LocationDiffUtilCallback(emptyList(), listForRecyclerLocation)
                val locationDiffResult = DiffUtil.calculateDiff(locationDiffUtilCallback)
                recyclerView.adapter?.let { locationDiffResult.dispatchUpdatesTo(it) }
            }
            recyclerOrigin -> {
                val originDiffUtilCallback = LocationDiffUtilCallback(emptyList(), listForRecyclerOrigin)
                val originDiffResult = DiffUtil.calculateDiff(originDiffUtilCallback)
                recyclerOrigin.adapter?.let { originDiffResult.dispatchUpdatesTo(it) }
            }
            recyclerEpisodesList -> {
                val episodeDiffUtilCallback = EpisodeDiffUtilCallback(emptyList(), listForRecycler)
                val episodeDiffResult = DiffUtil.calculateDiff(episodeDiffUtilCallback)
                recyclerEpisodesList.adapter?.let { episodeDiffResult.dispatchUpdatesTo(it) }
            }
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param characterId Parameter 1.
         * @return A new instance of fragment CharacterDetailsFragment.
         */
        @JvmStatic
        fun newInstance(characterId: Int) =
            CharacterDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_CHARACTER_ID, characterId)
                }
            }
    }

    override fun onItemClick(episode: Episode) {
        val fragment: Fragment = EpisodeDetailsFragment.newInstance(episode.id)
        mainViewModel.changeCurrentDetailsFragment(fragment)
    }

    override fun onItemClick(location: Location) {
        val fragment: Fragment = LocationDetailsFragment.newInstance(location.id)
        mainViewModel.changeCurrentDetailsFragment(fragment)
    }
}