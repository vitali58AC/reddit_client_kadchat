package com.kadun.kadchat.data.network.mediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.kadun.kadchat.data.db.RoomDaoDatabase
import com.kadun.kadchat.data.db.entity.DbCommentsData
import com.kadun.kadchat.data.db.entity.DbCommentsRemoteKeys
import com.kadun.kadchat.data.network.data.comments.CommentsDto
import com.kadun.kadchat.data.network.data.subreddit.DataDto
import com.kadun.kadchat.data.repositories.SubredditsRepositoryImpl
import com.kadun.kadchat.data.utils.suspendTransform

@OptIn(ExperimentalPagingApi::class)
class CommentsPagingMediator<P : Any>(
    private val postId: String,
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
                val remoteKey = db.getCommentsRemoteKeysDao().getRemoteKey()
                if (remoteKey?.isEndFeed == true) return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                remoteKey
            }
        }
        val response = subredditRepo.getPostComments(
            postId, cursor?.nextCursor, limit = COMMENTS_COUNT_LIMIT
        )
        return response.suspendTransform(throwableTransformation = {
            MediatorResult.Error(it)
        }) {
            if (loadType == LoadType.REFRESH) clearTables()
            saveData(it)
            MediatorResult.Success(endOfPaginationReached = true)
        }
    }

    private suspend fun clearTables() = db.withTransaction {
        db.getCommentsRemoteKeysDao().clearRemoteKeys()
        db.getCommentsDao().clearDataById(postId)
    }

    private suspend fun saveData(dto: List<DataDto<CommentsDto>>) = db.withTransaction {
        db.getCommentsRemoteKeysDao()
            .insert(
                DbCommentsRemoteKeys(
                    nextCursor = null, isEndFeed = true
                )
            )
        val newList = mutableListOf<DbCommentsData>()
        dto.map {
            it.children.filter { commentDto ->
                commentDto.data.body != null
            }.map { commentDto ->
                val savedFavoriteState = commentDto.data.id?.let { id ->
                    db.getFavoriteCommentsDao().getDbFavoriteComments(id)
                }?.isFavorite ?: false
                newList.add(DbCommentsData.fromDto(commentDto.data, savedFavoriteState, postId))
            }
        }
        db.getCommentsDao().insert(newList)
    }

    companion object {
        private const val COMMENTS_COUNT_LIMIT = 100
    }
}