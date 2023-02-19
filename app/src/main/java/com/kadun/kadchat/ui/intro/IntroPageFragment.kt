package com.kadun.kadchat.ui.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation.fragment.findNavController
import com.kadun.kadchat.R
import com.kadun.kadchat.common.InsetsWithBindingFragment
import com.kadun.kadchat.data.extentions.withArguments
import com.kadun.kadchat.databinding.FragmentIntroPageBinding

class IntroPageFragment : InsetsWithBindingFragment<FragmentIntroPageBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.initViews()
    }

    private fun FragmentIntroPageBinding.initViews() {
        pageText.setText(requireArguments().getInt(KEY_TEXT))
        pageDescription.setText(requireArguments().getInt(KEY_DESCRIPTION))
        image.setImageResource(requireArguments().getInt(KEY_IMAGE))
        skipButton.text = if (requireArguments().getBoolean(KEY_LAST_PAGE, false)) {
            getString(R.string.Done)
        } else {
            getString(R.string.Skip)
        }
        skipButton.setOnClickListener {
            findNavController().navigate(IntroFragmentDirections.toHomeNavigation())
        }
    }

    override fun getTopView(): View? = null
    override fun getRootView(): View? = null
    override fun getBinding(): (inflater: LayoutInflater, root: ViewGroup?, attachToRoot: Boolean) -> FragmentIntroPageBinding {
        return FragmentIntroPageBinding::inflate
    }

    companion object {

        private const val KEY_TEXT = "title"
        private const val KEY_DESCRIPTION = "text"
        private const val KEY_IMAGE = "image"
        private const val KEY_LAST_PAGE = "is_last_page"

        fun newInstance(
            @StringRes text: Int,
            @StringRes description: Int,
            @DrawableRes image: Int,
            isLastPage: Boolean
        ): IntroPageFragment {
            return IntroPageFragment().withArguments {
                putInt(KEY_TEXT, text)
                putInt(KEY_DESCRIPTION, description)
                putInt(KEY_IMAGE, image)
                putBoolean(KEY_LAST_PAGE, isLastPage)
            }
        }
    }
}