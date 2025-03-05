package com.example.codeexercise1.repository

import android.util.Log
import com.example.codeexercise1.db.ItemEntity
import com.example.codeexercise1.db.ItemsDao
import com.example.codeexercise1.util.retrofit.ApiService
import com.example.codeexercise1.util.serviceObjects.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ItemRepository @Inject constructor(
    private val apiService: ApiService,
    private val itemsDao: ItemsDao
) {
    private val _items = MutableSharedFlow<List<Item>>(replay = 1)
    val items: SharedFlow<List<Item>> = _items


    suspend fun updateItems(): Unit =
        withContext(Dispatchers.IO) {
            if (!shouldFetchFromDb()) {
                fetchItemsFromApi()
            }
            fetchItemsFromDb().collect {
                _items.tryEmit(it)
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
        name = name ?: ""
    )

    // we don't expect the json to change, but in real world would check TTL or set a value
    private suspend fun shouldFetchFromDb() =
        withContext(Dispatchers.IO) { return@withContext itemsDao.countItems() > 0 }

    private fun fetchItemsFromDb() =
        itemsDao.getAllSorted().map { list -> list.map { it.toDomain() } }


    private suspend fun fetchItemsFromApi() =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.getJson()
                if (response.isSuccessful) {
                    val items = response.body() ?: emptyList()
                    itemsDao.deleteAll()
                    itemsDao.insertAll(items.map { it.toEntity() })

                } else {
                    Log.e("API Error", "Response Code: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "Error fetching data", e)
            }
        }


}
