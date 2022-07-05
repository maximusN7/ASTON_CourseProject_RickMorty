package com.example.aston_courseproject_rickmorty.fragments

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.aston_courseproject_rickmorty.R
import com.example.aston_courseproject_rickmorty.fragments.dialogs.CharacterFilterDialog
import com.example.aston_courseproject_rickmorty.fragments.dialogs.Filter
import com.example.aston_courseproject_rickmorty.model.dto.CharacterForListDto
import com.example.aston_courseproject_rickmorty.recycler_view.MyLoaderStateAdapter
import com.example.aston_courseproject_rickmorty.recycler_view.CharacterPaginationRecyclerAdapter
import com.example.aston_courseproject_rickmorty.utils.RecyclerDecorator
import com.example.aston_courseproject_rickmorty.viewmodel.CharacterViewModel
import com.example.aston_courseproject_rickmorty.viewmodel.factory.CharacterViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 * Use the [CharacterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@ExperimentalPagingApi
class CharacterFragment : Fragment(),
    CharacterPaginationRecyclerAdapter.CharacterViewHolder.ItemClickListener,
    CharacterFilterDialog.ApplyClickListener {

    private lateinit var viewModel: CharacterViewModel
    private lateinit var recyclerCharacterList: RecyclerView
    private lateinit var mAdapter: CharacterPaginationRecyclerAdapter
    private var filterList = mutableListOf(Filter(), Filter(), Filter(), Filter(), Filter())

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
        filterList = mutableListOf(Filter(), Filter(), Filter(), Filter(), Filter())

        initRecyclerView()

        val editTextName = view.findViewById<EditText>(R.id.editTextName)
        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            this.viewModelStore.clear()

            createViewModelUpdateAdapter()

            if (editTextName.text.toString() != "") editTextName.setText("")

            swipeRefreshLayout.isRefreshing = false
        }

        val filterButton = view.findViewById<Button>(R.id.button_filter)
        filterButton.setOnClickListener {
            viewModel.openFilterDialog()
        }

        editTextName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
                editTextName.isCursorVisible = true
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.isNotEmpty()) {
                    filterList[0].stringToFilter = s.toString()
                    filterList[0].isApplied = true
                } else {
                    filterList[0].stringToFilter = ""
                    filterList[0].isApplied = false
                }

                clearView()
                createViewModelUpdateAdapter()
            }
        })
    }

    fun clearView() {
        this.viewModelStore.clear()
    }

    private fun initRecyclerView() {
        recyclerCharacterList.adapter = mAdapter.withLoadStateFooter(MyLoaderStateAdapter())
        recyclerCharacterList.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2)
            addItemDecoration(RecyclerDecorator())
        }
    }

    private fun createViewModelUpdateAdapter() {
        val appContext = activity?.applicationContext
        viewModel = ViewModelProvider(
            this,
            CharacterViewModelFactory(
                requireContext(),
                appContext!!,
                requireActivity(),
                this,
                filterList
            )
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
            val errorText = view?.findViewById<TextView>(R.id.errorText)
            val errorTextTitle = view?.findViewById<TextView>(R.id.errorTextTitle)
            when (state.refresh.toString()) {
                "Error(endOfPaginationReached=false, error=java.io.IOException: Wrong Query)" -> {
                    errorText?.visibility = View.VISIBLE
                    errorTextTitle?.visibility = View.VISIBLE
                    errorTextTitle?.text = getString(R.string.error_empty_list_character_title)
                    errorText?.text = getString(R.string.error_empty_list_character)
                }
                "Error(endOfPaginationReached=false, error=java.io.IOException: Empty Database)" -> {
                    errorText?.visibility = View.VISIBLE
                    errorTextTitle?.visibility = View.VISIBLE
                    errorTextTitle?.text = getString(R.string.error_empty_database_title)
                    errorText?.text = getString(R.string.error_empty_database)
                }
                else -> {
                    errorText?.visibility = View.GONE
                    errorTextTitle?.visibility = View.GONE
                }
            }
        }

        viewModel.statusFilter.observe(viewLifecycleOwner) {
            if (it.stringToFilter != "" && it.isApplied) {
                filterList[1].stringToFilter = it.stringToFilter
                filterList[1].isApplied = it.isApplied
            } else {
                filterList[1].stringToFilter = ""
                filterList[1].isApplied = false
            }
            this.viewModelStore.clear()
            createViewModelUpdateAdapter()
        }
        viewModel.speciesFilter.observe(viewLifecycleOwner) {
            if (it.stringToFilter != "" && it.isApplied) {
                filterList[2].stringToFilter = it.stringToFilter
                filterList[2].isApplied = it.isApplied
            } else {
                filterList[2].stringToFilter = ""
                filterList[2].isApplied = false
            }
            this.viewModelStore.clear()
            createViewModelUpdateAdapter()
        }
        viewModel.typeFilter.observe(viewLifecycleOwner) {
            if (it.stringToFilter != "" && it.isApplied) {
                filterList[3].stringToFilter = it.stringToFilter
                filterList[3].isApplied = it.isApplied
            } else {
                filterList[3].stringToFilter = ""
                filterList[3].isApplied = false
            }
            this.viewModelStore.clear()
            createViewModelUpdateAdapter()
        }
        viewModel.genderFilter.observe(viewLifecycleOwner) {
            if (it.stringToFilter != "" && it.isApplied) {
                filterList[4].stringToFilter = it.stringToFilter
                filterList[4].isApplied = it.isApplied
            } else {
                filterList[4].stringToFilter = ""
                filterList[4].isApplied = false
            }
            this.viewModelStore.clear()
            createViewModelUpdateAdapter()
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

    override fun onItemClick(character: CharacterForListDto?) {
        viewModel.openFragment(character)
    }

    override fun onApplyClick(dialog: Dialog) {
        val editTextSearch = view?.findViewById<EditText>(R.id.editTextName)
        editTextSearch?.isCursorVisible = false
        viewModel.onApplyClick(dialog)
    }
}