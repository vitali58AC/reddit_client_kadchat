package com.kadun.kadchat.data.repositories

import com.kadun.kadchat.data.network.api.RedditApi
import com.kadun.kadchat.data.utils.suspendCallForAppResult

class SubredditsRepositoriesImpl(
    private val api: RedditApi
) : SubredditsRepositories {

    override suspend fun getNewSubreddits() = suspendCallForAppResult {
        api.getNewSubreddits()
    }
}