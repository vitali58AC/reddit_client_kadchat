package com.kadun.kadchat.data.repositories

import com.kadun.kadchat.data.network.api.RedditApi
import com.kadun.kadchat.data.utils.suspendCallForAppResult

class UserRepositoryImpl(
    private val api: RedditApi
): UserRepository {

    override suspend fun getCurrentUserInfo() = suspendCallForAppResult {
        api.getCurrentUserInfo()
    }
}