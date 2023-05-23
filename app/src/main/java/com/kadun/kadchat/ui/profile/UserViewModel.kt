package com.kadun.kadchat.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kadun.kadchat.data.network.data.users.Data
import com.kadun.kadchat.data.repositories.UserRepository
import com.kadun.kadchat.data.utils.onSuccess
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class UserViewModel(
    author: String?, private val userRepo: UserRepository
) : ViewModel() {

    private val _errorStateFlow = MutableSharedFlow<String?>()
    val errorStateFlow: SharedFlow<String?> get() = _errorStateFlow

    private val _userInfoFlow = MutableSharedFlow<Data>()
    val userInfoFlow: SharedFlow<Data> get() = _userInfoFlow

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
        viewModelScope.launch { _errorStateFlow.emit(e.message) }
    }

    init {
        author?.let { getUserInfo(author) }
    }

    private fun getUserInfo(author: String) =
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            userRepo.getUserInfo(author).onSuccess {
                it.data?.let { data ->
                    _userInfoFlow.emit(data)
                }
            }
        }
}