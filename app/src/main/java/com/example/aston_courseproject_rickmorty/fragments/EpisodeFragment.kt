package com.example.aston_courseproject_rickmorty.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aston_courseproject_rickmorty.MainViewModel
import com.example.aston_courseproject_rickmorty.MainViewModelFactory
import com.example.aston_courseproject_rickmorty.R
import com.example.aston_courseproject_rickmorty.model.Episode
import com.example.aston_courseproject_rickmorty.recycler_view.EpisodeRecyclerAdapter
import com.example.aston_courseproject_rickmorty.utils.EpisodeDiffUtilCallback
import com.example.aston_courseproject_rickmorty.utils.RecyclerDecorator
import com.example.aston_courseproject_rickmorty.viewmodel.EpisodeViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [EpisodeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EpisodeFragment : Fragment(), EpisodeRecyclerAdapter.EpisodeViewHolder.ItemClickListener {

    private lateinit var viewModel: EpisodeViewModel
    private lateinit var mainViewModel: MainViewModel
    private var listForRecycler: MutableList<Episode> = mutableListOf()
    private lateinit var recyclerEpisodeList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }

        viewModel = ViewModelProvider(this)[EpisodeViewModel::class.java]

        mainViewModel = ViewModelProvider(requireActivity(), MainViewModelFactory(requireContext()))[MainViewModel::class.java]
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

        initRecyclerView()

        viewModel.episodeList.observe(viewLifecycleOwner) {
            listForRecycler.clear()
            listForRecycler.addAll(it)
            notifyWithDiffUtil()
        }

    }

    private fun initRecyclerView() {
        val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 2)
        val sidePadding = 5
        val topPadding = 5
        val mAdapter = EpisodeRecyclerAdapter((activity as AppCompatActivity), listForRecycler, this)
        recyclerEpisodeList = requireView().findViewById(R.id.recyclerView_episodes)
        recyclerEpisodeList.apply {
            setHasFixedSize(true)
            layoutManager = mLayoutManager
            addItemDecoration(RecyclerDecorator(sidePadding, topPadding))
            adapter = mAdapter
        }

        notifyWithDiffUtil()
    }

    private fun notifyWithDiffUtil() {
        val episodeDiffUtilCallback = EpisodeDiffUtilCallback(emptyList(), listForRecycler)
        val episodeDiffResult = DiffUtil.calculateDiff(episodeDiffUtilCallback)
        recyclerEpisodeList.adapter?.let { episodeDiffResult.dispatchUpdatesTo(it) }
    }

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

    override fun onItemClick(episode: Episode) {
        val fragment: Fragment = EpisodeDetailsFragment.newInstance(episode.id)
        mainViewModel.changeCurrentDetailsFragment(fragment)
    }
}