package com.kadun.kadchat.data.db

import android.app.Application
import androidx.room.*
import com.kadun.kadchat.data.db.dao.*
import com.kadun.kadchat.data.db.entity.*
import com.kadun.kadchat.data.network.data.posts.Image
import com.kadun.kadchat.data.network.data.posts.ImageDto
import com.kadun.kadchat.data.network.data.posts.MediaPreview
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

@Database(
    entities = [
        DbSubredditData::class,
        DbSubredditRemoteKeys::class,
        DbFriendsData::class,
        DbFriendsRemoteKeys::class,
        DbPostsData::class,
        DbPostsRemoteKeys::class,
        DbFavoriteSubreddits::class,
        DbFavoritesPosts::class
    ],
    version = RoomDaoDatabase.DB_VERSION
)
@TypeConverters(DbTypeConverters::class)
abstract class RoomDaoDatabase : RoomDatabase() {
    abstract fun getSubredditDao(): SubredditDao
    abstract fun getSubredditRemoteKeysDao(): SubredditRemoteKeysDao
    abstract fun getFriendsDao(): FriendsDao
    abstract fun getFriendsRemoteKeysDao(): FriendsRemoteKeysDao
    abstract fun getPostsDao(): PostsDao
    abstract fun getPostsRemoteKeysDao(): PostsRemoteKeysDao
    abstract fun getFavoriteSubredditDao(): FavoriteSubredditDao
    abstract fun getFavoritePostDao(): FavoritePostDao


    companion object {
        const val DB_VERSION = 12
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

class DbTypeConverters {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val mediaPreviewAdapter = moshi.adapter(MediaPreview::class.java)

    private val listImageDtoAdapter = moshi.adapter<List<ImageDto>>(
        Types.newParameterizedType(
            List::class.java,
            Image::class.java
        )
    )

    private val listImageAdapter = moshi.adapter<List<Image>>(
        Types.newParameterizedType(
            List::class.java,
            Image::class.java
        )
    )

    @TypeConverter
    fun listImageToString(list: List<Image>?) = list?.let {
        listImageAdapter.toJson(it)
    }

    @TypeConverter
    fun stringToListImage(string: String?) = string?.let {
        listImageAdapter.fromJson(it)
    }

    @TypeConverter
    fun mediaPreviewToString(value: MediaPreview?) = value?.let {
        mediaPreviewAdapter.toJson(it)
    }

    @TypeConverter
    fun stringToMediaPreview(string: String?) = string?.let {
        mediaPreviewAdapter.fromJson(it)
    }

    @TypeConverter
    fun listImageDtoToString(value: List<ImageDto>?) = value?.let {
        listImageDtoAdapter.toJson(it)
    }

    @TypeConverter
    fun stringToListImageDto(string: String?) = string?.let {
        listImageDtoAdapter.fromJson(it)
    }
}