package com.kadun.kadchat

import android.app.Application
import com.kadun.kadchat.di.modules.appModule
import com.kadun.kadchat.di.modules.databaseModule
import com.kadun.kadchat.di.modules.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KadChatApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() = startKoin {
        androidContext(this@KadChatApp)
        modules(networkModule, appModule, databaseModule)
    }
}