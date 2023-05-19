package com.kadun.kadchat.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kadun.kadchat.data.db.entity.DbFavoritesPosts
import com.kadun.kadchat.data.network.data.subreddit.SubscribeAction
import com.kadun.kadchat.data.repositories.SubredditsRepository
import com.kadun.kadchat.data.utils.onFailure
import com.kadun.kadchat.data.utils.onSuccess
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val subredditsRepo: SubredditsRepository
) : ViewModel() {

    private val _subscribeStateFlow = MutableSharedFlow<Boolean>()
    val subscribeStateFlow: SharedFlow<Boolean> get() = _subscribeStateFlow

    private val _errorStateFlow = MutableSharedFlow<String?>()
    val errorStateFlow: SharedFlow<String?> get() = _errorStateFlow

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
        viewModelScope.launch { _errorStateFlow.emit(e.message) }
    }

    fun changeSubredditSubscribeState(action: SubscribeAction, displayName: String?) =
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            displayName?.let { name ->
                subredditsRepo.changeSubredditSubscribeState(action, name).onSuccess {
                    _subscribeStateFlow.emit(action == SubscribeAction.SUBSCRIBE)
                }.onFailure {
                    _errorStateFlow.emit(it.message)
                }
            }
        }

    fun changeSubredditExpandState(id: String?, isExpanded: Boolean) =
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            id?.let { subredditsRepo.changeSubredditExpandedState(it, isExpanded.not()) }
        }

    fun changeSubredditFavoriteState(id: String?, isFavorite: Boolean) =
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            id?.let { subredditsRepo.changeSubredditFavoriteState(it, isFavorite.not()) }
        }

    fun getFavoriteSubreddits() = subredditsRepo.getFavoriteSubreddits()
    fun getFavoritePosts() = subredditsRepo.getFavoritePosts()

    fun changePostFavoriteState(item: DbFavoritesPosts) =
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            item.name?.let { subredditsRepo.changePostFavoriteState(it, item.isFavorite.not()) }
        }

}