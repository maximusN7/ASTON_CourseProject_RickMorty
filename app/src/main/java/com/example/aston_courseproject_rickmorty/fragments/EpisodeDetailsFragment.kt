package com.example.aston_courseproject_rickmorty.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.aston_courseproject_rickmorty.recycler_view.CharacterRecyclerAdapter
import com.example.aston_courseproject_rickmorty.utils.CharacterDiffUtilCallback
import com.example.aston_courseproject_rickmorty.utils.RecyclerDecorator
import com.example.aston_courseproject_rickmorty.viewmodel.EpisodeDetailsViewModel
import com.example.aston_courseproject_rickmorty.viewmodel.EpisodeDetailsViewModelFactory


private const val ARG_EPISODE_ID = "episodeId"

/**
 * A simple [Fragment] subclass.
 * Use the [EpisodeDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EpisodeDetailsFragment : Fragment(), CharacterRecyclerAdapter.CharacterViewHolder.ItemClickListener {
    private var episodeId: Int? = null

    private lateinit var viewModel: EpisodeDetailsViewModel
    private lateinit var mainViewModel: MainViewModel
    private var listForRecycler: MutableList<Character> = mutableListOf()
    private lateinit var recyclerCharacterList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            episodeId = it.getInt(ARG_EPISODE_ID)
        }

        viewModel = ViewModelProvider(this, EpisodeDetailsViewModelFactory(episodeId!!))[EpisodeDetailsViewModel::class.java]

        mainViewModel = ViewModelProvider(requireActivity(), MainViewModelFactory(requireContext()))[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_episode_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentEpisode.observe(viewLifecycleOwner) {
            updateView(it)
        }
        viewModel.characterList.observe(viewLifecycleOwner) {
            listForRecycler.clear()
            listForRecycler.addAll(it)
            notifyWithDiffUtil()
        }
    }

    private fun updateView(currentEpisode: Episode) {
        val textViewName = view?.findViewById<TextView>(R.id.textView_name)
        val textViewAirDate = view?.findViewById<TextView>(R.id.textView_airdate)
        val textViewEpisode = view?.findViewById<TextView>(R.id.textView_episode)

        textViewName?.text = currentEpisode.name
        textViewAirDate?.text = currentEpisode.episode
        textViewEpisode?.text = currentEpisode.air_date

        initRecyclerView()
    }

    private fun initRecyclerView() {
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        val sidePadding = 5
        val topPadding = 5
        val mAdapter = CharacterRecyclerAdapter(
            (activity as AppCompatActivity),
            listForRecycler, this
        )
        recyclerCharacterList = requireView().findViewById(R.id.recycler_characters)
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
         * @param episodeId Parameter 1.
         * @return A new instance of fragment EpisodeDetailsFragment.
         */
        @JvmStatic
        fun newInstance(episodeId: Int) =
            EpisodeDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_EPISODE_ID, episodeId)
                }
            }
    }

    override fun onItemClick(character: Character) {
        val fragment: Fragment = CharacterDetailsFragment.newInstance(character.id!!)
        mainViewModel.changeCurrentDetailsFragment(fragment)
    }
}