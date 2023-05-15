package com.kadun.kadchat.data.network.api

import okhttp3.ResponseBody
import retrofit2.http.GET

interface RedditApi {

    @GET("subreddits/new")
    suspend fun getNewSubreddits(): ResponseBody

}
