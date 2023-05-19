package com.kadun.kadchat.data.db.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.kadun.kadchat.data.db.entity.DbPostsData
import kotlinx.coroutines.flow.Flow

@Dao
interface PostsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<DbPostsData>)

    @Query("DELETE FROM DbPostsData")
    suspend fun clearData()

    @Query("SELECT * FROM DbPostsData WHERE subreddit_name_prefixed = :subredditName ORDER BY sortingPosition")
    fun getItems(subredditName: String): PagingSource<Int, DbPostsData>

    @Transaction
    @Query("SELECT * FROM DbPostsData WHERE isFavorite = 1 ORDER BY sortingPosition")
    fun getPostsFavoriteList(): Flow<List<DbPostsData>>

    @Query("UPDATE DbPostsData SET isFavorite = :isFavorite WHERE name = :name")
    suspend fun updatePostFavoriteState(name: String, isFavorite: Boolean)

    @Query("SELECT * FROM DbPostsData WHERE name = :name")
    suspend fun getDbPostData(name: String): DbPostsData
}