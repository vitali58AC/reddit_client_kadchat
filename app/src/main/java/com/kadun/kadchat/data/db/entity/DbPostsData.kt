package com.kadun.kadchat.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kadun.kadchat.data.network.data.posts.MediaPreview
import com.kadun.kadchat.data.network.data.posts.Post

@Entity
data class DbPostsData(
    @PrimaryKey(autoGenerate = true)
    val sortingPosition: Long = 0,
    val id: String?,
    val subreddit: String?,
    val selftext: String?,
    val title: String?,
    val subreddit_name_prefixed: String?,
    val name: String?,
    val upvote_ratio: Double?,
    val ups: Int?,
    val score: Int?,
    val is_self: Boolean?,
    val created: Double?,
    val likes: Boolean?,
    val view_count: Int?,
    val subreddit_id: String?,
    val author: String?,
    val num_comments: Int?,
    val url: String?,
    val subreddit_subscribers: Int?,
    val thumbnail: String?,
    val url_overridden_by_dest: String?,
    val preview: MediaPreview?,
    val isVoted: Boolean? = null,
    val isFavorite: Boolean = false
) {
    companion object {
        fun fromDto(dto: Post, isFavorite: Boolean) = DbPostsData(
            id = dto.id,
            subreddit = dto.subreddit,
            selftext = dto.selftext,
            title = dto.title,
            subreddit_name_prefixed = dto.subreddit_name_prefixed,
            name = dto.name,
            upvote_ratio = dto.upvote_ratio,
            ups = dto.ups,
            score = dto.score,
            is_self = dto.is_self,
            created = dto.created,
            likes = dto.likes,
            view_count = dto.view_count,
            subreddit_id = dto.subreddit_id,
            author = dto.author,
            num_comments = dto.num_comments,
            url = dto.url,
            subreddit_subscribers = dto.subreddit_subscribers,
            thumbnail = dto.thumbnail,
            url_overridden_by_dest = dto.url_overridden_by_dest,
            preview = dto.preview,
            isFavorite = isFavorite
        )

        fun DbPostsData.toFavoriteData() = DbFavoritesPosts(
            id = id,
            subreddit = subreddit,
            selftext = selftext,
            title = title,
            subreddit_name_prefixed = subreddit_name_prefixed,
            name = name,
            upvote_ratio = upvote_ratio,
            ups = ups,
            score = score,
            is_self = is_self,
            created = created,
            likes = likes,
            view_count = view_count,
            subreddit_id = subreddit_id,
            author = author,
            num_comments = num_comments,
            url = url,
            subreddit_subscribers = subreddit_subscribers,
            thumbnail = thumbnail,
            url_overridden_by_dest = url_overridden_by_dest,
            preview = preview,
            isVoted = isVoted,
            isFavorite = isFavorite
        )
    }
}
