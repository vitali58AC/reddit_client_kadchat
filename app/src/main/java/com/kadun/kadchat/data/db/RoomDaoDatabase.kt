package com.kadun.kadchat.data.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kadun.kadchat.data.db.dao.SubredditDao
import com.kadun.kadchat.data.db.dao.SubredditRemoteKeysDao
import com.kadun.kadchat.data.db.entity.DbSubredditData
import com.kadun.kadchat.data.db.entity.DbSubredditRemoteKeys

@Database(
    entities = [
        DbSubredditData::class,
        DbSubredditRemoteKeys::class
    ],
    version = RoomDaoDatabase.DB_VERSION
)
abstract class RoomDaoDatabase : RoomDatabase() {
    abstract fun getSubredditDao(): SubredditDao
    abstract fun getSubredditRemoteKeysDao(): SubredditRemoteKeysDao

    companion object {
        const val DB_VERSION = 1
        private const val DB_NAME = "room-dao-data"


        fun createDb(application: Application): RoomDaoDatabase {
            return Room.databaseBuilder(
                application, RoomDaoDatabase::class.java, DB_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}