package com.example.aston_courseproject_rickmorty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
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
        viewModel.showToolbar.observe(this) {
            this.supportActionBar?.setDisplayHomeAsUpEnabled(it)
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

        viewModel.currentDetailsFragment.observe(this) {
            val fTrans = supportFragmentManager.beginTransaction()
            supportFragmentManager.findFragmentByTag("current_main_fragment")?.let { fTrans.hide(it) }
            fTrans.apply{
                replace(R.id.fragmentContainerView, it, "current_main_fragment")
                addToBackStack(null)
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
                    viewModel.changeCurrentFragment(CharacterFragment.newInstance())
                }
                R.id.ic_episode -> {
                    viewModel.changeCurrentFragment(EpisodeFragment.newInstance())
                }
                R.id.ic_location -> {
                    viewModel.changeCurrentFragment(LocationFragment.newInstance())
                }
            }
            true
        }
        bottomNavigation.selectedItemId = R.id.ic_character
    }

    @Override
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                checkFragment()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @Override
    override fun onBackPressed() {
        super.onBackPressed()
        checkFragment()
    }

    private fun checkFragment() {
        val currentFragment = supportFragmentManager.findFragmentByTag("current_main_fragment")
        viewModel.checkFragment(currentFragment!!)
    }

}