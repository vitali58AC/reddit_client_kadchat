package com.kadun.kadchat.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kadun.kadchat.common.LOGOUT_KEY
import com.kadun.kadchat.common.UserSettingPrefs
import com.kadun.kadchat.data.network.data.users.UserDto
import com.kadun.kadchat.data.repositories.UserRepository
import com.kadun.kadchat.data.utils.onFailure
import com.kadun.kadchat.data.utils.onSuccess
import com.kirkbushman.auth.RedditAuth
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepo: UserRepository,
    private val authClient: RedditAuth,
    private val userSettingPrefs: UserSettingPrefs
) : ViewModel() {

    private val _currentUserSharedFlow = MutableStateFlow<UserDto?>(null)
    val currentUserSharedFlow: StateFlow<UserDto?> get() = _currentUserSharedFlow

    private val _errorStateFlow = MutableSharedFlow<String?>()
    val errorStateFlow: SharedFlow<String?> get() = _errorStateFlow

    private val _logoutResult = MutableSharedFlow<Unit>()
    val logoutResult: SharedFlow<Unit> get() = _logoutResult

    private val _loadingStateFlow = MutableSharedFlow<Boolean>()
    val loadingStateFlow: SharedFlow<Boolean> get() = _loadingStateFlow

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
        viewModelScope.launch { _errorStateFlow.emit(e.message) }
    }

    init {
        getProfileData()
    }

    private fun getProfileData() =
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            userRepo.getCurrentUserInfo().onSuccess {
                _loadingStateFlow.emit(false)
                _currentUserSharedFlow.emit(it)
            }.onFailure {
                _loadingStateFlow.emit(false)
                _errorStateFlow.emit(it.message)
            }
        }

    fun logout() =
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            try {
                authClient.getTokenBearer()?.revokeToken()
                userSettingPrefs.putValue(LOGOUT_KEY)
            } finally {
                _logoutResult.emit(Unit)
            }
        }

    fun clearFavorites() = viewModelScope.launch(Dispatchers.IO) {
        userRepo.clearFavorites()
    }
}