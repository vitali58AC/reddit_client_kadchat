package com.kadun.kadchat.di.modules

import com.kadun.kadchat.common.UserSettingPrefs
import com.kadun.kadchat.ui.favorite.FavoriteViewModel
import com.kadun.kadchat.ui.home.CommentsViewModel
import com.kadun.kadchat.ui.home.HomeViewModel
import com.kadun.kadchat.ui.home.PostsViewModel
import com.kadun.kadchat.ui.home.data.SubredditsType
import com.kadun.kadchat.ui.profile.FriendsListViewModel
import com.kadun.kadchat.ui.profile.ProfileViewModel
import com.kadun.kadchat.ui.profile.UserViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { UserSettingPrefs(androidContext()) }
    viewModel { (type: SubredditsType) -> HomeViewModel(type, get()) }
    viewModel { ProfileViewModel(get(), get(), get()) }
    viewModel { FriendsListViewModel(get()) }
    viewModel { (subredditName: String) -> PostsViewModel(subredditName, get()) }
    viewModel { FavoriteViewModel(get()) }
    viewModel { (postId: String) -> CommentsViewModel(postId, get()) }
    viewModel { (author: String) -> UserViewModel(author, get()) }
}