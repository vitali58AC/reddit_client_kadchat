package com.kadun.kadchat.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kadun.kadchat.ui.home.data.SubredditsType

@Entity
data class DbSubredditRemoteKeys(
    @PrimaryKey val type: SubredditsType,
    val nextCursor: String?,
    val isEndFeed: Boolean
)