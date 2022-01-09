package com.softocorp.assignmentapp.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.softocorp.assignmentapp.MyApplication
import com.softocorp.assignmentapp.model.RepositoryModel

@Database(entities = [RepositoryModel::class], version = 1)
abstract class RepositoryDatabase: RoomDatabase() {

    companion object {
        val instance = Room.databaseBuilder(MyApplication.context!!, RepositoryDatabase::class.java, "repository_database")
            .allowMainThreadQueries()
            .build()
    }

    abstract fun getRepositoryDao(): RepositoryDao

}