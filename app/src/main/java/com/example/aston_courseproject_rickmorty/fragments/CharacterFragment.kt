package com.example.aston_courseproject_rickmorty.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import com.example.aston_courseproject_rickmorty.R
import com.example.aston_courseproject_rickmorty.model.Character
import com.example.aston_courseproject_rickmorty.recycler_view.CharacterRecyclerAdapter
import com.example.aston_courseproject_rickmorty.utils.CharacterDiffUtilCallback
import com.example.aston_courseproject_rickmorty.utils.RecyclerDecorator
import com.example.aston_courseproject_rickmorty.viewmodel.CharacterViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CharacterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CharacterFragment : Fragment(), CharacterRecyclerAdapter.CharacterViewHolder.ItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var viewModel: CharacterViewModel
    lateinit var listForRecycler: MutableList<Character>
    lateinit var recyclerCharacterList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        viewModel = ViewModelProvider(this)[CharacterViewModel::class.java]

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

        initRecyclerView()

        viewModel.characterList.observe(viewLifecycleOwner) {
            listForRecycler.clear()
            listForRecycler.addAll(it)
            val characterDiffUtilCallback = CharacterDiffUtilCallback(emptyList(), listForRecycler)
            val characterDiffResult = DiffUtil.calculateDiff(characterDiffUtilCallback)
            recyclerCharacterList.adapter?.let { characterDiffResult.dispatchUpdatesTo(it) }
        }

    }

    fun initRecyclerView() {
        recyclerCharacterList = requireView().findViewById(R.id.recyclerView_characters)
        recyclerCharacterList.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 2)
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
         * @return A new instance of fragment CharacterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CharacterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onItemClick(character: Character) {
        val fragment: Fragment = CharacterDetailsFragment.newInstance(character.id!!)

        val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()

        requireActivity().supportFragmentManager.findFragmentByTag("current_main_fragment")
            ?.let { transaction.hide(it) }
        transaction.add(R.id.fragmentContainerView, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}