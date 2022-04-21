package com.example.mock_off_project.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ItemDao {
    @Query("SELECT * FROM Item")
    fun getAllItem(): LiveData<List<Item>>

    @Update
    suspend fun updateItem(item: Item)

    @Insert
    suspend fun insertItem(item: Item)
}