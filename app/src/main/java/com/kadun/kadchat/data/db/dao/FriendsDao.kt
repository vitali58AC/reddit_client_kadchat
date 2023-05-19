package com.kadun.kadchat.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kadun.kadchat.data.db.entity.DbFriendsData

@Dao
interface FriendsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFriends(subredditList: List<DbFriendsData>)

    @Query("DELETE FROM DbFriendsData")
    suspend fun clearFriendsData()

    @Query("SELECT * FROM DbFriendsData ORDER BY sortingPosition")
    fun getFriends(): PagingSource<Int, DbFriendsData>

}