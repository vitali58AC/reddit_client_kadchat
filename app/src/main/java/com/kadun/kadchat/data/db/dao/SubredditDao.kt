package com.kadun.kadchat.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kadun.kadchat.data.db.entity.DbSubredditData
import com.kadun.kadchat.ui.home.data.SubredditsType

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

    @Query("UPDATE DbSubredditData SET isExpanded = :isExpanded WHERE id = :id")
    suspend fun updateSubredditExpandedState(id: String, isExpanded: Boolean)
}