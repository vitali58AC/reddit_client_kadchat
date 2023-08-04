package com.kadun.kadchat.data.network

import android.content.Context
import com.kirkbushman.auth.RedditAuth
import com.kirkbushman.auth.managers.SharedPrefsStorageManager
import java.lang.Exception

class RedditAuthClient {

    companion object {

        private val CLIENT_ID = "CLIENT_ID"
            get() {
                throw Exception("Get Client id from https://www.reddit.com/prefs/apps")
                field
            }
        private const val REDIRECT_URI = "android-app://com.kadun.kadchat/login_fragment"
        private val SCOPES = arrayOf(
            "identity",
            "edit",
            "flair",
            "history",
            "modconfig",
            "modflair",
            "modlog",
            "modposts",
            "modwiki",
            "mysubreddits",
            "privatemessages",
            "read",
            "report",
            "save",
            "submit",
            "subscribe",
            "vote",
            "wikiedit",
            "wikiread"
        )


        fun getAuthClient(context: Context) = RedditAuth.Builder()
            // specify the credentials you can find on your reddit app console
            .setApplicationCredentials(clientId = CLIENT_ID, redirectUrl = REDIRECT_URI)
            // the api endpoints scopes this client will need
            .setScopes(SCOPES)
            // to manage tokens info in memory
            .setStorageManager(SharedPrefsStorageManager(context))
            // if you set this flag to 'true' it will add to the OkHttp Client a listener to log the
            // Request and Response object, to make it easy to debug.
            .setLogging(true)
            .build()

        fun String.fixRedditAuthUrl() = replace("www.reddit.com", "old.reddit.com")
        fun String.fixRedditImageLink() = replace("&amp;", "&")
    }
}