package com.kadun.kadchat.ui.favorite.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kadun.kadchat.ui.favorite.FavoritePageFragment
import com.kadun.kadchat.ui.favorite.data.FavoriteType

class FavoritePageAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = FAVORITE_PAGE_COUNT

    override fun createFragment(position: Int): Fragment {
        val type = if (position == 0) FavoriteType.SUBREDDITS else FavoriteType.POSTS
        return FavoritePageFragment.newInstance(type)
    }

    companion object {
        private const val FAVORITE_PAGE_COUNT = 2
    }
}