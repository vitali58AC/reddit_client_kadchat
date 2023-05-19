package com.kadun.kadchat.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.kadun.kadchat.R
import com.kadun.kadchat.common.InsetsWithBindingFragment
import com.kadun.kadchat.databinding.FragmentFavoriteBinding
import com.kadun.kadchat.ui.favorite.adapters.FavoritePageAdapter
import com.kadun.kadchat.ui.home.HomeFragment

class FavoriteFragment : InsetsWithBindingFragment<FragmentFavoriteBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.initViewPager()
    }

    private fun FragmentFavoriteBinding.initViewPager() {
        viewPager.adapter =
            FavoritePageAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        TabLayoutMediator(tabsRoot, viewPager, true, true) { tab, position ->
            tab.text = if (position == HomeFragment.START_POSITION) {
                getString(R.string.subreddits)
            } else {
                getString(R.string.posts)
            }
        }.attach()
    }

    override fun getTopView() = null
    override fun getRootView() = binding.root
    override fun getBinding(): (inflater: LayoutInflater, root: ViewGroup?, attachToRoot: Boolean) -> FragmentFavoriteBinding =
        FragmentFavoriteBinding::inflate
}