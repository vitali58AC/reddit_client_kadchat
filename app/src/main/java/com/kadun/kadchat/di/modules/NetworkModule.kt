package com.kadun.kadchat.di.modules

import com.kadun.kadchat.data.network.AuthInterceptor
import com.kadun.kadchat.data.network.KadChatRetrofit
import com.kadun.kadchat.data.network.RedditAuthClient
import com.kadun.kadchat.data.repositories.SubredditsRepositories
import com.kadun.kadchat.data.repositories.SubredditsRepositoriesImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkModule = module {

    single { RedditAuthClient.getAuthClient(androidContext()/*, get()*/) }
    single { KadChatRetrofit.provideOkHttpClient(get()) }
    single { KadChatRetrofit.provideRetrofit(get()) }
    single { KadChatRetrofit.provideRedditApi(get()) }
    single { AuthInterceptor(get()) }

    factory<SubredditsRepositories> { SubredditsRepositoriesImpl(get()) }
}