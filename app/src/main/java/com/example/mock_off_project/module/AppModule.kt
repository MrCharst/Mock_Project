package com.example.mock_off_project.module

import android.app.Application
import com.example.mock_off_project.model.ItemDao
import com.example.mock_off_project.model.ItemRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun getItemDB(context: Application): ItemRoomDatabase {
        return ItemRoomDatabase.getInstant(context)
    }

    @Singleton
    @Provides
    fun getItemDao(itemRoomDatabase: ItemRoomDatabase): ItemDao {
        return itemRoomDatabase.getItemDao()
    }
}