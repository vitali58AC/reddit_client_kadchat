package com.kadun.kadchat.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kadun.kadchat.data.db.entity.DbFriendsRemoteKeys

@Dao
interface FriendsRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKey: DbFriendsRemoteKeys)

    @Query("SELECT * FROM DbFriendsRemoteKeys")
    suspend fun getRemoteKey(): DbFriendsRemoteKeys?

    @Query("DELETE FROM DbFriendsRemoteKeys")
    suspend fun clearRemoteKeys()
}