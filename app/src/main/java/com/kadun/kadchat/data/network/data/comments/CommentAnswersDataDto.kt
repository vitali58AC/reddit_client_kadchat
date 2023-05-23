package com.kadun.kadchat.data.network.data.comments

data class CommentAnswersDataDto(
    val after: String?,
    val dist: Int?,
    val children: List<CommentAnswersDto>
)