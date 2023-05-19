package com.kadun.kadchat.ui.favorite.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class FavoriteType : Parcelable {
    SUBREDDITS, POSTS
}