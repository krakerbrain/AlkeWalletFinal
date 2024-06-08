package com.example.alkewalletfinal.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.alkewalletfinal.data.dao.TransactionDao
import com.example.alkewalletfinal.model.database.TransactionEntity

@Database(entities = [TransactionEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "alkewallet-db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}