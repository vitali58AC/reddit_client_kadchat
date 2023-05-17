package com.kadun.kadchat.data.repositories

import androidx.paging.PagingData
import com.kadun.kadchat.data.db.entity.DbSubredditData
import com.kadun.kadchat.data.network.data.subreddit.DataDto
import com.kadun.kadchat.data.network.data.subreddit.SubscribeAction
import com.kadun.kadchat.data.utils.AppResult
import com.kadun.kadchat.ui.home.data.SubredditsType
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

interface SubredditsRepositories {

    suspend fun getNewSubreddits(
        after: String?, count: Int?, limit: Int?
    ): AppResult<DataDto>

    suspend fun getPopularSubreddits(
        after: String?, count: Int?, limit: Int?
    ): AppResult<DataDto>

    suspend fun getSubredditByType(
        type: SubredditsType, after: String?, count: Int?, limit: Int? = 25
    ): AppResult<DataDto>

    fun getSubredditFlow(type: SubredditsType): Flow<PagingData<DbSubredditData>>

    suspend fun changeSubredditSubscribeState(
        action: SubscribeAction,
        displayName: String
    ): AppResult<ResponseBody>

    suspend fun getMeJson(): AppResult<ResponseBody>
}