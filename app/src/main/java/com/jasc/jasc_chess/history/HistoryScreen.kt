package com.jasc.jasc_chess.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class) // Necesario para TopAppBar en Material3
@Composable
fun HistoryScreen(navController: NavController, viewModel: HistoryViewModel = viewModel()) {
    Scaffold(
        topBar = {
            TopAppBar( // Usamos el componente estándar
                title = { Text("Historia del Ajedrez") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = viewModel.historiaTexto,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.DarkGray
            )
        }
    }
}