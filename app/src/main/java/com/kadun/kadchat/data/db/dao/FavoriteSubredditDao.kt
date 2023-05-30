package com.kadun.kadchat.data.db.dao

import androidx.room.*
import com.kadun.kadchat.data.db.entity.DbFavoriteSubreddits
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteSubredditDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubreddit(subreddit: DbFavoriteSubreddits)

    @Query("DELETE FROM DbFavoriteSubreddits WHERE isFavorite = 1")
    suspend fun clearSubreddits()

    @Transaction
    @Query("SELECT * FROM DbFavoriteSubreddits ORDER BY sortingPosition")
    fun getSubredditsFavoriteList(): Flow<List<DbFavoriteSubreddits>>

    @Query("SELECT * FROM DbSubredditData WHERE name = :name")
    suspend fun getDbSubredditData(name: String): DbFavoriteSubreddits

    @Query("UPDATE DbFavoriteSubreddits SET isFavorite = :isFavorite WHERE name = :name")
    suspend fun updateSubredditFavoriteState(name: String, isFavorite: Boolean)
}