package com.kadun.kadchat.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.SimpleItemAnimator
import com.kadun.kadchat.R
import com.kadun.kadchat.common.AnswerClickListener
import com.kadun.kadchat.common.CommentClickListener
import com.kadun.kadchat.common.InsetsWithBindingFragment
import com.kadun.kadchat.common.PostClickListener
import com.kadun.kadchat.common.SubredditClickListener
import com.kadun.kadchat.data.db.entity.DbFavoriteSubreddits
import com.kadun.kadchat.data.db.entity.DbFavoritesComments
import com.kadun.kadchat.data.db.entity.DbFavoritesPosts
import com.kadun.kadchat.data.extentions.getParcelableSafe
import com.kadun.kadchat.data.extentions.showSnackbar
import com.kadun.kadchat.data.extentions.withArguments
import com.kadun.kadchat.data.network.data.comments.CommentAnswers
import com.kadun.kadchat.data.network.data.subreddit.SubscribeAction
import com.kadun.kadchat.databinding.FragmentFavoritePageBinding
import com.kadun.kadchat.ui.favorite.adapters.CommentListAdapter
import com.kadun.kadchat.ui.favorite.adapters.PostsListAdapter
import com.kadun.kadchat.ui.favorite.adapters.SubredditListAdapter
import com.kadun.kadchat.ui.favorite.data.FavoriteType
import com.kadun.kadchat.ui.home.adapters.CommentAnswersAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FavoritePageFragment : InsetsWithBindingFragment<FragmentFavoritePageBinding>() {

    private val viewModel by viewModel<FavoriteViewModel> {
        parametersOf(requireArguments().getParcelableSafe<FavoriteType>(FAVORITE_TYPE))
    }
    private val onSubredditClickListener = object : SubredditClickListener<DbFavoriteSubreddits> {
        override fun onSubscribeClicked(item: DbFavoriteSubreddits) {
            val newState = if (item.user_is_subscriber == true) {
                SubscribeAction.UNSUBSCRIBE
            } else {
                SubscribeAction.SUBSCRIBE
            }
            viewModel.changeSubredditSubscribeState(newState, item.display_name)
        }

        override fun onFavoriteClicked(item: DbFavoriteSubreddits) {
            viewModel.changeSubredditFavoriteState(item.name, item.isFavorite)
        }

        override fun onRootClicked(item: DbFavoriteSubreddits) {
            viewModel.changeSubredditExpandState(item.name, item.isExpanded)
        }

        override fun onOpenSubredditPosts(item: DbFavoriteSubreddits) {
            (item.display_name_prefixed ?: item.display_name)?.let {
                findNavController().navigate(
                    FavoriteFragmentDirections.actionNavigationFavoriteToPostsFragment(
                        displayName = item.display_name,
                        displayNamePrefixed = it
                    )
                )
            } ?: showSnackbar("Произошла неизвестная ошибка")
        }
    }

    private val onPostClickListener = object : PostClickListener<DbFavoritesPosts> {

        override fun onCommentClicked(item: DbFavoritesPosts) {
            showSnackbar("В комментарии")
        }

        override fun onVoteUpClicked(item: DbFavoritesPosts) {
            showSnackbar("Повысить рейтинг поста")
        }

        override fun onVoteDownClicked(item: DbFavoritesPosts) {
            showSnackbar("Снизить рейтинг поста!")
        }

        override fun onFavoriteClicked(item: DbFavoritesPosts) {
            viewModel.changePostFavoriteState(item)
        }
    }

    private val onCommentClickListener = object : CommentClickListener<DbFavoritesComments> {

        override fun onFavoriteClick(item: DbFavoritesComments) {
            showSnackbar("Add to favorite")
        }

        override fun onUsernameClick(item: DbFavoritesComments) {
            openUserFragment(item.author)
        }
    }

    private val onAnswerClickListener = object : AnswerClickListener<CommentAnswers> {

        override fun onUsernameClick(item: CommentAnswers) {
            openUserFragment(item.author)
        }
    }

    private val subredditAdapter by lazy {
        SubredditListAdapter(clickListener = onSubredditClickListener)
    }

    private val postAdapter by lazy {
        PostsListAdapter(clickListener = onPostClickListener)
    }

    private val commentAdapter by lazy {
        CommentListAdapter(
            clickListener = onCommentClickListener,
            answerAdapter = CommentAnswersAdapter(onAnswerClickListener)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.initViews()
        viewModel.initFlows()
    }

    private fun FragmentFavoritePageBinding.initViews() {
        val currentType =
            requireArguments().getParcelableSafe<FavoriteType>(FAVORITE_TYPE) ?: return
        rvItems.apply {
            val currentAdapter =
                when (currentType) {
                    FavoriteType.SUBREDDITS -> subredditAdapter
                    FavoriteType.POSTS -> postAdapter
                    FavoriteType.COMMENTS -> commentAdapter
                }
            adapter = currentAdapter
            setHasFixedSize(true)
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }

    private fun FavoriteViewModel.initFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.getFavoriteSubreddits().collectLatest {
                    subredditAdapter.submitList(it)
                }
            }
            launch {
                viewModel.getFavoritePosts().collectLatest {
                    postAdapter.submitList(it)
                }
            }
            launch {
                viewModel.getFavoriteComments().collectLatest {
                    commentAdapter.submitList(it)
                }
            }
            launch {
                subscribeStateFlow.collectLatest {
                    val message =
                        getString(if (it) R.string.success_subscribe else R.string.success_unsubscribe)
                    showSnackbar(message)
                }
            }
            launch {
                errorStateFlow.collectLatest {
                    val message = it ?: getString(R.string.unknown_error_occurred)
                    showSnackbar(message)
                }
            }
        }
    }

    private fun openUserFragment(author: String?) {
        author ?: return
        findNavController().navigate(
            FavoriteFragmentDirections.toUserFragment(
                author = author
            )
        )
    }

    override fun getTopView() = null
    override fun getRootView() = binding.root
    override fun getBinding(): (inflater: LayoutInflater, root: ViewGroup?, attachToRoot: Boolean) -> FragmentFavoritePageBinding =
        FragmentFavoritePageBinding::inflate

    companion object {

        private const val FAVORITE_TYPE = "favorite_type"

        fun newInstance(type: FavoriteType): FavoritePageFragment {
            return FavoritePageFragment().withArguments {
                putParcelable(FAVORITE_TYPE, type)
            }
        }
    }
}