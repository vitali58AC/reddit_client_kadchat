package com.kadun.kadchat.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DbCommentsRemoteKeys(
    @PrimaryKey val id: Int = 0,
    val nextCursor: String?,
    val isEndFeed: Boolean
)
