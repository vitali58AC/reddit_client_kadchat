package com.kadun.kadchat.data.network.mediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.kadun.kadchat.data.db.RoomDaoDatabase
import com.kadun.kadchat.data.db.entity.DbFriendsData
import com.kadun.kadchat.data.db.entity.DbFriendsRemoteKeys
import com.kadun.kadchat.data.network.data.subreddit.DataDto
import com.kadun.kadchat.data.network.data.users.FriendDto
import com.kadun.kadchat.data.repositories.SubredditsRepositoryImpl
import com.kadun.kadchat.data.utils.suspendTransform

@OptIn(ExperimentalPagingApi::class)
class FriendsPagingMediator<P : Any>(
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
                val remoteKey = db.getFriendsRemoteKeysDao().getRemoteKey()
                if (remoteKey?.isEndFeed == true) return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                remoteKey
            }
        }
        val response =
            subredditRepo.getCurrentUserFriends(cursor?.nextCursor, state.config.pageSize)
        return response.suspendTransform(throwableTransformation = {
            MediatorResult.Error(it)
        }) {
            if (loadType == LoadType.REFRESH) clearTables()
            saveData(it)
            MediatorResult.Success(endOfPaginationReached = it.children.isEmpty() || it.after == null)
        }
    }

    private suspend fun clearTables() = db.withTransaction {
        db.getFriendsRemoteKeysDao().clearRemoteKeys()
        db.getFriendsDao().clearFriendsData()
    }

    private suspend fun saveData(dto: DataDto<FriendDto>) = db.withTransaction {
        db.getFriendsRemoteKeysDao()
            .insert(
                DbFriendsRemoteKeys(
                    nextCursor = dto.after, isEndFeed = dto.children.isEmpty()
                )
            )
        db.getFriendsDao().insertFriends(dto.children.map {
            DbFriendsData.fromDto(it)
        })
    }
}