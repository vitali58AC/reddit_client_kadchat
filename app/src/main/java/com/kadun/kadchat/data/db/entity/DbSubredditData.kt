package com.kadun.kadchat.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kadun.kadchat.data.network.data.subreddit.SubredditsDto
import com.kadun.kadchat.ui.home.data.SubredditsType

@Entity
data class DbSubredditData(
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
) {
    companion object {
        fun fromDto(dto: SubredditsDto, type: SubredditsType) = DbSubredditData(
            id = dto.data.id,
            name = dto.data.name,
            title = dto.data.title,
            display_name = dto.data.display_name,
            display_name_prefixed = dto.data.display_name_prefixed,
            community_icon = dto.data.community_icon,
            created = dto.data.created,
            subredditType = type,
            user_is_subscriber = dto.data.user_is_subscriber,
            description = dto.data.description,
            icon_img = dto.data.icon_img,
            header_img = dto.data.header_img,
            subscribers = dto.data.subscribers,
            public_description = dto.data.public_description,
            banner_background_image = dto.data.banner_background_image
        )
    }
}