package com.example.aston_courseproject_rickmorty.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.aston_courseproject_rickmorty.MainViewModel
import com.example.aston_courseproject_rickmorty.R
import com.example.aston_courseproject_rickmorty.model.Character
import com.example.aston_courseproject_rickmorty.model.Location
import com.example.aston_courseproject_rickmorty.recycler_view.CharacterRecyclerAdapter
import com.example.aston_courseproject_rickmorty.utils.CharacterDiffUtilCallback
import com.example.aston_courseproject_rickmorty.utils.RecyclerDecorator
import com.example.aston_courseproject_rickmorty.viewmodel.LocationDetailsViewModel
import com.example.aston_courseproject_rickmorty.viewmodel.factory.LocationDetailsViewModelFactory


private const val ARG_LOCATION_ID = "locationId"

/**
 * A simple [Fragment] subclass.
 * Use the [LocationDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LocationDetailsFragment : Fragment(), CharacterRecyclerAdapter.CharacterViewHolder.ItemClickListener {
    private var locationId: Int? = null

    private lateinit var viewModel: LocationDetailsViewModel
    private lateinit var mainViewModel: MainViewModel
    private var listForRecycler: MutableList<Character> = mutableListOf()
    private lateinit var recyclerCharacterList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            locationId = it.getInt(ARG_LOCATION_ID)
        }

        viewModel = ViewModelProvider(this, LocationDetailsViewModelFactory(locationId!!, requireContext(), requireActivity()))[LocationDetailsViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val detailsLayout = view.findViewById<ConstraintLayout>(R.id.location_detailsLayout)
        detailsLayout.visibility = View.INVISIBLE
        val pbView = view.findViewById<ProgressBar>(R.id.progress)
        pbView.visibility = View.VISIBLE

        viewModel.currentLocation.observe(viewLifecycleOwner) {
            detailsLayout.visibility = View.VISIBLE
            pbView.visibility = View.GONE
            updateView(it)
        }
        viewModel.characterList.observe(viewLifecycleOwner) {
            listForRecycler.clear()
            listForRecycler.addAll(it)
            notifyWithDiffUtil()
        }

        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {

            //TODO: add logic, when coroutines are enabled
            this.viewModelStore.clear()
            viewModel = ViewModelProvider(this, LocationDetailsViewModelFactory(locationId!!, requireContext(), requireActivity()))[LocationDetailsViewModel::class.java]

            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun updateView(currentLocation: Location) {
        val textViewName = view?.findViewById<TextView>(R.id.textView_name)
        val textViewType = view?.findViewById<TextView>(R.id.textView_type)
        val textViewDimension = view?.findViewById<TextView>(R.id.textView_dimension)

        textViewName?.text = currentLocation.name
        textViewType?.text = currentLocation.type
        textViewDimension?.text = currentLocation.dimension

        initRecyclerView()
    }

    private fun initRecyclerView() {
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        val sidePadding = 5
        val topPadding = 5
        val mAdapter = CharacterRecyclerAdapter(
            listForRecycler, this
        )
        recyclerCharacterList = requireView().findViewById(R.id.recycler_residents)
        recyclerCharacterList.apply {
            setHasFixedSize(true)
            layoutManager = mLayoutManager
            addItemDecoration(RecyclerDecorator(sidePadding, topPadding))
            adapter = mAdapter
        }

        notifyWithDiffUtil()
    }

    private fun notifyWithDiffUtil() {
        val characterDiffUtilCallback = CharacterDiffUtilCallback(emptyList(), listForRecycler)
        val characterDiffResult = DiffUtil.calculateDiff(characterDiffUtilCallback)
        recyclerCharacterList.adapter?.let { characterDiffResult.dispatchUpdatesTo(it) }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param locationId Parameter 1.
         * @return A new instance of fragment LocationDetailsFragment.
         */
        @JvmStatic
        fun newInstance(locationId: Int) =
            LocationDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_LOCATION_ID, locationId)
                }
            }
    }

    override fun onItemClick(character: Character?) {
        viewModel.openFragment(character)
    }
}