package com.surya.fubotvtask

import android.app.Application
import androidx.room.Room
import com.surya.fubotvtask.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class FuboTVApplication : Application() {

    private val appModule = module {
        single {
            Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "app-database"
            ).build()
        }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(applicationContext)
            modules(appModule)
        }
    }
}
