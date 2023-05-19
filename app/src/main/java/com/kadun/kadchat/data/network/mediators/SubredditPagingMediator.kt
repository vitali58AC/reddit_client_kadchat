package com.kadun.kadchat.data.network.mediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.kadun.kadchat.data.db.RoomDaoDatabase
import com.kadun.kadchat.data.db.entity.DbSubredditData
import com.kadun.kadchat.data.db.entity.DbSubredditRemoteKeys
import com.kadun.kadchat.data.network.data.subreddit.DataDto
import com.kadun.kadchat.data.network.data.subreddit.SubredditsDto
import com.kadun.kadchat.data.repositories.SubredditsRepositoryImpl
import com.kadun.kadchat.data.utils.suspendTransform
import com.kadun.kadchat.ui.home.data.SubredditsType

@OptIn(ExperimentalPagingApi::class)
class SubredditPagingMediator<P : Any>(
    private val db: RoomDaoDatabase,
    private val subredditRepo: SubredditsRepositoryImpl,
    private val type: SubredditsType,
) : RemoteMediator<Int, P>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, P>
    ): MediatorResult {
        val cursor = when (loadType) {
            LoadType.REFRESH -> null
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val remoteKey = db.getSubredditRemoteKeysDao().getRemoteKey(type)
                if (remoteKey?.isEndFeed == true) return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                remoteKey
            }
        }
        val response =
            subredditRepo.getSubredditByType(type, cursor?.nextCursor, state.config.pageSize)
        return response.suspendTransform(throwableTransformation = {
            MediatorResult.Error(it)
        }) {
            if (loadType == LoadType.REFRESH) clearTables()
            saveData(it)
            MediatorResult.Success(endOfPaginationReached = it.children.isEmpty() || it.after == null)
        }
    }

    private suspend fun clearTables() = db.withTransaction {
        db.getSubredditRemoteKeysDao().clearRemoteKeys(type)
        db.getSubredditDao().clearSubredditDataByType(type)
    }

    private suspend fun saveData(dto: DataDto<SubredditsDto>) = db.withTransaction {
        db.getSubredditRemoteKeysDao()
            .insert(
                DbSubredditRemoteKeys(
                    type, dto.after, dto.children.isEmpty()
                )
            )
        db.getSubredditDao().insertSubreddit(dto.children.map {
            DbSubredditData.fromDto(it, type)
        })
    }
}