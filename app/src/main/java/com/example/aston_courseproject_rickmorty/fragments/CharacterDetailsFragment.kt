package com.example.aston_courseproject_rickmorty.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aston_courseproject_rickmorty.R
import com.example.aston_courseproject_rickmorty.model.Character
import com.example.aston_courseproject_rickmorty.model.Episode
import com.example.aston_courseproject_rickmorty.model.Location
import com.example.aston_courseproject_rickmorty.recycler_view.CharacterRecyclerAdapter
import com.example.aston_courseproject_rickmorty.utils.CharacterDiffUtilCallback
import com.example.aston_courseproject_rickmorty.utils.RecyclerDecorator
import com.example.aston_courseproject_rickmorty.viewmodel.CharacterDetailsViewModel
import com.example.aston_courseproject_rickmorty.viewmodel.CharacterDetailsViewModelFactory
import com.example.aston_courseproject_rickmorty.viewmodel.CharacterViewModel
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [CharacterDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CharacterDetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: Int? = null

    private lateinit var viewModel: CharacterDetailsViewModel
    lateinit var listForRecycler: MutableList<Episode>
    lateinit var recyclerEpisodesList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
        }

        viewModel = ViewModelProvider(this, CharacterDetailsViewModelFactory(param1!!))[CharacterDetailsViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_character_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.currentCharacter.observe(viewLifecycleOwner) {
            updateView(it)
        }
        viewModel.currentLocation.observe(viewLifecycleOwner) {
            updateViewLocation(it)
        }
        viewModel.currentOrigin.observe(viewLifecycleOwner) {
            updateViewOrigin(it)
        }
    }

    fun updateView(currentCharacter: Character) {
        val imageView = view?.findViewById<ImageView>(R.id.imageView_avatar)
        val textViewName = view?.findViewById<TextView>(R.id.textView_name)
        val textViewStatus = view?.findViewById<TextView>(R.id.textView_status)
        val textViewSpecies = view?.findViewById<TextView>(R.id.textView_species)
        val textViewType = view?.findViewById<TextView>(R.id.textView_type)
        val textViewGender = view?.findViewById<TextView>(R.id.textView_gender)

        Picasso.get().load(currentCharacter.image).into(imageView)
        textViewName?.text = currentCharacter.name
        textViewStatus?.text = currentCharacter.status
        textViewSpecies?.text = currentCharacter.species
        textViewType?.text = currentCharacter.type
        textViewGender?.text = currentCharacter.gender

        initRecyclerView()
    }

    fun updateViewLocation(location: Location) {
        val textViewLocationName = view?.findViewById<TextView>(R.id.textView_locationName)
        val textViewLocationType = view?.findViewById<TextView>(R.id.textView_locationType)
        val textViewLocationDimension = view?.findViewById<TextView>(R.id.textView_locationDimension)

        textViewLocationName?.text = if (location.name != "") location.name else "unknown"
        textViewLocationType?.text = location.type
        textViewLocationDimension?.text = location.dimension
    }

    fun updateViewOrigin(location: Location) {
        val textViewOriginName = view?.findViewById<TextView>(R.id.textView_originName)
        val textViewOriginType = view?.findViewById<TextView>(R.id.textView_originType)
        val textViewOriginDimension = view?.findViewById<TextView>(R.id.textView_originDimension)

        textViewOriginName?.text = if (location.name != "") location.name else "unknown"
        textViewOriginType?.text = location.type
        textViewOriginDimension?.text = location.dimension
    }

    fun initRecyclerView() {
        recyclerEpisodesList = requireView().findViewById(R.id.recycler_episodes)
        recyclerEpisodesList.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 2)
        recyclerEpisodesList.layoutManager = layoutManager

        val sidePadding = 5
        val topPadding = 5
        recyclerEpisodesList.addItemDecoration(RecyclerDecorator(sidePadding, topPadding))

        listForRecycler = mutableListOf()
        // TODO: Connect EpisodeRecyclerAdapter
//        val adapter: CharacterRecyclerAdapter = CharacterRecyclerAdapter(
//            (activity as AppCompatActivity),
//            listForRecycler, this
//        )

//        recyclerEpisodesList.adapter = adapter
        // TODO: create EpisodeDiffUtilCallback
//        val characterDiffUtilCallback = CharacterDiffUtilCallback(emptyList(), listForRecycler)
//        val characterDiffResult = DiffUtil.calculateDiff(characterDiffUtilCallback)
//        recyclerEpisodesList.adapter?.let { characterDiffResult.dispatchUpdatesTo(it) }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CharacterDetailsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Int) =
            CharacterDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
    }
}