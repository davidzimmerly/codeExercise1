package com.example.codeexercise1.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codeexercise1.repository.ItemRepository
import com.example.codeexercise1.util.serviceObjects.Item
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(itemRepository: ItemRepository) : ViewModel() {
    val items: Flow<Map<Int, List<Item>>> = itemRepository.getItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyMap()
        )
}