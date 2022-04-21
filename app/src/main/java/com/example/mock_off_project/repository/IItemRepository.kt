package com.example.mock_off_project.repository

import androidx.lifecycle.LiveData
import com.example.mock_off_project.model.Item

interface IItemRepository {
    fun getAllItem(): LiveData<List<Item>>

    suspend fun updateItem(item: Item)

    suspend fun insertItem(item: Item)
}