package com.kadun.kadchat.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kadun.kadchat.data.repositories.SubredditsRepository

class PostsViewModel(
    subredditName: String,
    private val subredditsRepo: SubredditsRepository
): ViewModel() {


    val posts = subredditsRepo.getNewPostsFlow(subredditName)
        .cachedIn(viewModelScope)
}