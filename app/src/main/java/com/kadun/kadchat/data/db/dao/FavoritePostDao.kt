package com.kadun.kadchat.data.db.dao

import androidx.room.*
import com.kadun.kadchat.data.db.entity.DbFavoritesPosts
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritePostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: DbFavoritesPosts)

    @Query("DELETE FROM DbFavoritesPosts WHERE isFavorite = 1")
    suspend fun clearData()

    @Transaction
    @Query("SELECT * FROM DbFavoritesPosts ORDER BY sortingPosition")
    fun getPostsFavoriteList(): Flow<List<DbFavoritesPosts>>

    @Query("SELECT * FROM DbPostsData WHERE name = :name")
    suspend fun getDbPostData(name: String): DbFavoritesPosts
}