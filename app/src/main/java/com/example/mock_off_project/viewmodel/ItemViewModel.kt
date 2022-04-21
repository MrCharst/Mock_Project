package com.example.mock_off_project.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mock_off_project.model.Item
import com.example.mock_off_project.repository.IItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(private val iItemRepository: IItemRepository) :
    ViewModel() {

    fun insertItem(item: Item) = viewModelScope.launch { iItemRepository.insertItem(item) }

    fun updateItem(item: Item) = viewModelScope.launch { iItemRepository.updateItem(item) }

    fun getAllItem(): LiveData<List<Item>> = iItemRepository.getAllItem()


}