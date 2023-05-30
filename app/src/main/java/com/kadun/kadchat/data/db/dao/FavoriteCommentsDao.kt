package com.kadun.kadchat.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.kadun.kadchat.data.db.entity.DbFavoritesComments
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteCommentsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: DbFavoritesComments)

    @Query("DELETE FROM DbFavoritesComments WHERE isFavorite = 1")
    suspend fun clearData()

    @Transaction
    @Query("SELECT * FROM DbFavoritesComments ORDER BY sortingPosition")
    fun getFavoriteList(): Flow<List<DbFavoritesComments>>

    @Query("SELECT * FROM DbFavoritesComments WHERE id = :id")
    suspend fun getDbFavoriteComments(id: String): DbFavoritesComments
}