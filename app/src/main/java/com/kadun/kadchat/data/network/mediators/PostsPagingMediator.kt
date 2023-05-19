package com.kadun.kadchat.data.network.mediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.kadun.kadchat.data.db.RoomDaoDatabase
import com.kadun.kadchat.data.db.entity.DbPostsData
import com.kadun.kadchat.data.db.entity.DbPostsRemoteKeys
import com.kadun.kadchat.data.network.data.posts.PostDto
import com.kadun.kadchat.data.network.data.subreddit.DataDto
import com.kadun.kadchat.data.repositories.SubredditsRepositoryImpl
import com.kadun.kadchat.data.utils.suspendTransform

@OptIn(ExperimentalPagingApi::class)
class PostsPagingMediator<P : Any>(
    private val subredditName: String,
    private val db: RoomDaoDatabase,
    private val subredditRepo: SubredditsRepositoryImpl
) : RemoteMediator<Int, P>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, P>
    ): MediatorResult {
        val cursor = when (loadType) {
            LoadType.REFRESH -> null
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val remoteKey = db.getPostsRemoteKeysDao().getRemoteKey()
                if (remoteKey?.isEndFeed == true) return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                remoteKey
            }
        }
        val response = subredditRepo.getNewSubredditPosts(
            subredditName, cursor?.nextCursor, state.config.pageSize
        )
        return response.suspendTransform(throwableTransformation = {
            MediatorResult.Error(it)
        }) {
            if (loadType == LoadType.REFRESH) clearTables()
            saveData(it)
            MediatorResult.Success(endOfPaginationReached = it.children.isEmpty() || it.after == null)
        }
    }

    private suspend fun clearTables() = db.withTransaction {
        db.getPostsRemoteKeysDao().clearRemoteKeys()
        db.getPostsDao().clearData()
    }

    private suspend fun saveData(dto: DataDto<PostDto>) = db.withTransaction {
        db.getPostsRemoteKeysDao()
            .insert(
                DbPostsRemoteKeys(
                    nextCursor = dto.after, isEndFeed = dto.children.isEmpty()
                )
            )
        db.getPostsDao().insert(dto.children
            .filter { it.data.title != null }
            .map {
                DbPostsData.fromDto(it.data)
            })
    }
}