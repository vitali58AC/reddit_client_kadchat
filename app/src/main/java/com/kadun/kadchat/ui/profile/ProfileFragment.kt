package com.kadun.kadchat.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.kadun.kadchat.MainActivity
import com.kadun.kadchat.R
import com.kadun.kadchat.common.InsetsWithBindingFragment
import com.kadun.kadchat.common.aboveBottomNavigation
import com.kadun.kadchat.common.createDefaultSnackbar
import com.kadun.kadchat.data.network.RedditAuthClient.Companion.fixRedditImageLink
import com.kadun.kadchat.databinding.FragmentProfileBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : InsetsWithBindingFragment<FragmentProfileBinding>() {

    private val viewModel: ProfileViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.initViews()
        viewModel.initFlows()
    }

    private fun FragmentProfileBinding.initViews() {
        flFriendsList.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.toFriendsListFragment())
        }
        flClearFavorite.setOnClickListener {
            showSnackbar("Clear favorites")
        }
        flLogout.setOnClickListener { viewModel.logout() }
    }

    private fun ProfileViewModel.initFlows() = viewLifecycleOwner.lifecycleScope.launch {
        launch {
            currentUserSharedFlow.collectLatest { user ->
                user ?: return@collectLatest
                with(binding) {
                    tvName.text = user.name
                    tvUserName.text = user.id
                    tvCommentsCount.text = getString(R.string.comments_count, user.inbox_count)
                    tvSubredditsCount.text =
                        getString(R.string.karma_count, user.total_karma)
                    loadImage(user.icon_img)
                }
            }
        }
        launch {
            errorStateFlow.collectLatest {
                val message = it ?: getString(R.string.unknown_error_occurred)
                showSnackbar(message)
            }
        }
        launch {
            logoutResult.collectLatest {
                val intent = Intent(context, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                (context as? Activity)?.finish()
                context?.startActivity(intent)
            }
        }
    }

    private fun showSnackbar(message: String) {
        createDefaultSnackbar(message)
            .aboveBottomNavigation(requireActivity())
            .show()
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

    override fun getTopView() = null
    override fun getRootView() = binding.root
    override fun getBinding(): (inflater: LayoutInflater, root: ViewGroup?, attachToRoot: Boolean) -> FragmentProfileBinding =
        FragmentProfileBinding::inflate
}