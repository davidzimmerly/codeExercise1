package com.example.codeexercise1.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codeexercise1.repository.ItemRepository
import com.example.codeexercise1.util.serviceObjects.Item
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val itemRepository: ItemRepository) : ViewModel() {
    private var _shouldSort = MutableStateFlow(false)
    private var _shouldFilter = MutableStateFlow(false)
    val shouldSort: StateFlow<Boolean> = _shouldSort
    val shouldFilter: StateFlow<Boolean> = _shouldFilter

    init {
        viewModelScope.launch { itemRepository.updateItems() }

    }

    fun setShouldFilter(value: Boolean) {
        _shouldFilter.value = value
    }

    fun setShouldSort(value: Boolean) {
        _shouldSort.value = value
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val items: Flow<Map<Int, List<Item>>> = combine(
        _shouldFilter,
        _shouldSort,
        itemRepository.items
    ) { shouldFilter, shouldSort, itemList ->
        itemList.filter { item -> if (shouldFilter) !item.name.isNullOrBlank() else true }
            .groupBy { it.listId }  // Then group them by listId
            .mapValues { (_, items) ->
                if (shouldSort) {
                    items.sortedWith(compareBy<Item> { extractNumber(it.name) }.thenBy { it.name })
                } else {
                    items
                }
            }
    }

    private fun extractNumber(s: String?) = s?.filter { it.isDigit() }?.toIntOrNull()
}