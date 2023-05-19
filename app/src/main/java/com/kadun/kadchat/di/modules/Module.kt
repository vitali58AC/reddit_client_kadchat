package com.kadun.kadchat.di.modules

import com.kadun.kadchat.ui.home.HomeViewModel
import com.kadun.kadchat.ui.home.PostsViewModel
import com.kadun.kadchat.ui.home.data.SubredditsType
import com.kadun.kadchat.ui.profile.FriendsListViewModel
import com.kadun.kadchat.ui.profile.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { (type: SubredditsType) -> HomeViewModel(type, get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { FriendsListViewModel(get()) }
    viewModel { (subredditName: String) -> PostsViewModel(subredditName, get()) }
}