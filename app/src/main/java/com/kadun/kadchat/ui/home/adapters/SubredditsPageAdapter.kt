package com.kadun.kadchat.ui.home.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kadun.kadchat.ui.home.SubredditPageFragment
import com.kadun.kadchat.ui.home.data.SubredditsType

class SubredditsPageAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = SUBREDDIT_TYPES_COUNT

    override fun createFragment(position: Int): Fragment {
        val type = if (position == 0) SubredditsType.NEW else SubredditsType.POPULAR
        return SubredditPageFragment.newInstance(type)
    }

    companion object {
        private const val SUBREDDIT_TYPES_COUNT = 2
    }
}