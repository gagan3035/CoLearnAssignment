package com.example.colearn.ui

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.commit
import com.example.colearn.R
import com.example.colearn.databinding.ActivityMainBinding
import com.example.colearn.ui.home.HomeFragment
import com.example.colearn.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val homeViewModel : HomeViewModel by viewModels()

    var binding : ActivityMainBinding? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        setUpViews()
    }

    private fun setUpViews() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as HomeFragment?
        if (fragment == null) {
           supportFragmentManager.commit {
               setReorderingAllowed(true)
               add(R.id.fragment_container_view, HomeFragment::class.java, null)
           }
        }
    }

}