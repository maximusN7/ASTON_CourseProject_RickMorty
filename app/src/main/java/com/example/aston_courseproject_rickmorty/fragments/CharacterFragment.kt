package com.example.aston_courseproject_rickmorty.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.aston_courseproject_rickmorty.MainViewModel
import com.example.aston_courseproject_rickmorty.R
import com.example.aston_courseproject_rickmorty.model.Character
import com.example.aston_courseproject_rickmorty.model.CharacterForList
import com.example.aston_courseproject_rickmorty.recycler_view.CharacterLoaderStateAdapter
import com.example.aston_courseproject_rickmorty.recycler_view.CharacterPaginationRecyclerAdapter
import com.example.aston_courseproject_rickmorty.utils.CharacterDiffUtilCallback
import com.example.aston_courseproject_rickmorty.utils.RecyclerDecorator
import com.example.aston_courseproject_rickmorty.viewmodel.CharacterDetailsViewModel
import com.example.aston_courseproject_rickmorty.viewmodel.CharacterViewModel
import com.example.aston_courseproject_rickmorty.viewmodel.factory.CharacterDetailsViewModelFactory
import com.example.aston_courseproject_rickmorty.viewmodel.factory.CharacterViewModelFactory
import kotlinx.coroutines.flow.collectLatest


/**
 * A simple [Fragment] subclass.
 * Use the [CharacterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@ExperimentalPagingApi
class CharacterFragment : Fragment(),
    CharacterPaginationRecyclerAdapter.CharacterViewHolder.ItemClickListener {

    private lateinit var viewModel: CharacterViewModel
    private lateinit var recyclerCharacterList: RecyclerView
    private lateinit var mAdapter: CharacterPaginationRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }
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

        recyclerCharacterList = view.findViewById(R.id.recyclerView_characters)
        mAdapter = CharacterPaginationRecyclerAdapter(this)

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
        recyclerCharacterList.adapter =
            mAdapter.withLoadStateFooter(footer = CharacterLoaderStateAdapter())
        recyclerCharacterList.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2)
            addItemDecoration(RecyclerDecorator(sidePadding, topPadding))
        }
    }

    private fun createViewModelUpdateAdapter() {
        val appContext = activity?.applicationContext
        viewModel = ViewModelProvider(
            this,
            CharacterViewModelFactory(requireContext(), appContext!!, requireActivity())
        )[CharacterViewModel::class.java]
        lifecycleScope.launchWhenCreated {
            viewModel.characters.collectLatest {
                mAdapter.submitData(it)
            }
        }
        mAdapter.addLoadStateListener { state: CombinedLoadStates ->
            recyclerCharacterList.visibility =
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
         * @return A new instance of fragment CharacterFragment.
         */
        @JvmStatic
        fun newInstance() =
            CharacterFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    override fun onItemClick(character: CharacterForList?) {
        viewModel.openFragment(character)
    }
}