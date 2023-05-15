package com.kadun.kadchat.data.network

import com.kadun.kadchat.data.network.api.RedditApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class KadChatRetrofit {

    companion object {

        fun provideOkHttpClient(authInterceptor: AuthInterceptor) = OkHttpClient
            .Builder()
            .addNetworkInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .addInterceptor(authInterceptor)
            .build()

        fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
            .baseUrl("https://www.reddit.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()

        fun provideRedditApi(retrofit: Retrofit): RedditApi =
            retrofit.create(RedditApi::class.java)

    }
}