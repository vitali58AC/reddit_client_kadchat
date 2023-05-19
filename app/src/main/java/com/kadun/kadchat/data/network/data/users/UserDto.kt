package com.kadun.kadchat.data.network.data.users

import com.kadun.kadchat.data.network.data.subreddit.Subreddit

data class UserDto(
    val id: String?,
    val name: String?,
    val icon_img: String?,
    val total_karma: Int?,
    val inbox_count: Int?,
    val subreddit: Subreddit
)
