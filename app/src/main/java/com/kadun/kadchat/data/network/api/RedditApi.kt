package com.kadun.kadchat.data.network.api

import com.kadun.kadchat.data.network.data.subreddit.SubredditsRootDto
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RedditApi {

    @GET("subreddits/new")
    suspend fun getNewSubreddits(
        @Query("after") after: String?,
        @Query("count") count: Int?,
        @Query("limit") limit: Int?,
    ): SubredditsRootDto

    @GET("subreddits/popular")
    suspend fun getPopularSubreddits(
        @Query("after") after: String?,
        @Query("count") count: Int?,
        @Query("limit") limit: Int?,
    ): SubredditsRootDto

    @POST("api/subscribe")
    suspend fun changeSubredditSubscribeState(
        @Query("action") action: String,
        @Query("skip_initial_defaults") skip_initial_defaults: Boolean,
        @Query("sr_name") sr_name: String,
    ): ResponseBody

    @GET("api/me.json")
    suspend fun getMeJson(): ResponseBody
}
