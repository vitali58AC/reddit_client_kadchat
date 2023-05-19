package com.kadun.kadchat.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.SimpleItemAnimator
import com.kadun.kadchat.R
import com.kadun.kadchat.common.InsetsWithBindingFragment
import com.kadun.kadchat.common.PostClickListener
import com.kadun.kadchat.data.db.entity.DbPostsData
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
            //Обработать клики
        }

        override fun onVoteUpClicked(item: DbPostsData) {
            TODO("Not yet implemented")
        }

        override fun onVoteDownClicked(item: DbPostsData) {
            TODO("Not yet implemented")
        }

        override fun onFavoriteClicked(item: DbPostsData) {
            TODO("Not yet implemented")
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
    }

    override fun getTopView() = null
    override fun getRootView() = binding.root
    override fun getBinding(): (inflater: LayoutInflater, root: ViewGroup?, attachToRoot: Boolean) -> FragmentPostsBinding =
        FragmentPostsBinding::inflate
}