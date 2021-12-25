package com.example.model.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.model.bean.PrivateFile
import com.example.model.database.dao.PrivateFileDao

@Database(entities = [PrivateFile::class], version = 1)
abstract class DatabaseManager:RoomDatabase() {
    abstract fun getPrivateFileDao(): PrivateFileDao
    companion object {
        lateinit var dbManager: DatabaseManager
        @JvmStatic
        fun initDataBase(application: Application) {
            dbManager = Room.databaseBuilder(application,
                DatabaseManager::class.java, application.packageName + "_data")
                .allowMainThreadQueries()
                .build()
        }
    }
}