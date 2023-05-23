package com.kadun.kadchat.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.kadun.kadchat.R
import com.kadun.kadchat.common.InsetsWithBindingFragment
import com.kadun.kadchat.data.extentions.loadImage
import com.kadun.kadchat.data.extentions.showSnackbar
import com.kadun.kadchat.data.network.RedditAuthClient.Companion.fixRedditImageLink
import com.kadun.kadchat.data.network.data.users.Subreddit
import com.kadun.kadchat.databinding.FragmentUserBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class UserFragment : InsetsWithBindingFragment<FragmentUserBinding>() {

    private val args: UserFragmentArgs by navArgs()
    private val viewModel: UserViewModel by viewModel {
        parametersOf(args.author)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initFlows()
    }

    private fun UserViewModel.initFlows() = viewLifecycleOwner.lifecycleScope.launch {
        launch {
            userInfoFlow.collectLatest { user ->
                with(binding) {
                    tvName.text = user.name
                    tvUserName.text = user.id
                    tvCommentsCount.text =
                        getString(R.string.comments_karma_count, user.comment_karma)
                    tvSubredditsCount.text =
                        getString(R.string.karma_count, user.total_karma)
                    loadImage(user.icon_img)
                    setupSubreddit(user.subreddit)
                }
            }
        }
        launch {
            errorStateFlow.collectLatest {
                val message = it ?: getString(R.string.unknown_error_occurred)
                showSnackbar(message)
            }
        }
    }

    private fun loadImage(url: String?) {
        Glide.with(binding.root.context.applicationContext)
            .apply {
                if (url == null) {
                    clear(binding.ivAvatar)
                    binding.ivAvatar.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireContext(),
                            R.drawable.ic_launcher_drawable_list
                        )
                    )
                } else {
                    load(url.fixRedditImageLink())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ic_user_avatar_placeholder_90dp)
                        .transform(CenterCrop())
                        .into(binding.ivAvatar)
                }
            }
    }

    private fun FragmentUserBinding.setupSubreddit(subreddit: Subreddit?) {
        tvTitle.text = subreddit?.title
        ivCommunityIcon.isVisible = subreddit?.community_icon?.isEmpty()?.not() == true
        subreddit?.community_icon?.let { ivCommunityIcon.loadImage(it) }

        ivImage.isVisible = subreddit?.header_img.isNullOrEmpty().not()
        subreddit?.header_img?.let { ivImage.loadImage(it) }

        tvDescription.isVisible = subreddit?.public_description.isNullOrEmpty().not()
        tvDescription.text = subreddit?.public_description

        tvEmptyData.isVisible = tvDescription.isVisible.not() && ivImage.isVisible.not()

    }

    override fun getTopView() = null
    override fun getRootView() = binding.root
    override fun getBinding(): (inflater: LayoutInflater, root: ViewGroup?, attachToRoot: Boolean) -> FragmentUserBinding =
        FragmentUserBinding::inflate
}