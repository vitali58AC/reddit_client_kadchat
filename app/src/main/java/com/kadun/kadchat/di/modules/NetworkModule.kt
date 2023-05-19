package com.kadun.kadchat.di.modules

import com.kadun.kadchat.data.network.AuthInterceptor
import com.kadun.kadchat.data.network.KadChatRetrofit
import com.kadun.kadchat.data.network.RedditAuthClient
import com.kadun.kadchat.data.repositories.SubredditsRepository
import com.kadun.kadchat.data.repositories.SubredditsRepositoryImpl
import com.kadun.kadchat.data.repositories.UserRepository
import com.kadun.kadchat.data.repositories.UserRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkModule = module {

    single { RedditAuthClient.getAuthClient(androidContext()) }
    single { KadChatRetrofit.provideOkHttpClient(get()) }
    single { KadChatRetrofit.provideRetrofit(get()) }
    single { KadChatRetrofit.provideRedditApi(get()) }
    single { AuthInterceptor(get()) }

    factory<SubredditsRepository> { SubredditsRepositoryImpl(get(), get()) }
    factory<UserRepository> { UserRepositoryImpl(get()) }
}