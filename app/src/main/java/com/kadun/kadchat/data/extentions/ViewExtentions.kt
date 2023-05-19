package com.kadun.kadchat.data.extentions

import android.animation.LayoutTransition
import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.kadun.kadchat.data.network.RedditAuthClient.Companion.fixRedditImageLink

fun ViewGroup.enableTransactions() {
    this.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
}

fun <T> RequestBuilder<T>.setProgressPlaceHolder(
    context: Context,
    strokeWidth: Float = 5f,
    centerRadius: Float? = null,
    @ColorInt color: Int? = null
) = placeholder(CircularProgressDrawable(context).apply {
    this.strokeWidth = strokeWidth
    centerRadius?.let { this.centerRadius = it }
    color?.let {
        this.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
            it, BlendModeCompat.SRC_ATOP
        )
    }
    this.start()
})

fun ImageView.loadImage(url: String) {
    Glide.with(this.context.applicationContext)
        .apply {
            load(url.fixRedditImageLink())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(CenterInside())
                .setProgressPlaceHolder(
                    this@loadImage.context.applicationContext, centerRadius = 40f
                )
                .into(this@loadImage)
        }
}

fun Context.getCompatDrawable(@DrawableRes res: Int) = AppCompatResources.getDrawable(this, res)