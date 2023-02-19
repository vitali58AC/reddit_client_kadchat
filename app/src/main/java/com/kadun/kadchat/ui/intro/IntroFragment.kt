package com.kadun.kadchat.ui.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kadun.kadchat.R
import com.kadun.kadchat.common.InsetsWithBindingFragment
import com.kadun.kadchat.databinding.FragmentIntroBinding
import com.kadun.kadchat.ui.intro.adapters.IntroViewPagerAdapter
import com.kadun.kadchat.ui.intro.data.IntroPagesData

class IntroFragment : InsetsWithBindingFragment<FragmentIntroBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.initViewPager()
    }

    private fun FragmentIntroBinding.initViewPager() {
        viewPager.adapter = IntroViewPagerAdapter(introScreens, childFragmentManager, lifecycle)
        wormDotsIndicator.attachTo(viewPager)
    }

    override fun getTopView() = binding.root
    override fun getRootView() = binding.root
    override fun getBinding(): (inflater: LayoutInflater, root: ViewGroup?, attachToRoot: Boolean) -> FragmentIntroBinding {
        return FragmentIntroBinding::inflate
    }

    companion object {
        private val introScreens = listOf(
            IntroPagesData(
                text = R.string.welcome_to_kadchat,
                description = R.string.welcome_description,
                image = R.drawable.welcome_image
            ),
            IntroPagesData(
                text = R.string.sub_kadchat,
                description = R.string.sub_kadchat_description,
                image = R.drawable.sub_kadchat_image
            ),
            IntroPagesData(
                text = R.string.share_and_save,
                description = R.string.share_and_save_description,
                image = R.drawable.share_and_save_image,
                isLastPage = true
            )
        )
    }
}