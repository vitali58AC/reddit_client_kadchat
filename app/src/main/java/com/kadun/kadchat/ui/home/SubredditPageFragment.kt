package com.kadun.kadchat.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.SimpleItemAnimator
import com.kadun.kadchat.R
import com.kadun.kadchat.common.InsetsWithBindingFragment
import com.kadun.kadchat.common.ItemClickListener
import com.kadun.kadchat.common.aboveBottomNavigation
import com.kadun.kadchat.common.createDefaultSnackbar
import com.kadun.kadchat.data.db.entity.DbSubredditData
import com.kadun.kadchat.data.extentions.getParcelableSafe
import com.kadun.kadchat.data.extentions.withArguments
import com.kadun.kadchat.databinding.FragmentSubredditPageBinding
import com.kadun.kadchat.ui.home.adapters.SubredditAdapter
import com.kadun.kadchat.ui.home.data.SubredditsType
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SubredditPageFragment : InsetsWithBindingFragment<FragmentSubredditPageBinding>() {

    private val viewModel by viewModel<HomeViewModel> {
        parametersOf(requireArguments().getParcelableSafe<SubredditsType>(SUBREDDIT_TYPE))
    }
    private val onItemClickListener = object : ItemClickListener<DbSubredditData> {
        override fun onItemClicked(item: DbSubredditData) {
            viewModel.changeSubredditSubscribeState(item)
        }
    }
    private val subredditAdapter by lazy {
        SubredditAdapter(clickListener = onItemClickListener)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.initViews()
        viewModel.initFlows()
    }

    private fun FragmentSubredditPageBinding.initViews() {
        rvSubreddits.apply {
            adapter = subredditAdapter
            setHasFixedSize(true)
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }

    private fun HomeViewModel.initFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                posts.collectLatest {
                    subredditAdapter.submitData(it)
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

    private fun showSnackbar(message: String) {
        createDefaultSnackbar(message)
            .aboveBottomNavigation(requireActivity())
            .show()
    }

    override fun getTopView() = null
    override fun getRootView() = binding.root
    override fun getBinding(): (inflater: LayoutInflater, root: ViewGroup?, attachToRoot: Boolean) -> FragmentSubredditPageBinding =
        FragmentSubredditPageBinding::inflate

    companion object {

        private const val SUBREDDIT_TYPE = "title"

        fun newInstance(type: SubredditsType): SubredditPageFragment {
            return SubredditPageFragment().withArguments {
                putParcelable(SUBREDDIT_TYPE, type)
            }
        }
    }
}