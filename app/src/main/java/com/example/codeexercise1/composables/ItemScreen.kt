package com.example.codeexercise1.composables

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.codeexercise1.viewModel.MainViewModel

@Composable
fun ItemScreen(modifier: Modifier, viewModel: MainViewModel) {
    val items = viewModel.items.collectAsState()

    LazyColumn(modifier = modifier) {
        items(items.value) { item ->
            Text(text = "Item ID: ${item.id}, Name: ${item.name ?: "No Name"}")
        }
    }
}