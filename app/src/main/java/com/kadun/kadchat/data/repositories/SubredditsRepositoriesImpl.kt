package com.kadun.kadchat.data.repositories

import androidx.paging.*
import com.kadun.kadchat.data.db.RoomDaoDatabase
import com.kadun.kadchat.data.db.entity.DbSubredditData
import com.kadun.kadchat.data.network.api.RedditApi
import com.kadun.kadchat.data.network.mediators.SubredditPagingMediator
import com.kadun.kadchat.data.utils.suspendCallForAppResult
import com.kadun.kadchat.ui.home.data.SubredditsType
import kotlinx.coroutines.flow.Flow

class SubredditsRepositoriesImpl(
    private val api: RedditApi,
    private val db: RoomDaoDatabase
) : SubredditsRepositories {

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