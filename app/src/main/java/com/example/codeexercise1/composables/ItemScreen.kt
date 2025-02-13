package com.example.codeexercise1.composables

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.codeexercise1.util.serviceObjects.Item
import com.example.codeexercise1.viewModel.MainViewModel

@Composable
fun ItemScreen(
    modifier: Modifier,
    viewModel: MainViewModel = hiltViewModel<MainViewModel>(),
) {
    val itemsState = viewModel.items.collectAsState(emptyMap())
    ItemListView(modifier = modifier, items = itemsState.value)
}

@Composable
fun ItemListView(modifier: Modifier, items: Map<Int, List<Item>>) =
    LazyColumn(modifier = modifier) {
        items.forEach { (listId, itemList) ->
            item {
                Text(text = "List ID: $listId")
            }
            items(itemList) { item ->
                Text(text = "List ID: ${item.listId}, Name: ${item.name}, Item ID: ${item.id}")
            }
        }
    }
