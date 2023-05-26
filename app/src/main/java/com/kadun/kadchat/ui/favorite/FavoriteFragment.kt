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

class FavoriteFragment : InsetsWithBindingFragment<FragmentFavoriteBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.initViewPager()
    }

    private fun FragmentFavoriteBinding.initViewPager() {
        viewPager.adapter =
            FavoritePageAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        TabLayoutMediator(tabsRoot, viewPager, true, true) { tab, position ->
            tab.text = when (position) {
                SUBREDDITS_POSITION -> getString(R.string.subreddits)
                POSTS_POSITION -> getString(R.string.posts)
                COMMENTS_POSITION -> getString(R.string.comments)
                else -> ""
            }
        }.attach()
    }

    override fun getTopView() = null
    override fun getRootView() = binding.root
    override fun getBinding(): (inflater: LayoutInflater, root: ViewGroup?, attachToRoot: Boolean) -> FragmentFavoriteBinding =
        FragmentFavoriteBinding::inflate

    companion object {
        const val SUBREDDITS_POSITION = 0
        const val POSTS_POSITION = 1
        const val COMMENTS_POSITION = 2
    }
}