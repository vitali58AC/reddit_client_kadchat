package com.kadun.kadchat.common

import android.annotation.SuppressLint
import android.content.Context

class UserSettingPrefs(context: Context) {

    private val sharedPrefers =
        context.getSharedPreferences(USER_SETTINGS_PREFS, Context.MODE_PRIVATE)

    @SuppressLint("CommitPrefEdits")
    fun putValue(key: String, value: Boolean = NEED_INTRO) {
        sharedPrefers
            .edit()
            .putBoolean(key, value)
            .apply()
    }

    fun getValue(key: String) = sharedPrefers.getBoolean(key, false)

    companion object {
        private const val USER_SETTINGS_PREFS = "user_settings_prefs"
        private const val NEED_INTRO = true
    }
}