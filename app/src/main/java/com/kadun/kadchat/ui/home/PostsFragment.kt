package com.kadun.kadchat.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.SimpleItemAnimator
import com.kadun.kadchat.R
import com.kadun.kadchat.common.InsetsWithBindingFragment
import com.kadun.kadchat.common.PostClickListener
import com.kadun.kadchat.data.db.entity.DbPostsData
import com.kadun.kadchat.data.extentions.isNotLoading
import com.kadun.kadchat.data.extentions.isRefreshLoading
import com.kadun.kadchat.data.extentions.showSnackbar
import com.kadun.kadchat.databinding.FragmentPostsBinding
import com.kadun.kadchat.ui.home.adapters.PostAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PostsFragment : InsetsWithBindingFragment<FragmentPostsBinding>() {

    private val args: PostsFragmentArgs by navArgs()
    private val viewModel: PostsViewModel by viewModel {
        parametersOf(args.displayNamePrefixed)
    }

    private val onItemClickListener = object : PostClickListener<DbPostsData> {

        override fun onCommentClicked(item: DbPostsData) {
            findNavController().navigate(
                PostsFragmentDirections.toCommentsFragment(
                    postId = item.id,
                    postTitle = item.title
                )
            )
        }

        override fun onVoteUpClicked(item: DbPostsData) {
            showSnackbar("Повысить рейтинг поста")
        }

        override fun onVoteDownClicked(item: DbPostsData) {
            showSnackbar("Снизить рейтинг поста!")
        }

        override fun onFavoriteClicked(item: DbPostsData) {
            viewModel.changePostFavoriteState(item)
        }
    }

    private val postAdapter by lazy {
        PostAdapter(clickListener = onItemClickListener)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.initViews()
        viewModel.initFlows()
    }

    private fun FragmentPostsBinding.initViews() {
        postsHeader.tvTitle.text = args.displayNamePrefixed
        rvPosts.apply {
            adapter = postAdapter
            setHasFixedSize(true)
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
        postsHeader.ivBack.setOnClickListener { findNavController().popBackStack() }
        postsHeader.ivInfo.setOnClickListener {
            showSnackbar(getString(R.string.post_screen_info, args.displayName))
        }
    }

    private fun PostsViewModel.initFlows() = viewLifecycleOwner.lifecycleScope.launch {
        launch {
            posts.collectLatest {
                postAdapter.submitData(it)
            }
        }
        launch {
            postAdapter.loadStateFlow.collectLatest {
                binding.progressBar.isVisible =
                    it.isRefreshLoading() && postAdapter.itemCount == 0
                binding.tvEmptyContainer.isVisible =
                    it.isNotLoading() && postAdapter.itemCount == 0
                binding.rvPosts.isVisible = binding.tvEmptyContainer.isVisible.not()
            }
        }
    }

    override fun getTopView() = null
    override fun getRootView() = binding.root
    override fun getBinding(): (inflater: LayoutInflater, root: ViewGroup?, attachToRoot: Boolean) -> FragmentPostsBinding =
        FragmentPostsBinding::inflate
}