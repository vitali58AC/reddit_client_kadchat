package com.kadun.kadchat.data.repositories

import com.kadun.kadchat.data.utils.AppResult
import okhttp3.ResponseBody

interface SubredditsRepositories {

    suspend fun getNewSubreddits(): AppResult<ResponseBody>
}