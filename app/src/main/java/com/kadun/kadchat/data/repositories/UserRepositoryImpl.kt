package com.kadun.kadchat.data.repositories

import androidx.room.withTransaction
import com.kadun.kadchat.data.db.RoomDaoDatabase
import com.kadun.kadchat.data.network.api.RedditApi
import com.kadun.kadchat.data.network.data.users.SetFriendInDto
import com.kadun.kadchat.data.utils.suspendCallForAppResult

class UserRepositoryImpl(
    private val api: RedditApi,
    private val db: RoomDaoDatabase
) : UserRepository {

    override suspend fun getCurrentUserInfo() = suspendCallForAppResult {
        api.getCurrentUserInfo()
    }

    override suspend fun clearFavorites() = db.withTransaction {
        db.getFavoriteSubredditDao().clearSubreddits()
        db.getFavoritePostDao().clearData()
        db.getFavoriteCommentsDao().clearData()
    }

    override suspend fun getUserInfo(author: String) =
        suspendCallForAppResult {
            api.getUserInfo(author)
        }

    override suspend fun changeUserIsFriendStatus(name: String, isFriend: Boolean) =
        suspendCallForAppResult {
            api.changeUserIsFriendStatus(
                name = name,
                SetFriendInDto(name = name, note = "Test note")
            )
        }
}