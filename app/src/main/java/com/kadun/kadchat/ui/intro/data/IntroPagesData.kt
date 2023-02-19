package com.kadun.kadchat.ui.intro.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class IntroPagesData(
    @StringRes val text: Int,
    @StringRes val description: Int,
    @DrawableRes val image: Int,
    val isLastPage: Boolean = false
)
