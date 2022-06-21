package com.example.aston_courseproject_rickmorty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
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

        viewModel.titleString.observe(this) {
            title = it
        }

        viewModel.currentFragment.observe(this) {

            val fragments = supportFragmentManager.fragments
            for (fragment in fragments) {
                supportFragmentManager.beginTransaction().remove(fragment).commit()
            }
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView, it, "current_main_fragment")
                commit()
            }

        }

//        val connectivityManager = this.getSystemService(
//            Context.CONNECTIVITY_SERVICE
//        ) as ConnectivityManager
//        if (connectivityManager.activeNetworkInfo?.isConnected!!) Log.e("AAA", "connected") else Log.e("AAA", "not connected")


        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener {
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
        bottomNavigation.selectedItemId = R.id.ic_character
    }

}