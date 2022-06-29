package com.example.aston_courseproject_rickmorty.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.aston_courseproject_rickmorty.R
import com.example.aston_courseproject_rickmorty.model.Character
import com.example.aston_courseproject_rickmorty.model.Episode
import com.example.aston_courseproject_rickmorty.model.Location
import com.example.aston_courseproject_rickmorty.model.LocationForList
import com.example.aston_courseproject_rickmorty.recycler_view.EpisodeRecyclerAdapter
import com.example.aston_courseproject_rickmorty.recycler_view.LocationRecyclerAdapter
import com.example.aston_courseproject_rickmorty.retrofit.Status
import com.example.aston_courseproject_rickmorty.utils.EpisodeDiffUtilCallback
import com.example.aston_courseproject_rickmorty.utils.LocationDiffUtilCallback
import com.example.aston_courseproject_rickmorty.utils.RecyclerDecorator
import com.example.aston_courseproject_rickmorty.viewmodel.CharacterDetailsViewModel
import com.example.aston_courseproject_rickmorty.viewmodel.factory.CharacterDetailsViewModelFactory
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.coroutines.launch
import java.lang.Exception


private const val ARG_CHARACTER_ID = "characterId"

/**
 * A simple [Fragment] subclass.
 * Use the [CharacterDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@ExperimentalPagingApi
class CharacterDetailsFragment : Fragment(),
    EpisodeRecyclerAdapter.EpisodeViewHolder.ItemClickListener,
    LocationRecyclerAdapter.LocationViewHolder.ItemClickListener {
    private var characterId: Int? = null

    private lateinit var viewModel: CharacterDetailsViewModel
    private var listForRecycler: MutableList<Episode> = mutableListOf()
    private var listForRecyclerOrigin: MutableList<LocationForList> = mutableListOf(LocationForList())
    private var listForRecyclerLocation: MutableList<LocationForList> = mutableListOf(LocationForList())
    private lateinit var recyclerEpisodesList: RecyclerView
    private lateinit var recyclerLocation: RecyclerView
    private lateinit var recyclerOrigin: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            characterId = it.getInt(ARG_CHARACTER_ID)
        }
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

        initEpisodesRecyclerView()
        initOriginRecyclerView()
        initLocationRecyclerView()

        initView()

        /*viewModel.currentCharacter.observe(viewLifecycleOwner) {
            detailsLayout.visibility = View.VISIBLE
            pbView.visibility = View.GONE
            if (it.origin?.name == "unknown") {
                listForRecyclerOrigin.clear()
                listForRecyclerOrigin.add(Location(-1, "unknown", "", "", emptyArray(), "", ""))
            }
            updateView(it)
        }
        viewModel.currentLocation.observe(viewLifecycleOwner) {
            listForRecyclerLocation.clear()
            listForRecyclerLocation.add(it)
            notifyWithDiffUtil(recyclerLocation)
        }
        viewModel.currentOrigin.observe(viewLifecycleOwner) {
            if (listForRecyclerOrigin.size == 0) {
                listForRecyclerOrigin.clear()
                listForRecyclerOrigin.add(it)
                notifyWithDiffUtil(recyclerOrigin)
            }
        }
        viewModel.episodeList.observe(viewLifecycleOwner) {
            listForRecycler.clear()
            listForRecycler.addAll(it)
            notifyWithDiffUtil(recyclerEpisodesList)
        }*/

        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {

            this.viewModelStore.clear()

            initView()

            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun initView() {
        val appContext = activity?.applicationContext
        viewModel = ViewModelProvider(
            this,
            CharacterDetailsViewModelFactory(
                characterId!!,
                appContext!!,
                requireContext(),
                requireActivity()
            )
        )[CharacterDetailsViewModel::class.java]

        val detailsLayout = view?.findViewById<ConstraintLayout>(R.id.character_detailsLayout)
        detailsLayout?.visibility = View.INVISIBLE
        val pbView = view?.findViewById<ProgressBar>(R.id.progress)
        pbView?.visibility = View.VISIBLE
        val pbViewRecycler = view?.findViewById<ProgressBar>(R.id.progressRecycler)
        pbViewRecycler?.visibility = View.VISIBLE

        lifecycleScope.launch {
            viewModel.character.collect {

                when (it.status) {
                    Status.LOADING -> {
                        detailsLayout?.visibility = View.INVISIBLE
                        pbView?.visibility = View.VISIBLE
                    }
                    Status.SUCCESS -> {
                        detailsLayout?.visibility = View.VISIBLE
                        pbView?.visibility = View.GONE
                        it.data?.let { character ->
                            updateView(character)
                        }
                    }
                    Status.ERROR -> {
                        pbView?.visibility = View.GONE
                        Log.e("AAA", "${it.message}")
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.origin.collect {

                when (it.status) {
                    Status.LOADING -> {
                        detailsLayout?.visibility = View.INVISIBLE
                        pbView?.visibility = View.VISIBLE
                    }
                    Status.SUCCESS -> {
                        detailsLayout?.visibility = View.VISIBLE
                        pbView?.visibility = View.GONE
                        it.data?.let { origin ->
                            val oldList = listForRecyclerOrigin.map { loc -> loc.copy() }
                            listForRecyclerOrigin.clear()
                            listForRecyclerOrigin.addAll(mutableListOf(origin))
                            notifyOriginWithDiffUtil(oldList.toMutableList())
                        }
                    }
                    Status.ERROR -> {
                        pbView?.visibility = View.GONE
                        Log.e("AAAList", "${it.message}")
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.location.collect {

                when (it.status) {
                    Status.LOADING -> {
                        detailsLayout?.visibility = View.INVISIBLE
                        pbView?.visibility = View.VISIBLE
                    }
                    Status.SUCCESS -> {
                        detailsLayout?.visibility = View.VISIBLE
                        pbView?.visibility = View.GONE
                        it.data?.let { location ->
                            val oldList = listForRecyclerLocation.map { loc -> loc.copy() }
                            listForRecyclerLocation.clear()
                            listForRecyclerLocation.addAll(mutableListOf(location))
                            notifyLocationWithDiffUtil(oldList.toMutableList())
                        }
                    }
                    Status.ERROR -> {
                        pbView?.visibility = View.GONE
                        Log.e("AAAList", "${it.message}")
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.episodes.collect {

                when (it.status) {
                    Status.LOADING -> {
                        pbViewRecycler?.visibility = View.VISIBLE
                    }
                    Status.SUCCESS -> {
                        pbViewRecycler?.visibility = View.GONE
                        it.data?.let { episodes ->
                            val oldList = listForRecycler.map { episode -> episode.copy() }
                            listForRecycler.clear()
                            listForRecycler.addAll(episodes)
                            notifyEpisodesWithDiffUtil(oldList.toMutableList())
                        }
                    }
                    Status.ERROR -> {
                        pbView?.visibility = View.GONE
                        Log.e("AAAList", "${it.message}")
                    }
                }
            }
        }
    }

    private fun updateView(currentCharacter: Character) {
        val imageView = view?.findViewById<ImageView>(R.id.imageView_avatar)
        val textViewName = view?.findViewById<TextView>(R.id.textView_name)
        val textViewStatus = view?.findViewById<TextView>(R.id.textView_status)
        val textViewSpecies = view?.findViewById<TextView>(R.id.textView_species)
        val textViewType = view?.findViewById<TextView>(R.id.textView_type)
        val textViewGender = view?.findViewById<TextView>(R.id.textView_gender)
        val imageProgressBar = view?.findViewById<ProgressBar>(R.id.image_progressbar)

        Picasso.get()
            .load(currentCharacter.image)
            .transform(CropCircleTransformation())
            .into(imageView, object : Callback {
                override fun onSuccess() {
                    imageProgressBar?.visibility = View.GONE
                }

                override fun onError(e: Exception?) {
                }
            })
        textViewName?.text = currentCharacter.name
        textViewStatus?.text = currentCharacter.status
        textViewSpecies?.text = currentCharacter.species
        textViewType?.text = currentCharacter.type
        textViewGender?.text = currentCharacter.gender
    }

    private fun initEpisodesRecyclerView() {
        val sidePadding = 5
        val topPadding = 5
        val mAdapter = EpisodeRecyclerAdapter(
            listForRecycler, this
        )
        recyclerEpisodesList = requireView().findViewById(R.id.recycler_episodes)
        recyclerEpisodesList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(RecyclerDecorator(sidePadding, topPadding))
            adapter = mAdapter
        }
    }

    private fun initOriginRecyclerView() {
        val sidePadding = 5
        val topPadding = 5
        val mAdapter = LocationRecyclerAdapter(
            listForRecyclerOrigin, this
        )
        recyclerOrigin = requireView().findViewById(R.id.recycler_origin)
        recyclerOrigin.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(RecyclerDecorator(sidePadding, topPadding))
            adapter = mAdapter
        }
    }

    private fun initLocationRecyclerView() {
        val sidePadding = 5
        val topPadding = 5
        val mAdapter = LocationRecyclerAdapter(
            listForRecyclerLocation, this
        )
        recyclerLocation = requireView().findViewById(R.id.recycler_location)
        recyclerLocation.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(RecyclerDecorator(sidePadding, topPadding))
            adapter = mAdapter
        }
    }

    private fun notifyEpisodesWithDiffUtil(oldEpisodes: MutableList<Episode>) {
        val episodeDiffUtilCallback = EpisodeDiffUtilCallback(oldEpisodes, listForRecycler)
        val episodeDiffResult = DiffUtil.calculateDiff(episodeDiffUtilCallback)
        recyclerEpisodesList.adapter?.let { episodeDiffResult.dispatchUpdatesTo(it) }
    }
    private fun notifyLocationWithDiffUtil(oldLocations: MutableList<LocationForList>) {
        val locationDiffUtilCallback = LocationDiffUtilCallback(oldLocations, listForRecyclerLocation)
        val locationDiffResult = DiffUtil.calculateDiff(locationDiffUtilCallback)
        recyclerLocation.adapter?.let { locationDiffResult.dispatchUpdatesTo(it) }
    }
    private fun notifyOriginWithDiffUtil(oldLocations: MutableList<LocationForList>) {
        val originDiffUtilCallback = LocationDiffUtilCallback(oldLocations, listForRecyclerOrigin)
        val originDiffResult = DiffUtil.calculateDiff(originDiffUtilCallback)
        recyclerOrigin.adapter?.let { originDiffResult.dispatchUpdatesTo(it) }
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

    override fun onItemClick(episode: Episode?) {
        viewModel.openFragment(episode)
    }

    override fun onItemClick(location: LocationForList?) {
        viewModel.openFragment(location)
    }
}