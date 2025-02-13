package com.example.codeexercise1.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey() val id: Int,
    @ColumnInfo(name = "list_id") val listId: String?,
    @ColumnInfo(name = "name") val name: String?
)