package com.kadun.kadchat.data.network.api

import com.kadun.kadchat.data.network.data.SubredditsRootDto
import retrofit2.http.GET
import retrofit2.http.Query

interface RedditApi {

    @GET("subreddits/new.json")
    suspend fun getNewSubreddits(
        @Query("after") after: String?,
        @Query("count") count: Int?,
        @Query("limit") limit: Int?,
    ): SubredditsRootDto

    @GET("subreddits/popular.json")
    suspend fun getPopularSubreddits(
        @Query("after") after: String?,
        @Query("count") count: Int?,
        @Query("limit") limit: Int?,
    ): SubredditsRootDto



}
