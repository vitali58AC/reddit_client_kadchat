package com.kadun.kadchat.data.repositories

import com.kadun.kadchat.data.network.data.users.RedditUserDto
import com.kadun.kadchat.data.network.data.users.UserDto
import com.kadun.kadchat.data.utils.AppResult

interface UserRepository {

    suspend fun getCurrentUserInfo(): AppResult<UserDto>

    suspend fun clearFavorites()

    suspend fun getUserInfo(author: String): AppResult<RedditUserDto>

}