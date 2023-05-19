package com.kadun.kadchat.common

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.BaseTransientBottomBar.BaseCallback
import com.google.android.material.snackbar.Snackbar
import com.kadun.kadchat.R

@SuppressLint("ShowToast")
fun View.createDefaultSnackbar(message: String, length: Int = Snackbar.LENGTH_SHORT): Snackbar {
    return Snackbar.make(this, message, length)
}

fun Fragment.createDefaultSnackbar(message: String, length: Int = Snackbar.LENGTH_SHORT): Snackbar =
    requireActivity().window.decorView.rootView.createDefaultSnackbar(message, length)

fun Snackbar.setCustomAnchorView(view: View) = this.apply { anchorView = view }

fun Snackbar.setOnDismissListener(onDismiss: () -> Unit) = addCallback(object : BaseCallback<Snackbar>() {
    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
        super.onDismissed(transientBottomBar, event)
        onDismiss()
    }
})

/**
 * Show snackbar above current bottom navigation view.
 */
fun Snackbar.aboveBottomNavigation(activity: Activity) =
    activity.window?.decorView?.rootView?.let { rootView ->
        val homeBottomNav: BottomNavigationView? = rootView.findViewById(R.id.nav_view)
        when {
            homeBottomNav != null && homeBottomNav.isVisible -> setCustomAnchorView(homeBottomNav)
            else -> this
        }
    } ?: this
