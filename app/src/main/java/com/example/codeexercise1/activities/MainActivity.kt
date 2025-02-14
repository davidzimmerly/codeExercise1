package com.example.codeexercise1.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.codeexercise1.composables.ItemScreen
import com.example.codeexercise1.ui.theme.CodeExercise1Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CodeExercise1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ItemScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}