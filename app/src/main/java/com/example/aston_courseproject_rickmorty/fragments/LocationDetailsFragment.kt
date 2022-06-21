package com.example.aston_courseproject_rickmorty.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aston_courseproject_rickmorty.R
import com.example.aston_courseproject_rickmorty.model.Character
import com.example.aston_courseproject_rickmorty.model.Location
import com.example.aston_courseproject_rickmorty.recycler_view.CharacterRecyclerAdapter
import com.example.aston_courseproject_rickmorty.utils.CharacterDiffUtilCallback
import com.example.aston_courseproject_rickmorty.utils.RecyclerDecorator
import com.example.aston_courseproject_rickmorty.viewmodel.LocationDetailsViewModel
import com.example.aston_courseproject_rickmorty.viewmodel.LocationDetailsViewModelFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [LocationDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LocationDetailsFragment : Fragment(), CharacterRecyclerAdapter.CharacterViewHolder.ItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: Int? = null

    private lateinit var viewModel: LocationDetailsViewModel
    lateinit var listForRecycler: MutableList<Character>
    lateinit var recyclerCharacterList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
        }

        viewModel = ViewModelProvider(this, LocationDetailsViewModelFactory(param1!!))[LocationDetailsViewModel::class.java]

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


        viewModel.currentLocation.observe(viewLifecycleOwner) {
            updateView(it)
        }
        viewModel.characterList.observe(viewLifecycleOwner) {
            listForRecycler.clear()
            listForRecycler.addAll(it)
            val characterDiffUtilCallback = CharacterDiffUtilCallback(emptyList(), listForRecycler)
            val characterDiffResult = DiffUtil.calculateDiff(characterDiffUtilCallback)
            recyclerCharacterList.adapter?.let { characterDiffResult.dispatchUpdatesTo(it) }
        }
    }

    fun updateView(currentLocation: Location) {

        val textViewName = view?.findViewById<TextView>(R.id.textView_name)
        val textViewType = view?.findViewById<TextView>(R.id.textView_type)
        val textViewDimension = view?.findViewById<TextView>(R.id.textView_dimension)

        textViewName?.text = currentLocation.name
        textViewType?.text = currentLocation.type
        textViewDimension?.text = currentLocation.dimension

        initRecyclerView()
    }

    fun initRecyclerView() {
        recyclerCharacterList = requireView().findViewById(R.id.recycler_residents)
        recyclerCharacterList.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerCharacterList.layoutManager = layoutManager

        val sidePadding = 5
        val topPadding = 5
        recyclerCharacterList.addItemDecoration(RecyclerDecorator(sidePadding, topPadding))

        listForRecycler = mutableListOf()

        val adapter: CharacterRecyclerAdapter = CharacterRecyclerAdapter(
            (activity as AppCompatActivity),
            listForRecycler, this
        )

        recyclerCharacterList.adapter = adapter

        val characterDiffUtilCallback = CharacterDiffUtilCallback(emptyList(), listForRecycler)
        val characterDiffResult = DiffUtil.calculateDiff(characterDiffUtilCallback)
        recyclerCharacterList.adapter?.let { characterDiffResult.dispatchUpdatesTo(it) }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LocationDetailsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Int) =
            LocationDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
    }

    override fun onItemClick(character: Character) {
        val fragment: Fragment = CharacterDetailsFragment.newInstance(character.id!!)

        val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()

        requireActivity().supportFragmentManager.findFragmentByTag("current_main_fragment")
            ?.let { transaction.hide(it) }
        transaction.replace(R.id.fragmentContainerView, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}