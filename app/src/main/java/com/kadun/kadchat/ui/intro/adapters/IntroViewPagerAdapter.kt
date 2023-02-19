package com.kadun.kadchat.ui.intro.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kadun.kadchat.ui.intro.IntroPageFragment
import com.kadun.kadchat.ui.intro.data.IntroPagesData

class IntroViewPagerAdapter(
    private val screens: List<IntroPagesData>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return screens.size
    }

    override fun createFragment(position: Int): Fragment {
        val screen: IntroPagesData = screens[position]
        return IntroPageFragment.newInstance(
            screen.text,
            screen.description,
            screen.image,
            screen.isLastPage
        )
    }
}
