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
import com.example.aston_courseproject_rickmorty.model.Episode
import com.example.aston_courseproject_rickmorty.model.EpisodeForList
import com.example.aston_courseproject_rickmorty.recycler_view.CharacterLoaderStateAdapter
import com.example.aston_courseproject_rickmorty.recycler_view.CharacterPaginationRecyclerAdapter
import com.example.aston_courseproject_rickmorty.recycler_view.EpisodePaginationRecyclerAdapter
import com.example.aston_courseproject_rickmorty.recycler_view.EpisodeRecyclerAdapter
import com.example.aston_courseproject_rickmorty.utils.EpisodeDiffUtilCallback
import com.example.aston_courseproject_rickmorty.utils.RecyclerDecorator
import com.example.aston_courseproject_rickmorty.viewmodel.EpisodeViewModel
import com.example.aston_courseproject_rickmorty.viewmodel.factory.EpisodeViewModelFactory
import kotlinx.coroutines.flow.collectLatest


/**
 * A simple [Fragment] subclass.
 * Use the [EpisodeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@ExperimentalPagingApi
class EpisodeFragment : Fragment(),
    EpisodePaginationRecyclerAdapter.EpisodeViewHolder.ItemClickListener {

    private lateinit var viewModel: EpisodeViewModel
    private lateinit var recyclerEpisodeList: RecyclerView
    private lateinit var mAdapter: EpisodePaginationRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_episode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerEpisodeList = view.findViewById(R.id.recyclerView_episodes)
        mAdapter = EpisodePaginationRecyclerAdapter(this)

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
        recyclerEpisodeList.adapter = mAdapter.withLoadStateFooter(CharacterLoaderStateAdapter())
        recyclerEpisodeList.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2)
            addItemDecoration(RecyclerDecorator(sidePadding, topPadding))
        }
    }

    private fun createViewModelUpdateAdapter() {
        val appContext = activity?.applicationContext
        viewModel = ViewModelProvider(
            this,
            EpisodeViewModelFactory(requireContext(), appContext!!, requireActivity())
        )[EpisodeViewModel::class.java]
        lifecycleScope.launchWhenCreated {
            viewModel.episodes.collectLatest {
                mAdapter.submitData(it)
            }
        }
        mAdapter.addLoadStateListener { state: CombinedLoadStates ->
            recyclerEpisodeList.visibility =
                if (state.refresh != LoadState.Loading) View.VISIBLE else View.GONE
            val pbView = view?.findViewById<ProgressBar>(R.id.progress)
            pbView?.visibility = if (state.refresh == LoadState.Loading) View.VISIBLE else View.GONE
        }
    }

    /*private fun notifyWithDiffUtil() {
        val episodeDiffUtilCallback = EpisodeDiffUtilCallback(emptyList(), listForRecycler)
        val episodeDiffResult = DiffUtil.calculateDiff(episodeDiffUtilCallback)
        recyclerEpisodeList.adapter?.let { episodeDiffResult.dispatchUpdatesTo(it) }
    }*/

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment EpisodeFragment.
         */
        @JvmStatic
        fun newInstance() =
            EpisodeFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    override fun onItemClick(episode: EpisodeForList?) {
        viewModel.openFragment(episode)
    }
}