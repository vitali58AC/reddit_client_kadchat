package com.kadun.kadchat.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kadun.kadchat.data.db.entity.DbPostsData
import com.kadun.kadchat.data.repositories.SubredditsRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class PostsViewModel(
    subredditName: String,
    private val subredditsRepo: SubredditsRepository
): ViewModel() {

    private val _errorStateFlow = MutableSharedFlow<String?>()
    val errorStateFlow: SharedFlow<String?> get() = _errorStateFlow

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
        viewModelScope.launch { _errorStateFlow.emit(e.message) }
    }

    val posts = subredditsRepo.getNewPostsFlow(subredditName)
        .cachedIn(viewModelScope)

    fun changePostFavoriteState(item: DbPostsData) =
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            item.name?.let { subredditsRepo.changePostFavoriteState(it, item.isFavorite.not()) }
        }
}