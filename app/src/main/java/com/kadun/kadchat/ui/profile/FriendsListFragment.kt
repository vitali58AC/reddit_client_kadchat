package com.kadun.kadchat.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.SimpleItemAnimator
import com.kadun.kadchat.R
import com.kadun.kadchat.common.InsetsWithBindingFragment
import com.kadun.kadchat.data.extentions.isNotLoading
import com.kadun.kadchat.data.extentions.showSnackbar
import com.kadun.kadchat.databinding.FragmentFriendsListBinding
import com.kadun.kadchat.ui.profile.adapter.FriendsAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FriendsListFragment : InsetsWithBindingFragment<FragmentFriendsListBinding>() {

    private val viewModel: FriendsListViewModel by viewModel()

    private val friendsAdapter by lazy {
        FriendsAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.initViews()
        viewModel.initFlows()
    }

    private fun FragmentFriendsListBinding.initViews() {
        rvFriends.apply {
            adapter = friendsAdapter
            setHasFixedSize(true)
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
        friendsHeader.tvTitle.text = getString(R.string.friends_list)
        friendsHeader.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        friendsHeader.ivInfo.setOnClickListener {
            showSnackbar(getString(R.string.this_is_your_friends))
        }
    }

    private fun FriendsListViewModel.initFlows() = viewLifecycleOwner.lifecycleScope.launch {
        launch {
            friends.collectLatest {
                friendsAdapter.submitData(it)
            }
        }
        launch {
            friendsAdapter.loadStateFlow.collectLatest {
                binding.tvEmptyFriends.isVisible =
                    it.isNotLoading() && friendsAdapter.itemCount == 0
                binding.rvFriends.isVisible = binding.tvEmptyFriends.isVisible.not()
            }
        }
    }

    override fun getTopView() = null
    override fun getRootView() = binding.root
    override fun getBinding(): (inflater: LayoutInflater, root: ViewGroup?, attachToRoot: Boolean) -> FragmentFriendsListBinding =
        FragmentFriendsListBinding::inflate
}