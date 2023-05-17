package com.kadun.kadchat.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kadun.kadchat.data.db.entity.DbSubredditData
import com.kadun.kadchat.data.network.data.subreddit.SubscribeAction
import com.kadun.kadchat.data.repositories.SubredditsRepositories
import com.kadun.kadchat.data.utils.onFailure
import com.kadun.kadchat.data.utils.onSuccess
import com.kadun.kadchat.ui.home.data.SubredditsType
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    type: SubredditsType,
    private val subredditsRepo: SubredditsRepositories
) : ViewModel() {

    private val _subscribeStateFlow = MutableSharedFlow<Boolean>()
    val subscribeStateFlow: SharedFlow<Boolean> get() = _subscribeStateFlow

    private val _errorStateFlow = MutableSharedFlow<String?>()
    val errorStateFlow: SharedFlow<String?> get() = _errorStateFlow

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
        Log.e("HomeViewModel error", "e")
    }

    val posts = subredditsRepo.getSubredditFlow(type)
        .cachedIn(viewModelScope)

    fun changeSubredditSubscribeState(item: DbSubredditData) =
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val newState = if (item.user_is_subscriber == true) {
                SubscribeAction.UNSUBSCRIBE
            } else {
                SubscribeAction.SUBSCRIBE
            }
            item.display_name?.let { name ->
                subredditsRepo.changeSubredditSubscribeState(newState, name).onSuccess {
                    _subscribeStateFlow.emit(newState == SubscribeAction.SUBSCRIBE)
                }.onFailure {
                    _errorStateFlow.emit(it.message)
                }
            }
        }
}