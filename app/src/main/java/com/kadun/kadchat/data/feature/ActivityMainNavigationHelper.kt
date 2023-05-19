package com.kadun.kadchat.data.feature

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.ui.AppBarConfiguration
import com.kadun.kadchat.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ActivityMainNavigationHelper(
    private val lifecycleOwner: LifecycleOwner,
    navController: NavController
) {

    private val appBarConfigurationWithBottomNav = AppBarConfiguration(
        setOf(
            R.id.navigation_home,
            R.id.navigation_favorite,
            R.id.navigation_profile
        )
    )

    private val _needBottomNavigation = MutableStateFlow(false)
    val needBottomNavigation: StateFlow<Boolean> get() = _needBottomNavigation

    init {
        navController.addDestinationListener()
    }

    private fun NavController.addDestinationListener() {
        addOnDestinationChangedListener { _, destination: NavDestination, _ ->
            lifecycleOwner.lifecycleScope.launch {
                _needBottomNavigation.emit(
                    appBarConfigurationWithBottomNav.topLevelDestinations.contains(destination.id)
                )
            }
        }
    }
}