package com.kadun.kadchat.common

interface PostClickListener<T> {

    fun onVoteUpClicked(item: T)
    fun onVoteDownClicked(item: T)
    fun onCommentClicked(item: T)
    fun onFavoriteClicked(item: T)

}