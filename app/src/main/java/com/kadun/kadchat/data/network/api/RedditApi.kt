package com.kadun.kadchat.data.network.api

import com.kadun.kadchat.data.network.data.comments.CommentsDto
import com.kadun.kadchat.data.network.data.posts.PostDto
import com.kadun.kadchat.data.network.data.subreddit.RedditPagingRootDto
import com.kadun.kadchat.data.network.data.subreddit.SubredditsDto
import com.kadun.kadchat.data.network.data.users.FriendDto
import com.kadun.kadchat.data.network.data.users.UserDto
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RedditApi {

    @GET("subreddits/new")
    suspend fun getNewSubreddits(
        @Query("after") after: String?,
        @Query("count") count: Int?,
        @Query("limit") limit: Int?,
    ): RedditPagingRootDto<SubredditsDto>

    @GET("subreddits/popular")
    suspend fun getPopularSubreddits(
        @Query("after") after: String?,
        @Query("count") count: Int?,
        @Query("limit") limit: Int?,
    ): RedditPagingRootDto<SubredditsDto>

    @POST("api/subscribe")
    suspend fun changeSubredditSubscribeState(
        @Query("action") action: String,
        @Query("skip_initial_defaults") skip_initial_defaults: Boolean,
        @Query("sr_name") sr_name: String,
    ): ResponseBody

    @GET("api/me.json")
    suspend fun getMeJson(): ResponseBody

    @GET("api/v1/me")
    suspend fun getCurrentUserInfo(): UserDto

    @GET("api/v1/me/friends")
    suspend fun getCurrentUserFriends(
        @Query("after") after: String?,
        @Query("count") count: Int?,
        @Query("limit") limit: Int?,
    ): RedditPagingRootDto<FriendDto>

    @GET("r/subreddit/search")
    suspend fun searchSubredditPosts(@Query("q") name: String): ResponseBody

    @GET("{subReddit}/new")
    suspend fun getNewSubredditPosts(
        @Path("subReddit") fullName: String,
        @Query("after") after: String?,
        @Query("count") count: Int?,
        @Query("limit") limit: Int?
    ): RedditPagingRootDto<PostDto>

    @POST("api/vote")
    suspend fun changeThingVoteStatus(
        @Query("dir") direction: Int,
        @Query("id") fullName: String,
    ): ResponseBody

    @GET("comments/{id}")
    suspend fun getPostComments(
        @Path("id") id: String,
        @Query("after") after: String?,
        @Query("depth") depth: Int?,
        @Query("limit") limit: Int?,
    ): List<RedditPagingRootDto<CommentsDto>>
}


