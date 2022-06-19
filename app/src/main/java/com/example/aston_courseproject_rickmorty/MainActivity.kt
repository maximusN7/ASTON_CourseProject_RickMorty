package com.example.aston_courseproject_rickmorty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.aston_courseproject_rickmorty.fragments.CharacterFragment
import com.example.aston_courseproject_rickmorty.fragments.EpisodeFragment
import com.example.aston_courseproject_rickmorty.fragments.LocationFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel =
            ViewModelProvider(this, MainViewModelFactory(this))[MainViewModel::class.java]

        viewModel.changeCurrentFragment(CharacterFragment())

        viewModel.titleString.observe(this) {
            title = it
        }

        viewModel.currentFragment.observe(this) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView, it)
                commit()
            }
        }

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_character -> {
                    viewModel.changeCurrentFragment(CharacterFragment())
                }
                R.id.ic_episode -> {
                    viewModel.changeCurrentFragment(EpisodeFragment())
                }
                R.id.ic_location -> {
                    viewModel.changeCurrentFragment(LocationFragment())
                }
            }
            true
        }
    }

}