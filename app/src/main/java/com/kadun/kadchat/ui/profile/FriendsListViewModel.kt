package com.kadun.kadchat.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kadun.kadchat.data.repositories.SubredditsRepository

class FriendsListViewModel(
    subredditsRepo: SubredditsRepository
) : ViewModel() {

    val friends = subredditsRepo.getFriendsFlow()
        .cachedIn(viewModelScope)
}