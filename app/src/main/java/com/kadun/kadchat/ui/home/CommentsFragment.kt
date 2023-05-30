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
import com.kadun.kadchat.common.AnswerClickListener
import com.kadun.kadchat.common.CommentClickListener
import com.kadun.kadchat.common.InsetsWithBindingFragment
import com.kadun.kadchat.data.db.entity.DbCommentsData
import com.kadun.kadchat.data.extentions.isNotLoading
import com.kadun.kadchat.data.extentions.isRefreshLoading
import com.kadun.kadchat.data.extentions.showSnackbar
import com.kadun.kadchat.data.network.data.comments.CommentAnswers
import com.kadun.kadchat.databinding.FragmentCommentsBinding
import com.kadun.kadchat.ui.home.adapters.CommentAdapter
import com.kadun.kadchat.ui.home.adapters.CommentAnswersAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CommentsFragment : InsetsWithBindingFragment<FragmentCommentsBinding>() {

    private val args: CommentsFragmentArgs by navArgs()
    private val viewModel: CommentsViewModel by viewModel {
        parametersOf(args.postId)
    }

    private val onCommentClickListener = object : CommentClickListener<DbCommentsData> {

        override fun onFavoriteClick(item: DbCommentsData) {
            viewModel.changeCommentFavoriteState(item)
        }

        override fun onUsernameClick(item: DbCommentsData) {
            openUserFragment(item.author)
        }
    }

    private val onAnswerClickListener = object : AnswerClickListener<CommentAnswers> {

        override fun onUsernameClick(item: CommentAnswers) {
            openUserFragment(item.author)
        }
    }

    private val commentAdapter by lazy {
        CommentAdapter(
            clickListener = onCommentClickListener,
            answerAdapter = CommentAnswersAdapter(onAnswerClickListener)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.initViews()
        viewModel.initFlows()
    }

    private fun FragmentCommentsBinding.initViews() {
        commentsHeader.tvTitle.text = getString(R.string.comments)

        rvComments.apply {
            adapter = commentAdapter
            setHasFixedSize(true)
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }

        commentsHeader.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        commentsHeader.ivInfo.setOnClickListener {
            showSnackbar(getString(R.string.comments_list_for, args.postTitle))
        }
    }

    private fun CommentsViewModel.initFlows() = viewLifecycleOwner.lifecycleScope.launch {
        launch {
            comments.collectLatest {
                commentAdapter.submitData(it)
            }
        }
        launch {
            errorStateFlow.collectLatest {
                val message = it ?: getString(R.string.unknown_error_occurred)
                showSnackbar(message)
            }
        }
        launch {
            commentAdapter.loadStateFlow.collectLatest {
                binding.progressBar.isVisible =
                    it.isRefreshLoading() && commentAdapter.itemCount == 0
                binding.tvEmptyContainer.isVisible =
                    it.isNotLoading() && commentAdapter.itemCount == 0
                binding.rvComments.isVisible = binding.tvEmptyContainer.isVisible.not()
            }
        }
    }

    private fun openUserFragment(author: String?) {
        author ?: return
        findNavController().navigate(
            CommentsFragmentDirections.actionCommentsFragmentToUserFragment(
                author = author
            )
        )
    }


    override fun getTopView() = null
    override fun getRootView() = binding.root
    override fun getBinding(): (inflater: LayoutInflater, root: ViewGroup?, attachToRoot: Boolean) -> FragmentCommentsBinding =
        FragmentCommentsBinding::inflate
}