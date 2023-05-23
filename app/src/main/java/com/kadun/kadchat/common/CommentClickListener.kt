package com.kadun.kadchat.common

interface CommentClickListener<T> {
    fun onFavoriteClick(item: T)
}