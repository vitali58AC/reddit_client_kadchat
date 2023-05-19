package com.kadun.kadchat.data.network.data.subreddit

data class Subreddit(
    val id: String?,
    val name: String?,
    val title: String?,
    val display_name: String?,
    val display_name_prefixed: String?,
    val community_icon: String?,
    val created: Double?,
    val user_is_subscriber: Boolean?,
    val description: String?,
    val icon_img: String?,
    val header_img: String?,
    val subscribers: Int?,
    val public_description: String?,
    val banner_background_image: String?
)
