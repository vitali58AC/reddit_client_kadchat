package com.kadun.kadchat.data.db.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.kadun.kadchat.data.db.entity.DbSubredditData
import com.kadun.kadchat.ui.home.data.SubredditsType
import kotlinx.coroutines.flow.Flow

@Dao
interface SubredditDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubreddit(subredditList: List<DbSubredditData>)

    @Query("DELETE FROM DbSubredditData WHERE subredditType =:type")
    suspend fun clearSubredditDataByType(type: SubredditsType)

    @Query("SELECT * FROM DbSubredditData WHERE subredditType =:type ORDER BY sortingPosition")
    fun getSubredditByType(type: SubredditsType): PagingSource<Int, DbSubredditData>

    @Query("UPDATE DbSubredditData SET user_is_subscriber = :isSubscribed WHERE display_name = :displayName")
    suspend fun updateSubredditSubscribeState(isSubscribed: Boolean, displayName: String)

    @Query("UPDATE DbSubredditData SET isExpanded = :isExpanded WHERE name = :name")
    suspend fun updateSubredditExpandedState(name: String, isExpanded: Boolean)

    @Query("UPDATE DbSubredditData SET isFavorite = :isFavorite WHERE name = :name")
    suspend fun updateSubredditFavoriteState(name: String, isFavorite: Boolean)

    @Query("SELECT * FROM DbSubredditData WHERE name = :name")
    suspend fun getDbSubredditData(name: String): DbSubredditData

    @Transaction
    @Query("SELECT * FROM DbSubredditData WHERE isFavorite = 1 ORDER BY sortingPosition")
    fun getSubredditsFavoriteList(): Flow<List<DbSubredditData>>
}