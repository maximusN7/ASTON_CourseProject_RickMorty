package com.example.aston_courseproject_rickmorty.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.*
import com.example.aston_courseproject_rickmorty.MainViewModel
import com.example.aston_courseproject_rickmorty.MainViewModelFactory
import com.example.aston_courseproject_rickmorty.R
import com.example.aston_courseproject_rickmorty.model.Character
import com.example.aston_courseproject_rickmorty.recycler_view.CharacterLoaderStateAdapter
import com.example.aston_courseproject_rickmorty.recycler_view.CharacterPaginationRecyclerAdapter
import com.example.aston_courseproject_rickmorty.utils.CharacterDiffUtilCallback
import com.example.aston_courseproject_rickmorty.utils.RecyclerDecorator
import com.example.aston_courseproject_rickmorty.viewmodel.CharacterViewModel
import kotlinx.coroutines.flow.collectLatest


/**
 * A simple [Fragment] subclass.
 * Use the [CharacterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CharacterFragment : Fragment(), CharacterPaginationRecyclerAdapter.CharacterViewHolder.ItemClickListener {

    private lateinit var viewModel: CharacterViewModel
    private lateinit var mainViewModel: MainViewModel
    private var listForRecycler: MutableList<Character> = mutableListOf()
    private lateinit var recyclerCharacterList: RecyclerView
    /*private val mAdapter: CharacterPaginationRecyclerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        CharacterPaginationRecyclerAdapter(requireContext(), this)
    }*/
    private lateinit var mAdapter: CharacterPaginationRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }

        viewModel = ViewModelProvider(this)[CharacterViewModel::class.java]

        //mAdapter = CharacterPaginationRecyclerAdapter(this, this)

        mainViewModel = ViewModelProvider(requireActivity(), MainViewModelFactory(requireContext()))[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_character, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = CharacterPaginationRecyclerAdapter(requireContext(), this)
        recyclerCharacterList = view.findViewById(R.id.recyclerView_characters)
        recyclerCharacterList.adapter = mAdapter.withLoadStateFooter(footer = CharacterLoaderStateAdapter())
        lifecycleScope.launchWhenCreated {
            viewModel.characterList.collectLatest {
                mAdapter.submitData(it)
            }
        }
        mAdapter.addLoadStateListener { state: CombinedLoadStates ->
            recyclerCharacterList.visibility = if (state.refresh != LoadState.Loading) View.VISIBLE else View.GONE
            val pbView = view.findViewById<ProgressBar>(R.id.progress)
            pbView.visibility = if (state.refresh == LoadState.Loading) View.VISIBLE else View.GONE
        }

        initRecyclerView()

        //viewModel.characterList.observe(viewLifecycleOwner) {
            //listForRecycler.addAll(it)
            //notifyWithDiffUtil()
        //}

    }

    private fun initRecyclerView() {
        val sidePadding = 5
        val topPadding = 5
        //recyclerCharacterList = requireView().findViewById(R.id.recyclerView_characters)
        recyclerCharacterList.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2)
            addItemDecoration(RecyclerDecorator(sidePadding, topPadding))
            //adapter = mAdapter
        }

        //notifyWithDiffUtil()
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
         * @return A new instance of fragment CharacterFragment.
         */
        @JvmStatic
        fun newInstance() =
            CharacterFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    override fun onItemClick(character: Character?) {
        val fragment: Fragment = CharacterDetailsFragment.newInstance(character?.id!!)
        mainViewModel.changeCurrentDetailsFragment(fragment)
    }
}