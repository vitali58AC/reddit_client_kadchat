package com.kadun.kadchat.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kadun.kadchat.data.db.entity.DbPostsData

@Dao
interface PostsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<DbPostsData>)

    @Query("DELETE FROM DbPostsData")
    suspend fun clearData()

    @Query("SELECT * FROM DbPostsData WHERE subreddit_name_prefixed = :subredditName ORDER BY sortingPosition")
    fun getItems(subredditName: String): PagingSource<Int, DbPostsData>

}