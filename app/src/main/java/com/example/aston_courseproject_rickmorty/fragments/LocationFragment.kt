package com.example.aston_courseproject_rickmorty.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.aston_courseproject_rickmorty.R
import com.example.aston_courseproject_rickmorty.model.Location
import com.example.aston_courseproject_rickmorty.model.LocationForList
import com.example.aston_courseproject_rickmorty.recycler_view.CharacterLoaderStateAdapter
import com.example.aston_courseproject_rickmorty.recycler_view.LocationPaginationRecyclerAdapter
import com.example.aston_courseproject_rickmorty.recycler_view.LocationRecyclerAdapter
import com.example.aston_courseproject_rickmorty.utils.LocationDiffUtilCallback
import com.example.aston_courseproject_rickmorty.utils.RecyclerDecorator
import com.example.aston_courseproject_rickmorty.viewmodel.LocationViewModel
import com.example.aston_courseproject_rickmorty.viewmodel.factory.LocationViewModelFactory
import kotlinx.coroutines.flow.collectLatest


/**
 * A simple [Fragment] subclass.
 * Use the [LocationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@ExperimentalPagingApi
class LocationFragment : Fragment(),
    LocationPaginationRecyclerAdapter.LocationViewHolder.ItemClickListener {

    private lateinit var viewModel: LocationViewModel
    private lateinit var recyclerLocationList: RecyclerView
    private lateinit var mAdapter: LocationPaginationRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerLocationList = view.findViewById(R.id.recyclerView_locations)
        mAdapter = LocationPaginationRecyclerAdapter(this)

        createViewModelUpdateAdapter()

        initRecyclerView()

        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            this.viewModelStore.clear()

            createViewModelUpdateAdapter()

            swipeRefreshLayout.isRefreshing = false
        }

        val filterButton = view.findViewById<Button>(R.id.button_filter)
        filterButton.setOnClickListener {
            viewModel.openFilterDialog()
        }

    }

    private fun initRecyclerView() {
        val sidePadding = 5
        val topPadding = 5
        recyclerLocationList.adapter = mAdapter.withLoadStateFooter(CharacterLoaderStateAdapter())
        recyclerLocationList.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2)
            addItemDecoration(RecyclerDecorator(sidePadding, topPadding))
        }
    }

    private fun createViewModelUpdateAdapter() {
        val appContext = activity?.applicationContext
        viewModel = ViewModelProvider(
            this,
            LocationViewModelFactory(requireContext(), appContext!!, requireActivity())
        )[LocationViewModel::class.java]
        lifecycleScope.launchWhenCreated {
            viewModel.locations.collectLatest {
                mAdapter.submitData(it)
            }
        }
        mAdapter.addLoadStateListener { state: CombinedLoadStates ->
            recyclerLocationList.visibility =
                if (state.refresh != LoadState.Loading) View.VISIBLE else View.GONE
            val pbView = view?.findViewById<ProgressBar>(R.id.progress)
            pbView?.visibility = if (state.refresh == LoadState.Loading) View.VISIBLE else View.GONE
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment LocationFragment.
         */
        @JvmStatic
        fun newInstance() =
            LocationFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    override fun onItemClick(location: LocationForList?) {
        viewModel.openFragment(location)
    }
}