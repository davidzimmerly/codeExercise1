package com.example.codeexercise1.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import com.example.codeexercise1.util.serviceObjects.Item
import com.example.codeexercise1.viewModel.MainViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Rule
import org.junit.Test

class ItemScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testFlowType() {
        val mockViewModel = mockk<MainViewModel>()
        val itemsFlow = MutableStateFlow(mapOf(1 to listOf(Item(id = 1, listId = 1, name = "Item 1"))))
        every { mockViewModel.items } returns itemsFlow.asStateFlow()

        composeTestRule.setContent {
            ItemScreen(modifier = Modifier.fillMaxSize(), viewModel = mockViewModel)
        }

        composeTestRule.onAllNodesWithText("List ID: 1").assertCountEquals(2)
        composeTestRule.onNodeWithText("Item 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Item ID: 1").assertIsDisplayed()
    }
}