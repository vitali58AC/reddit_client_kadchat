package com.kadun.kadchat.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kadun.kadchat.data.repositories.SubredditsRepositories
import com.kadun.kadchat.data.utils.onFailure
import com.kadun.kadchat.data.utils.onSuccess
import com.kadun.kadchat.ui.home.data.SubredditsType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val type: SubredditsType,
    private val subredditsRepo: SubredditsRepositories
) : ViewModel() {

    val posts = subredditsRepo.getSubredditFlow(type)
        .cachedIn(viewModelScope)

}