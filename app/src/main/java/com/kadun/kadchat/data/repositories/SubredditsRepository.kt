package com.kadun.kadchat.data.repositories

import androidx.paging.PagingData
import com.kadun.kadchat.data.db.entity.*
import com.kadun.kadchat.data.network.data.comments.CommentsDto
import com.kadun.kadchat.data.network.data.posts.PostDto
import com.kadun.kadchat.data.network.data.subreddit.DataDto
import com.kadun.kadchat.data.network.data.subreddit.SubredditsDto
import com.kadun.kadchat.data.network.data.subreddit.SubscribeAction
import com.kadun.kadchat.data.network.data.users.FriendDto
import com.kadun.kadchat.data.utils.AppResult
import com.kadun.kadchat.ui.home.data.SubredditsType
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

interface SubredditsRepository {

    suspend fun getNewSubreddits(
        after: String?, count: Int?, limit: Int?
    ): AppResult<DataDto<SubredditsDto>>

    suspend fun getPopularSubreddits(
        after: String?, count: Int?, limit: Int?
    ): AppResult<DataDto<SubredditsDto>>

    suspend fun getSubredditByType(
        type: SubredditsType, after: String?, count: Int?, limit: Int? = 25
    ): AppResult<DataDto<SubredditsDto>>

    fun getSubredditFlow(type: SubredditsType): Flow<PagingData<DbSubredditData>>

    suspend fun changeSubredditSubscribeState(
        action: SubscribeAction,
        displayName: String
    ): AppResult<ResponseBody>

    suspend fun getMeJson(): AppResult<ResponseBody>

    fun getFriendsFlow(): Flow<PagingData<DbFriendsData>>

    suspend fun getCurrentUserFriends(
        after: String?,
        count: Int?,
        limit: Int? = 25
    ): AppResult<DataDto<FriendDto>>

    suspend fun changeSubredditExpandedState(id: String, state: Boolean)

    suspend fun changeSubredditFavoriteState(id: String, state: Boolean)

    suspend fun changePostFavoriteState(id: String, state: Boolean)

    suspend fun changeCommentFavoriteState(id: String, state: Boolean)

    suspend fun getNewSubredditPosts(
        nameWithPrefix: String, after: String?, count: Int?, limit: Int? = 25
    ): AppResult<DataDto<PostDto>>

    fun getNewPostsFlow(subredditName: String): Flow<PagingData<DbPostsData>>

    fun getFavoriteSubreddits(): Flow<List<DbFavoriteSubreddits>>

    fun getFavoritePosts(): Flow<List<DbFavoritesPosts>>

    fun getPostCommentsFlow(postId: String): Flow<PagingData<DbCommentsData>>

    suspend fun getPostComments(
        id: String, after: String?, depth: Int? = 2, limit: Int?
    ): AppResult<List<DataDto<CommentsDto>>>
}