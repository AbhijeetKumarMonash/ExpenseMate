package com.example.expensemate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.expensemate.navigation.ExpenseMateApp
import com.example.expensemate.ui.theme.ExpenseMateTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensemate.viewmodel.ExpenseViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[ExpenseViewModel::class.java]

        setContent {
            ExpenseMateTheme {
                ExpenseMateApp(viewModel)
            }
        }
    }
}