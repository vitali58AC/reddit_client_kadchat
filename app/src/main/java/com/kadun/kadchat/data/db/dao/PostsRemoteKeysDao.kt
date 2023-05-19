package com.kadun.kadchat.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kadun.kadchat.data.db.entity.DbPostsRemoteKeys

@Dao
interface PostsRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKey: DbPostsRemoteKeys)

    @Query("SELECT * FROM DbPostsRemoteKeys")
    suspend fun getRemoteKey(): DbPostsRemoteKeys?

    @Query("DELETE FROM DbPostsRemoteKeys")
    suspend fun clearRemoteKeys()
}