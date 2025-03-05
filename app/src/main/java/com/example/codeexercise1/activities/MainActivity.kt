package com.example.codeexercise1.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.codeexercise1.composables.ItemScreen
import com.example.codeexercise1.ui.theme.CodeExercise1Theme
import com.example.codeexercise1.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val vm by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CodeExercise1Theme {
                val shouldFilter = vm.shouldFilter.collectAsState(false)
                val shouldSort = vm.shouldSort.collectAsState(false)
                Scaffold(modifier = Modifier.padding(top = 32.dp)) { innerPadding ->
                    Row {
                        Button(onClick = { vm.setShouldFilter(!shouldFilter.value) }) {
                            Text(text = "Filtering: ${shouldFilter.value}")
                        }
                        Button(onClick = { vm.setShouldSort(!shouldSort.value) }) {
                            Text(text = "Sorting: ${shouldSort.value}")
                        }
                    }
                    ItemScreen(modifier = Modifier.padding(innerPadding))
                }
            }

        }
    }
}
