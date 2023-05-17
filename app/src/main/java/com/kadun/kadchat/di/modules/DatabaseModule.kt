package com.kadun.kadchat.di.modules

import com.kadun.kadchat.data.db.RoomDaoDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single { RoomDaoDatabase.createDb(androidApplication()) }
}