package com.example.mock_off_project.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class ItemRoomDatabase : RoomDatabase() {

    abstract fun getItemDao(): ItemDao

    companion object {
        @Volatile
        private var INSTANCE: ItemRoomDatabase? = null
        private const val DB_NAME = "product_db"

        fun getInstant(context: Context): ItemRoomDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    ItemRoomDatabase::class.java,
                    DB_NAME
                ).build()
            }
            return INSTANCE!!

        }
    }
}