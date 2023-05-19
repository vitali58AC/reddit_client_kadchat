package com.kadun.kadchat.data.network.data.subreddit

data class RedditPagingRootDto<T>(
    val kind: String,
    val data: DataDto<T>
)