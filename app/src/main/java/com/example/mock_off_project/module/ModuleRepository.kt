package com.example.mock_off_project.module

import com.example.mock_off_project.repository.IItemRepository
import com.example.mock_off_project.repository.ItemRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface ModuleRepository {
    @Binds
    fun getRepository(repository: ItemRepository): IItemRepository
}