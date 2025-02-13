package com.example.codeexercise1.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codeexercise1.util.retrofit.RetrofitInstance.apiService
import com.example.codeexercise1.util.serviceObjects.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items: StateFlow<List<Item>> = _items.asStateFlow()

    init {
        fetchItems()
    }

    private fun fetchItems() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val response = apiService.getJson()
            if (response.isSuccessful) {
                val items = response.body() ?: emptyList()
                _items.value = items
            } else {
                Log.e("API Error", "Response Code: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("API Exception", "Error fetching data", e)
        }
    }
}