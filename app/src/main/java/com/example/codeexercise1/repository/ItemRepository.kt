package com.example.codeexercise1.repository

import android.util.Log
import com.example.codeexercise1.db.ItemEntity
import com.example.codeexercise1.db.ItemsDao
import com.example.codeexercise1.util.retrofit.ApiService
import com.example.codeexercise1.util.serviceObjects.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ItemRepository @Inject constructor(
    private val apiService: ApiService,
    private val itemsDao: ItemsDao
) {

    fun getItems(): Flow<List<Item>> = flow {
         if (shouldFetchFromDb()) {
            emitAll(fetchItemsFromDb())
        } else {
            emit(fetchItemsFromApi())
        }
    }

    private fun ItemEntity.toDomain(): Item = Item(
        id = id,
        listId = listId,
        name = name
    )

    private fun Item.toEntity(): ItemEntity = ItemEntity(
        id = id,
        listId = listId,
        name = name ?: "" //TODO filter for nulls before insertion
    )

    // we don't expect the json to change, but in real world would check TTL or set a value
    private fun shouldFetchFromDb() = itemsDao.countItems() > 0

    private fun fetchItemsFromDb() = itemsDao.getAll().map { list -> list.map { it.toDomain() } }
    private suspend fun fetchItemsFromApi(): List<Item> {
        try {
            val response = apiService.getJson()
            if (response.isSuccessful) {
                val items = response.body() ?: emptyList()
                itemsDao.deleteAll()
                itemsDao.insertAll(items.map { it.toEntity() })
                return items
            } else {
                Log.e("API Error", "Response Code: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("API Exception", "Error fetching data", e)
        }
        return emptyList()
    }
}
