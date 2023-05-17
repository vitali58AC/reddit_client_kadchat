package com.kadun.kadchat.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.kadun.kadchat.R
import com.kadun.kadchat.common.InsetsWithBindingFragment
import com.kadun.kadchat.databinding.FragmentHomeBinding
import com.kadun.kadchat.ui.home.adapters.SubredditsPageAdapter

class HomeFragment : InsetsWithBindingFragment<FragmentHomeBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.initViewPager()
    }

    private fun FragmentHomeBinding.initViewPager() {
        viewPager.adapter = SubredditsPageAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        TabLayoutMediator(tabsRoot, viewPager, true, true) { tab, position ->
            tab.text = if (position == START_POSITION) {
                getString(R.string.new_upper)
            } else {
                getString(R.string.popular)
            }
        }.attach()
    }

    override fun getTopView() = null
    override fun getRootView() = binding.root
    override fun getBinding(): (inflater: LayoutInflater, root: ViewGroup?, attachToRoot: Boolean) -> FragmentHomeBinding =
        FragmentHomeBinding::inflate

    companion object {
        private const val START_POSITION = 0
    }
}