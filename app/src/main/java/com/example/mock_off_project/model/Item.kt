package com.example.mock_off_project.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Item")
data class Item(
    @PrimaryKey var id: Int,
    @ColumnInfo(name = "Icon") val icon: Int,
    @ColumnInfo(name = "Color") val color: Int,
    @ColumnInfo(name = "Name") val name: String,
    @ColumnInfo(name = "Color_Background") val colorBackGround: Int,
    @ColumnInfo(name = "Value") val value: Int
) : Serializable