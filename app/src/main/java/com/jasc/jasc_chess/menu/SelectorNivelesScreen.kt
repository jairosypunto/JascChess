package com.jasc.jasc_chess.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jasc.jasc_chess.data.local.NivelRepository
import com.jasc.jasc_chess.game.BoardViewModel

@Composable
fun SelectorNivelesScreen(navController: NavController, boardViewModel: BoardViewModel) {
    // Obtenemos las llaves del mapa de niveles y las ordenamos
    val niveles = NivelRepository.totalNiveles.keys.sorted()

    LazyVerticalGrid(
        columns = GridCells.Fixed(3), // 3 columnas
        contentPadding = PaddingValues(16.dp)
    ) {
        items(niveles) { nivelId ->
            // En SelectorNivelesScreen.kt
            Button(
                onClick = {
                    // Borramos el argumento 'size' que sobra
                    boardViewModel.cargarPartida(nivelId)
                    navController.navigate("juego")
                },
                modifier = Modifier.padding(8.dp).aspectRatio(1f)
            ) {
                Text("Nivel $nivelId")
            }
        }
    }
}