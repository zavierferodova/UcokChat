package com.robbies.ucokchat

import android.app.Application
import com.robbies.ucokchat.di.firebaseDatabaseModule
import com.robbies.ucokchat.di.repositoryModule
import com.robbies.ucokchat.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                firebaseDatabaseModule,
                repositoryModule,
                viewModelModule,
            )
        }
    }
}