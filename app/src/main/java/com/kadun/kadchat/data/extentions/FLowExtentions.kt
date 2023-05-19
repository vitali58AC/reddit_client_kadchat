package com.kadun.kadchat.data.extentions

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates

fun LoadStates.isNotLoading() =
    append.isNotLoading() && prepend.isNotLoading() && refresh.isNotLoading()

fun LoadState.isNotLoading() = this is LoadState.NotLoading

fun CombinedLoadStates.isNotLoading(): Boolean {
    val isMediatorNotLoading = mediator?.isNotLoading() ?: true
    val isSourceNotLoading = source.isNotLoading()
    return isMediatorNotLoading && isSourceNotLoading &&
            append.isNotLoading() && prepend.isNotLoading() && refresh.isNotLoading()
}