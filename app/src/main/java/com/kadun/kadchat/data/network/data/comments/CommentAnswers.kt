package com.kadun.kadchat.data.network.data.comments

data class CommentAnswers(
    val id: String?,
    val author: String?,
    val parent_id: String?,
    val created_utc: Double?,
    val score: Int?,
    val author_fullname: String?,
    val body: String?,
    val name: String?,
    val downs: Int?,
    val created: Double?,
    val subreddit_name_prefixed: String?,
    val depth: Int?,
    val ups: Int?
)