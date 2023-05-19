package com.kadun.kadchat.common

interface SubredditClickListener<T> {

    fun onSubscribeClicked(item: T)
    fun onFavoriteClicked(item: T)
    fun onRootClicked(item: T)
    fun onOpenSubredditPosts(item: T)
}