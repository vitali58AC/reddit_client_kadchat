package com.kadun.kadchat.ui.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kadun.kadchat.common.InsetsWithBindingFragment
import com.kadun.kadchat.databinding.FragmentLoginBinding

class LoginFragment : InsetsWithBindingFragment<FragmentLoginBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.initViews()
    }

    private fun FragmentLoginBinding.initViews() {
        enterButton.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.toAuthFragment())
        }
    }

    override fun getTopView() = null
    override fun getRootView() = binding.root
    override fun getBinding(): (inflater: LayoutInflater, root: ViewGroup?, attachToRoot: Boolean) -> FragmentLoginBinding =
        FragmentLoginBinding::inflate
}