package com.example.codeexercise1.repository

import android.util.Log
import com.example.codeexercise1.db.ItemEntity
import com.example.codeexercise1.db.ItemsDao
import com.example.codeexercise1.util.retrofit.ApiService
import com.example.codeexercise1.util.serviceObjects.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ItemRepository @Inject constructor(
    private val apiService: ApiService,
    private val itemsDao: ItemsDao
) {

    fun getItems(): Flow<Map<Int, List<Item>>> = flow {
        if (!shouldFetchFromDb()) {
            fetchItemsFromApi()
        }
        emitAll(
            withContext(Dispatchers.IO) {
                fetchItemsFromDb()
            }
        )
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

    private fun fetchItemsFromDb(): Flow<Map<Int, List<Item>>> =
        itemsDao.getAllSorted()
            .map { items ->
                items.asSequence()  // Use sequences for more efficient transformations on larger datasets
                    .map { it.toDomain() }  // Convert each entity to domain model first
                    .groupBy { it.listId }  // Then group them by listId
                    .mapValues { (_, items) ->
                        items.sortedWith(compareBy<Item> { extractNumber(it.name) }
                            .thenBy { it.name })  // Sort items within each group
                    }
            }


    private suspend fun fetchItemsFromApi() =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.getJson()
                if (response.isSuccessful) {
                    val items = response.body() ?: emptyList()
                    itemsDao.deleteAll()
                    val filteredItems = items.filter { !it.name.isNullOrBlank() }
                    itemsDao.insertAll(filteredItems.map { it.toEntity() })

                } else {
                    Log.e("API Error", "Response Code: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "Error fetching data", e)
            }
        }


    private fun extractNumber(s: String?) = s?.filter { it.isDigit() }?.toIntOrNull()

}
