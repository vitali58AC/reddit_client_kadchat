package com.kadun.kadchat.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kadun.kadchat.data.network.data.comments.Comment
import com.kadun.kadchat.data.network.data.comments.CommentAnswers
import com.kadun.kadchat.data.network.data.comments.CommentAnswersRootDto

@Entity
data class DbCommentsData(
    @PrimaryKey(autoGenerate = true)
    val sortingPosition: Long = 0,
    val replies: List<CommentAnswers>?,
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
    val ups: Int?,
    val isFavorite: Boolean = false,
    val rootPostId: String
) {
    companion object {
        fun fromDto(dto: Comment, isFavorite: Boolean, rootPostId: String) = DbCommentsData(
            replies = if (dto.replies !is String) {
                (dto.replies as? CommentAnswersRootDto)?.data?.children?.map { it.data }
            } else null,
            id = dto.id,
            author = dto.author,
            created_utc = dto.created_utc,
            parent_id = dto.parent_id,
            score = dto.score,
            author_fullname = dto.author_fullname,
            body = dto.body,
            name = dto.name,
            downs = dto.downs,
            created = dto.created,
            link_id = dto.link_id,
            subreddit_name_prefixed = dto.subreddit_name_prefixed,
            depth = dto.depth,
            ups = dto.ups,
            isFavorite = isFavorite,
            rootPostId = rootPostId
        )

        fun DbCommentsData.toFavoriteData() = DbFavoritesComments(
            replies = replies,
            id = id,
            author = author,
            created_utc = created_utc,
            parent_id = parent_id,
            score = score,
            author_fullname = author_fullname,
            body = body,
            name = name,
            downs = downs,
            created = created,
            link_id = link_id,
            subreddit_name_prefixed = subreddit_name_prefixed,
            depth = depth,
            ups = ups,
            isFavorite = isFavorite
        )
    }
}
