package com.kadun.kadchat

import android.animation.LayoutTransition
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.kadun.kadchat.data.feature.ActivityMainNavigationHelper
import com.kadun.kadchat.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var navigationHelper: ActivityMainNavigationHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavigation()
        initScopes()
        with(binding) {
            initViews()
        }
    }

    private fun ActivityMainBinding.initViews() {
        root.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
    }

    private fun initNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        binding.navView.setupWithNavController(navController)
        navigationHelper = ActivityMainNavigationHelper(this, navController)
    }

    private fun initScopes() = lifecycleScope.launch {
        launch {
            navigationHelper?.needBottomNavigationTest?.collectLatest {
                binding.navView.isVisible = it
            }
        }
    }
}