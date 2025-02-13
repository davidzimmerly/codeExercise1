package com.example.codeexercise1.db
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemsDao {
    @Query("SELECT * FROM items")
    fun getAll(): Flow<List<ItemEntity>>

    @Insert
    fun insertAll(items: List<ItemEntity>)

    @Query("DELETE FROM items")
    fun deleteAll()


    @Query("SELECT COUNT(*) FROM items")
    fun countItems(): Int
}