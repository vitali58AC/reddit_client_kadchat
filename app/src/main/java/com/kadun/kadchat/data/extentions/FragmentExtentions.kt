package com.kadun.kadchat.data.extentions

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import com.kadun.kadchat.common.aboveBottomNavigation
import com.kadun.kadchat.common.createDefaultSnackbar

fun <T : Fragment> T.withArguments(action: Bundle.() -> Unit): T {
    return apply {
        val args = Bundle().apply(action)
        arguments = args
    }
}

/**
 * Убрать предупреждение(которое появилось с 33 API) для getParcelable
 */
inline fun <reified T : Parcelable> Bundle.getParcelableSafe(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

fun Fragment.showSnackbar(message: String) {
    createDefaultSnackbar(message)
        .aboveBottomNavigation(requireActivity())
        .show()
}
