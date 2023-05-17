package com.kadun.kadchat.data.network

import com.kirkbushman.auth.RedditAuth
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val authClient: RedditAuth) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = authClient.getSavedBearer().getAccessToken()
            ?: authClient.getTokenBearer()?.getAccessToken()
            ?: ""
        val modifiedRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(modifiedRequest)
    }
}
