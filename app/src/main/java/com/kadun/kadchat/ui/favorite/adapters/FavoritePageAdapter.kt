package com.kadun.kadchat.ui.favorite.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kadun.kadchat.ui.favorite.FavoriteFragment
import com.kadun.kadchat.ui.favorite.FavoritePageFragment
import com.kadun.kadchat.ui.favorite.data.FavoriteType

class FavoritePageAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = FAVORITE_PAGE_COUNT

    override fun createFragment(position: Int): Fragment {
        val type = when (position) {
            FavoriteFragment.SUBREDDITS_POSITION -> FavoriteType.SUBREDDITS
            FavoriteFragment.POSTS_POSITION -> FavoriteType.POSTS
            FavoriteFragment.COMMENTS_POSITION -> FavoriteType.COMMENTS
            else -> throw Exception("Wrong screen type!")
        }
        return FavoritePageFragment.newInstance(type)
    }

    companion object {
        private const val FAVORITE_PAGE_COUNT = 3
    }
}