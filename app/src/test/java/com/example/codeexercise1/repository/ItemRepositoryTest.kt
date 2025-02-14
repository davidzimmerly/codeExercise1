package com.example.codeexercise1.repository

import com.example.codeexercise1.db.ItemEntity
import com.example.codeexercise1.db.ItemsDao
import com.example.codeexercise1.util.retrofit.ApiService
import com.example.codeexercise1.util.serviceObjects.Item
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class ItemRepositoryTest {

    private lateinit var itemRepository: ItemRepository
    @MockK private lateinit var apiService: ApiService
    @MockK private lateinit var itemsDao: ItemsDao

    val itemEntities = listOf(
        ItemEntity(id = 2, listId = 1, name = "Item 10"),
        ItemEntity(id = 1, listId = 1, name = "Item 2"),
        ItemEntity(id = 3, listId = 2, name = "Item 10"),
        ItemEntity(id = 4, listId = 2, name = "Item 12"),
        ItemEntity(id = 5, listId = 2, name = "Item 2"),
    )
    val flowOfItems = flowOf(itemEntities)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        itemRepository = ItemRepository(apiService, itemsDao)
        coEvery { itemsDao.getAllSorted() } returns flowOfItems
    }

    @Test
    fun `When DB is not empty, fetch from DB and verify grouping`() = runTest {
        coEvery { itemsDao.countItems() } returns 50  // DB is not empty

        val results = itemRepository.getItems().toList()

        // sorting by just name yielded stuff like Item 10, Item 2, Item 30, so I took extra step here to sort by number
        assertEquals(
            mapOf(
                1 to listOf(
                    Item(id = 1, listId = 1, name = "Item 2"),
                    Item(id = 2, listId = 1, name = "Item 10")
                ),
                2 to listOf(
                    Item(id = 5, listId = 2, name = "Item 2"),
                    Item(id = 3, listId = 2, name = "Item 10"),
                    Item(id = 4, listId = 2, name = "Item 12"),
                ),
            ),
            results[0]
        )
    }

    @Test
    fun `getItems fetches from API when DB is empty and inserts into DB`() = runTest {
        val apiItems =
            listOf(Item(id = 1, listId = 1, name = "Item 1"), Item(id = 2, listId = 2, name = null))
        coEvery { itemsDao.countItems() } returns 0  // DB is empty
        coEvery { apiService.getJson() } returns Response.success(apiItems)
        coEvery { itemsDao.deleteAll() } coAnswers {}
        coEvery { itemsDao.insertAll(any()) } coAnswers {}
        val captureSlot = slot<List<ItemEntity>>()

        val results = itemRepository.getItems().toList()
        assertEquals(1, results.size)

        // Validate interactions & no null name insertions
        coVerify { itemsDao.deleteAll() }
        coVerify { itemsDao.insertAll(capture(captureSlot)) }

        val inserted = captureSlot.captured
        assertEquals(1, inserted.size)
        assertEquals(1, inserted[0].listId)
        assertEquals(1, inserted[0].id)
        assertEquals("Item 1", inserted[0].name)
    }
}
