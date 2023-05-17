package com.kadun.kadchat.data.network

import com.kadun.kadchat.data.network.api.RedditApi
import com.serjltt.moshi.adapters.FallbackEnum
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

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
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(getMoshi()).asLenient())
            .client(client)
            .build()

        private fun getMoshi(): Moshi =
            Moshi.Builder()
                .add(FallbackEnum.ADAPTER_FACTORY)
                .add(KotlinJsonAdapterFactory())
                .build()

        fun provideRedditApi(retrofit: Retrofit): RedditApi =
            retrofit.create(RedditApi::class.java)

    }
}