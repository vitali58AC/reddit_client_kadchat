package com.kadun.kadchat.common

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.viewbinding.ViewBinding

abstract class InsetsWithBindingFragment<T : ViewBinding> : BindingFragment<T>() {

    private var keyboardWasOpened = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        applyInsets(getTopView(), getRootView())
        super.onViewCreated(view, savedInstanceState)
    }

    /**
     *view, for which requires indent from above
     */
    abstract fun getTopView(): View?

    /**
     *view, which requires a bottom-down drop
     */
    abstract fun getRootView(): View?

    private fun applyInsets(
        topView: View? = null,
        rootView: View? = null
    ) {
        rootView?.let { root ->
            ViewCompat.setOnApplyWindowInsetsListener(root) { _, windowInsets ->
                val sysInsets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
                val imeInsets = windowInsets.getInsets(WindowInsetsCompat.Type.ime())

                topView?.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    topMargin = sysInsets.top
                }

                root.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    bottomMargin = sysInsets.bottom
                }

                keyboardWasOpened = if (imeInsets.bottom > 0) {
                    if (!keyboardWasOpened) onKeyboardOpen()
                    true
                } else {
                    if (keyboardWasOpened) onKeyboardClosed()
                    false
                }

                applyAdditionalInsets(sysInsets, imeInsets)
                windowInsets
            }
        }
    }

    /**
     * Add additional insets inside setOnApplyWindowInsetsListener
     */
    protected open fun applyAdditionalInsets(sysInsets: Insets, imeInsets: Insets) {}

    /**
     * Call when the keyboard is opened
     */
    protected open fun onKeyboardOpen() {}

    /**
     * Call when the keyboard is closed
     */
    protected open fun onKeyboardClosed() {}
}