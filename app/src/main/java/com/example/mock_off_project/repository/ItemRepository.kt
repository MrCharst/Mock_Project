package com.example.mock_off_project.repository

import androidx.lifecycle.LiveData
import com.example.mock_off_project.model.Item
import com.example.mock_off_project.model.ItemDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ItemRepository
@Inject constructor(private var itemDao: ItemDao) : IItemRepository {
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
    override fun getAllItem(): LiveData<List<Item>> {
        return itemDao.getAllItem()
    }

    override suspend fun updateItem(item: Item) {
        withContext(defaultDispatcher) {
            itemDao.updateItem(item)
        }
    }

    override suspend fun insertItem(item: Item) {
        withContext(defaultDispatcher) {
            itemDao.insertItem(item)
        }
    }
}