package com.example.codeexercise1.db
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ItemsDao {
    @Query("SELECT * FROM items")
    fun getAll(): List<ItemEntity>

    @Insert
    fun insertAll(items: List<ItemEntity>)

    @Query("DELETE FROM items")
    fun deleteAll()
}