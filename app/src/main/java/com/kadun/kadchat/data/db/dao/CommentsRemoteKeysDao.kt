package com.kadun.kadchat.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kadun.kadchat.data.db.entity.DbCommentsRemoteKeys
import com.kadun.kadchat.data.db.entity.DbPostsRemoteKeys

@Dao
interface CommentsRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKey: DbCommentsRemoteKeys)

    @Query("SELECT * FROM DbCommentsRemoteKeys")
    suspend fun getRemoteKey(): DbCommentsRemoteKeys?

    @Query("DELETE FROM DbCommentsRemoteKeys")
    suspend fun clearRemoteKeys()
}