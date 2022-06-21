package com.example.aston_courseproject_rickmorty.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [CharacterDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CharacterDetailsFragment : Fragment(), EpisodeRecyclerAdapter.EpisodeViewHolder.ItemClickListener, LocationRecyclerAdapter.LocationViewHolder.ItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: Int? = null

    private lateinit var viewModel: CharacterDetailsViewModel
    lateinit var listForRecycler: MutableList<Episode>
    lateinit var listForRecyclerOrigin: MutableList<Location>
    lateinit var listForRecyclerLocation: MutableList<Location>
    lateinit var recyclerEpisodesList: RecyclerView
    lateinit var recyclerLocation: RecyclerView
    lateinit var recyclerOrigin: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
        }

        viewModel = ViewModelProvider(this, CharacterDetailsViewModelFactory(param1!!))[CharacterDetailsViewModel::class.java]

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
            val locationDiffUtilCallback = LocationDiffUtilCallback(emptyList(), listForRecyclerLocation)
            val locationDiffResult = DiffUtil.calculateDiff(locationDiffUtilCallback)
            recyclerLocation.adapter?.let { locationDiffResult.dispatchUpdatesTo(it) }
        }
        viewModel.currentOrigin.observe(viewLifecycleOwner) {
            listForRecyclerOrigin.clear()
            listForRecyclerOrigin.add(it)
            val originDiffUtilCallback = LocationDiffUtilCallback(emptyList(), listForRecyclerOrigin)
            val originDiffResult = DiffUtil.calculateDiff(originDiffUtilCallback)
            recyclerOrigin.adapter?.let { originDiffResult.dispatchUpdatesTo(it) }
        }
        viewModel.episodeList.observe(viewLifecycleOwner) {
            listForRecycler.clear()
            listForRecycler.addAll(it)
            val episodeDiffUtilCallback = EpisodeDiffUtilCallback(emptyList(), listForRecycler)
            val episodeDiffResult = DiffUtil.calculateDiff(episodeDiffUtilCallback)
            recyclerEpisodesList.adapter?.let { episodeDiffResult.dispatchUpdatesTo(it) }
        }
    }

    fun updateView(currentCharacter: Character) {
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

    fun initEpisodesRecyclerView() {
        recyclerEpisodesList = requireView().findViewById(R.id.recycler_episodes)
        recyclerEpisodesList.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerEpisodesList.layoutManager = layoutManager

        val sidePadding = 5
        val topPadding = 5
        recyclerEpisodesList.addItemDecoration(RecyclerDecorator(sidePadding, topPadding))

        listForRecycler = mutableListOf()

        val adapter: EpisodeRecyclerAdapter = EpisodeRecyclerAdapter(
            (activity as AppCompatActivity),
            listForRecycler, this
        )

        recyclerEpisodesList.adapter = adapter
        val episodeDiffUtilCallback = EpisodeDiffUtilCallback(emptyList(), listForRecycler)
        val episodeDiffResult = DiffUtil.calculateDiff(episodeDiffUtilCallback)
        recyclerEpisodesList.adapter?.let { episodeDiffResult.dispatchUpdatesTo(it) }
    }

    fun initOriginRecyclerView() {
        recyclerOrigin= requireView().findViewById(R.id.recycler_origin)
        recyclerOrigin.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerOrigin.layoutManager = layoutManager

        val sidePadding = 5
        val topPadding = 5
        recyclerOrigin.addItemDecoration(RecyclerDecorator(sidePadding, topPadding))

        listForRecyclerOrigin = mutableListOf()

        val adapterOrigin: LocationRecyclerAdapter = LocationRecyclerAdapter(
            (activity as AppCompatActivity),
            listForRecyclerOrigin, this
        )

        recyclerOrigin.adapter = adapterOrigin
        val originDiffUtilCallback = LocationDiffUtilCallback(emptyList(), listForRecyclerOrigin)
        val originDiffResult = DiffUtil.calculateDiff(originDiffUtilCallback)
        recyclerOrigin.adapter?.let { originDiffResult.dispatchUpdatesTo(it) }
    }

    fun initLocationRecyclerView() {
        recyclerLocation= requireView().findViewById(R.id.recycler_location)
        recyclerLocation.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerLocation.layoutManager = layoutManager

        val sidePadding = 5
        val topPadding = 5
        recyclerLocation.addItemDecoration(RecyclerDecorator(sidePadding, topPadding))

        listForRecyclerLocation = mutableListOf()

        val adapterOrigin: LocationRecyclerAdapter = LocationRecyclerAdapter(
            (activity as AppCompatActivity),
            listForRecyclerLocation, this
        )

        recyclerLocation.adapter = adapterOrigin
        val locationDiffUtilCallback = LocationDiffUtilCallback(emptyList(), listForRecyclerLocation)
        val locationDiffResult = DiffUtil.calculateDiff(locationDiffUtilCallback)
        recyclerLocation.adapter?.let { locationDiffResult.dispatchUpdatesTo(it) }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CharacterDetailsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Int) =
            CharacterDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
    }

    override fun onItemClick(episode: Episode) {
        val fragment: Fragment = EpisodeDetailsFragment.newInstance(episode.id!!)

        val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()

        requireActivity().supportFragmentManager.findFragmentByTag("current_main_fragment")
            ?.let { transaction.hide(it) }
        transaction.replace(R.id.fragmentContainerView, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onItemClick(location: Location) {
        val fragment: Fragment = LocationDetailsFragment.newInstance(location.id!!)

        val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()

        requireActivity().supportFragmentManager.findFragmentByTag("current_main_fragment")
            ?.let { transaction.hide(it) }
        transaction.replace(R.id.fragmentContainerView, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}