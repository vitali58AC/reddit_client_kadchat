package com.kadun.kadchat.data.network.data.comments

data class Comment(
    val replies: Any?,
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
    val ups: Int?
)
