package com.kadun.kadchat.data.network.data.subreddit

data class DataDto<T>(
    val after: String?,
    val dist: Int?,
    val children: List<T>
)
