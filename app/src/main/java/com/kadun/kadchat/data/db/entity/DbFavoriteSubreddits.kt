package com.kadun.kadchat.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kadun.kadchat.ui.home.data.SubredditsType

@Entity
data class DbFavoriteSubreddits(
    @PrimaryKey(autoGenerate = true)
    val sortingPosition: Long = 0,
    val id: String?,
    val name: String?,
    val title: String?,
    val display_name: String?,
    val display_name_prefixed: String?,
    val community_icon: String?,
    val created: Double?,
    val subredditType: SubredditsType,
    val user_is_subscriber: Boolean?,
    val description: String?,
    val icon_img: String?,
    val header_img: String?,
    val subscribers: Int?,
    val public_description: String?,
    val banner_background_image: String?,
    val isExpanded: Boolean = false,
    val isFavorite: Boolean = false
)
