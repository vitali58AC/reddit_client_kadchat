package com.kadun.kadchat.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.kadun.kadchat.data.db.entity.DbCommentsData
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<DbCommentsData>)

    @Query("DELETE FROM DbCommentsData WHERE rootPostId = :rootPostId")
    suspend fun clearDataById(rootPostId: String)

    @Query("SELECT * FROM DbCommentsData WHERE rootPostId = :rootPostId ORDER BY sortingPosition")
    fun getItems(rootPostId: String): PagingSource<Int, DbCommentsData>

    @Transaction
    @Query("SELECT * FROM DbCommentsData WHERE isFavorite = 1 ORDER BY sortingPosition")
    fun getFavoriteList(): Flow<List<DbCommentsData>>

    @Query("UPDATE DbCommentsData SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteState(id: String, isFavorite: Boolean)

    @Query("SELECT * FROM DbCommentsData WHERE id = :id")
    suspend fun getDbCommentsData(id: String): DbCommentsData
}