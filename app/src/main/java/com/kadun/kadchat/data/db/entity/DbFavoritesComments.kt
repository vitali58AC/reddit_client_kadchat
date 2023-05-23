package com.kadun.kadchat.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kadun.kadchat.data.network.data.comments.CommentAnswers

@Entity
data class DbFavoritesComments(
    @PrimaryKey(autoGenerate = true)
    val sortingPosition: Long = 0,
    val replies: List<CommentAnswers>?,
    val id: String?,
    val author: String?,
    val created_utc: Double?,
    val parent_id: String?,
    val score: Int?,
    val author_fullname: String?,
    val body: String?,
    val name: String?,
    val downs: Int?,
    val created: Double?,
    val link_id: String?,
    val subreddit_name_prefixed: String?,
    val depth: Int?,
    val ups: Int?,
    val isFavorite: Boolean = false
)