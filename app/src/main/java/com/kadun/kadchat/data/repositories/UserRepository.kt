package com.kadun.kadchat.data.repositories

import com.kadun.kadchat.data.network.data.users.UserDto
import com.kadun.kadchat.data.utils.AppResult

interface UserRepository {

    suspend fun getCurrentUserInfo(): AppResult<UserDto>
}