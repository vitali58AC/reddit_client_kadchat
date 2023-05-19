package com.kadun.kadchat.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kadun.kadchat.ui.home.data.SubredditsType

@Entity
data class DbFriendsRemoteKeys(
    @PrimaryKey val id: Int = 0,
    val nextCursor: String?,
    val isEndFeed: Boolean
)
