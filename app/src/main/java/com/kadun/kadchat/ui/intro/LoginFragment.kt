package com.kadun.kadchat.ui.intro

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kadun.kadchat.common.InsetsWithBindingFragment
import com.kadun.kadchat.common.LOGOUT_KEY
import com.kadun.kadchat.common.UserSettingPrefs
import com.kadun.kadchat.databinding.FragmentLoginBinding
import com.kirkbushman.auth.RedditAuth
import org.koin.android.ext.android.inject

class LoginFragment : InsetsWithBindingFragment<FragmentLoginBinding>() {

    private val authClient: RedditAuth by inject()
    private val userSettings: UserSettingPrefs by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (authClient.hasSavedBearer() && userSettings.getValue(LOGOUT_KEY).not()) {
            navigateToNextFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.initViews()
    }

    private fun FragmentLoginBinding.initViews() {
        enterButton.setOnClickListener { navigateToNextFragment() }
    }

    private fun navigateToNextFragment() {
        findNavController().navigate(LoginFragmentDirections.toAuthFragment())
    }

    override fun getTopView() = null
    override fun getRootView() = binding.root
    override fun getBinding(): (inflater: LayoutInflater, root: ViewGroup?, attachToRoot: Boolean) -> FragmentLoginBinding =
        FragmentLoginBinding::inflate
}