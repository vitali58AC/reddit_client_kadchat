package com.kadun.kadchat.data.network.data.subreddit

data class DataDto(
    val after: String?,
    val dist: Int?,
    val children: List<SubredditsDto>
)
