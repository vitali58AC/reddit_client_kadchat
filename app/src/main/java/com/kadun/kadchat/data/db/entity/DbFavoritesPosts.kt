package com.kadun.kadchat.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kadun.kadchat.data.network.data.posts.MediaPreview

@Entity
data class DbFavoritesPosts(
    @PrimaryKey(autoGenerate = true)
    val sortingPosition: Long = 0,
    val id: String?,
    val subreddit: String?,
    val selftext: String?,
    val title: String?,
    val subreddit_name_prefixed: String?,
    val name: String?,
    val upvote_ratio: Double?,
    val ups: Int?,
    val score: Int?,
    val is_self: Boolean?,
    val created: Double?,
    val likes: Boolean?,
    val view_count: Int?,
    val subreddit_id: String?,
    val author: String?,
    val num_comments: Int?,
    val url: String?,
    val subreddit_subscribers: Int?,
    val thumbnail: String?,
    val url_overridden_by_dest: String?,
    val preview: MediaPreview?,
    val isVoted: Boolean? = null,
    val isFavorite: Boolean = false
)