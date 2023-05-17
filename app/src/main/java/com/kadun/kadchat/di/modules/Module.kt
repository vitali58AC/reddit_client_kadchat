package com.kadun.kadchat.di.modules

import com.kadun.kadchat.ui.home.HomeViewModel
import com.kadun.kadchat.ui.home.data.SubredditsType
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { (type: SubredditsType) -> HomeViewModel(type, get()) }
}