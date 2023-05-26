package com.kadun.kadchat.data.repositories

import androidx.paging.*
import androidx.room.withTransaction
import com.kadun.kadchat.data.db.RoomDaoDatabase
import com.kadun.kadchat.data.db.entity.*
import com.kadun.kadchat.data.db.entity.DbCommentsData.Companion.toFavoriteData
import com.kadun.kadchat.data.db.entity.DbPostsData.Companion.toFavoriteData
import com.kadun.kadchat.data.db.entity.DbSubredditData.Companion.toFavoriteData
import com.kadun.kadchat.data.network.api.RedditApi
import com.kadun.kadchat.data.network.data.subreddit.SubscribeAction
import com.kadun.kadchat.data.network.mediators.CommentsPagingMediator
import com.kadun.kadchat.data.network.mediators.FriendsPagingMediator
import com.kadun.kadchat.data.network.mediators.PostsPagingMediator
import com.kadun.kadchat.data.network.mediators.SubredditPagingMediator
import com.kadun.kadchat.data.utils.onSuccess
import com.kadun.kadchat.data.utils.suspendCallForAppResult
import com.kadun.kadchat.ui.home.data.SubredditsType
import kotlinx.coroutines.flow.Flow

class SubredditsRepositoryImpl(
    private val api: RedditApi,
    private val db: RoomDaoDatabase
) : SubredditsRepository {

    override suspend fun getNewSubreddits(after: String?, count: Int?, limit: Int?) =
        suspendCallForAppResult {
            api.getNewSubreddits(after, count, limit).data
        }

    override suspend fun getPopularSubreddits(after: String?, count: Int?, limit: Int?) =
        suspendCallForAppResult {
            api.getPopularSubreddits(after, count, limit).data
        }

    override suspend fun getSubredditByType(
        type: SubredditsType,
        after: String?,
        count: Int?,
        limit: Int?
    ) = when (type) {
        SubredditsType.NEW -> getNewSubreddits(after, count, limit)
        SubredditsType.POPULAR -> getPopularSubreddits(after, count, limit)
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getSubredditFlow(type: SubredditsType): Flow<PagingData<DbSubredditData>> {
        val pagingSourceFactory = { db.getSubredditDao().getSubredditByType(type) }
        return Pager(
            config = getDefaultPageConfig(),
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = SubredditPagingMediator(db, this, type)
        ).flow
    }

    override suspend fun changeSubredditSubscribeState(
        action: SubscribeAction,
        displayName: String
    ) = suspendCallForAppResult {
        api.changeSubredditSubscribeState(
            action = action.value,
            skip_initial_defaults = action == SubscribeAction.SUBSCRIBE,
            sr_name = displayName
        )
    }.onSuccess {
        db.getSubredditDao().updateSubredditSubscribeState(
            isSubscribed = action == SubscribeAction.SUBSCRIBE,
            displayName = displayName
        )
    }

    override suspend fun getMeJson() =
        suspendCallForAppResult {
            api.getMeJson()
        }

    @OptIn(ExperimentalPagingApi::class)
    override fun getFriendsFlow(): Flow<PagingData<DbFriendsData>> {
        val pagingSourceFactory = { db.getFriendsDao().getFriends() }
        return Pager(
            config = getDefaultPageConfig(),
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = FriendsPagingMediator(db, this)
        ).flow
    }

    override suspend fun getCurrentUserFriends(
        after: String?, count: Int?, limit: Int?
    ) = suspendCallForAppResult {
        api.getCurrentUserFriends(null, 10, 10).data
    }

    override suspend fun changeSubredditExpandedState(id: String, state: Boolean) {
        db.getSubredditDao().updateSubredditExpandedState(id, state)
    }

    override suspend fun changeSubredditFavoriteState(id: String, state: Boolean) =
        db.withTransaction {
            db.getSubredditDao().updateSubredditFavoriteState(id, state)
            db.getFavoriteSubredditDao().updateSubredditFavoriteState(id, state)
            val oldEntity = db.getSubredditDao().getDbSubredditData(id)
            db.getFavoriteSubredditDao()
                .insertSubreddit(oldEntity.toFavoriteData().copy(isFavorite = state))
            if (state.not()) {
                db.getFavoriteSubredditDao().clearSubreddits()
            }
        }

    override suspend fun changePostFavoriteState(id: String, state: Boolean) = db.withTransaction {
        db.getPostsDao().updatePostFavoriteState(id, state)
        val oldEntity = db.getPostsDao().getDbPostData(id)
        db.getFavoritePostDao()
            .insert(oldEntity.toFavoriteData().copy(isFavorite = state))
        if (state.not()) {
            db.getFavoritePostDao().clearData()
        }
    }

    override suspend fun changeCommentFavoriteState(id: String, state: Boolean) =
        db.withTransaction {
            db.getCommentsDao().updateFavoriteState(id, state)
            val oldEntity = db.getCommentsDao().getDbCommentsData(id)
            db.getFavoriteCommentsDao()
                .insert(oldEntity.toFavoriteData().copy(isFavorite = state))
            if (state.not()) {
                db.getFavoriteCommentsDao().clearData()
            }
        }

    override suspend fun getNewSubredditPosts(
        nameWithPrefix: String, after: String?, count: Int?, limit: Int?
    ) = suspendCallForAppResult {
        api.getNewSubredditPosts(nameWithPrefix, after, count, limit).data
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getNewPostsFlow(subredditName: String): Flow<PagingData<DbPostsData>> {
        val pagingSourceFactory = { db.getPostsDao().getItems(subredditName) }
        return Pager(
            config = getDefaultPageConfig(),
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = PostsPagingMediator(subredditName, db, this)
        ).flow
    }

    override fun getFavoriteSubreddits(): Flow<List<DbFavoriteSubreddits>> {
        return db.getFavoriteSubredditDao().getSubredditsFavoriteList()
    }

    override fun getFavoritePosts(): Flow<List<DbFavoritesPosts>> {
        return db.getFavoritePostDao().getPostsFavoriteList()
    }

    override fun getFavoriteComments(): Flow<List<DbFavoritesComments>> {
        return db.getFavoriteCommentsDao().getFavoriteList()
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPostCommentsFlow(postId: String): Flow<PagingData<DbCommentsData>> {
        val pagingSourceFactory = { db.getCommentsDao().getItems(postId) }
        return Pager(
            config = getDefaultPageConfig(),
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = CommentsPagingMediator(postId, db, this)
        ).flow
    }

    override suspend fun getPostComments(id: String, after: String?, depth: Int?, limit: Int?) =
        suspendCallForAppResult {
            api.getPostComments(id, after, depth, limit).map {
                it.data
            }
        }

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(
            pageSize = DEFAULT_PAGE_SIZE,
            initialLoadSize = DEFAULT_PAGE_SIZE,
            enablePlaceholders = true,
            maxSize = MAX_CACHE_SIZE
        )
    }

    companion object {
        private const val DEFAULT_PAGE_SIZE = 10
        private const val MAX_CACHE_SIZE = 100
    }
}