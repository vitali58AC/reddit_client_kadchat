package com.kadun.kadchat.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kadun.kadchat.data.db.entity.DbSubredditRemoteKeys
import com.kadun.kadchat.ui.home.data.SubredditsType

@Dao
interface SubredditRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKey: DbSubredditRemoteKeys)

    @Query("SELECT * FROM DbSubredditRemoteKeys WHERE type = :type")
    suspend fun getRemoteKey(type: SubredditsType): DbSubredditRemoteKeys?

    @Query("DELETE FROM DbSubredditRemoteKeys WHERE type = :type")
    suspend fun clearRemoteKeys(type: SubredditsType)
}