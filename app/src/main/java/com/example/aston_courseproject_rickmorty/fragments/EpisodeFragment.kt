package com.example.aston_courseproject_rickmorty.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aston_courseproject_rickmorty.R
import com.example.aston_courseproject_rickmorty.model.Episode
import com.example.aston_courseproject_rickmorty.model.Location
import com.example.aston_courseproject_rickmorty.recycler_view.EpisodeRecyclerAdapter
import com.example.aston_courseproject_rickmorty.recycler_view.LocationRecyclerAdapter
import com.example.aston_courseproject_rickmorty.utils.EpisodeDiffUtilCallback
import com.example.aston_courseproject_rickmorty.utils.LocationDiffUtilCallback
import com.example.aston_courseproject_rickmorty.utils.RecyclerDecorator
import com.example.aston_courseproject_rickmorty.viewmodel.EpisodeViewModel
import com.example.aston_courseproject_rickmorty.viewmodel.LocationViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EpisodeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EpisodeFragment : Fragment(), EpisodeRecyclerAdapter.EpisodeViewHolder.ItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var viewModel: EpisodeViewModel
    lateinit var listForRecycler: MutableList<Episode>
    lateinit var recyclerEpisodeList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        viewModel = ViewModelProvider(this)[EpisodeViewModel::class.java]

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
            val episodeDiffUtilCallback = EpisodeDiffUtilCallback(emptyList(), listForRecycler)
            val episodeDiffResult = DiffUtil.calculateDiff(episodeDiffUtilCallback)
            recyclerEpisodeList.adapter?.let { episodeDiffResult.dispatchUpdatesTo(it) }
        }

    }

    fun initRecyclerView() {
        recyclerEpisodeList = requireView().findViewById(R.id.recyclerView_episodes)
        recyclerEpisodeList.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 2)
        recyclerEpisodeList.layoutManager = layoutManager

        val sidePadding = 5
        val topPadding = 5
        recyclerEpisodeList.addItemDecoration(RecyclerDecorator(sidePadding, topPadding))

        listForRecycler = mutableListOf()
        val adapter: EpisodeRecyclerAdapter = EpisodeRecyclerAdapter(
            (activity as AppCompatActivity),
            listForRecycler, this
        )

        recyclerEpisodeList.adapter = adapter
        val episodeDiffUtilCallback = EpisodeDiffUtilCallback(emptyList(), listForRecycler)
        val episodeDiffResult = DiffUtil.calculateDiff(episodeDiffUtilCallback)
        recyclerEpisodeList.adapter?.let { episodeDiffResult.dispatchUpdatesTo(it) }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EpisodeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EpisodeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onItemClick(episode: Episode) {
        TODO("Not yet implemented")
//        val fragment: Fragment = EpisodeDetailsFragment.newInstance(episode.id!!)
//
//        val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
//
//        requireActivity().supportFragmentManager.findFragmentByTag("current_main_fragment")
//            ?.let { transaction.hide(it) }
//        transaction.add(R.id.fragmentContainerView, fragment)
//        transaction.addToBackStack(null)
//        transaction.commit()
    }
}