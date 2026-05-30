package com.jasc.jasc_chess.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jasc.jasc_chess.R
import com.jasc.jasc_chess.game.BoardViewModel
import com.jasc.jasc_chess.model.GameMode

@Composable
fun MainMenuScreen(
    navController: NavController,
    boardViewModel: BoardViewModel
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.fondo), contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)))

        IconButton(
            onClick = { navController.navigate("perfil") },
            modifier = Modifier.align(Alignment.TopEnd).padding(24.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f))
        ) {
            Icon(Icons.Default.Person, contentDescription = "Perfil", tint = Color.White)
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(id = R.drawable.tradicional_caballo_negro), contentDescription = "Logo", modifier = Modifier.size(120.dp).padding(bottom = 24.dp))
            Text(text = "JASC CHESS", fontSize = 32.sp, fontWeight = FontWeight.Black, color = Color.White, modifier = Modifier.padding(bottom = 48.dp))

            val btnColor = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B))
            val accentColor = ButtonDefaults.buttonColors(containerColor = Color(0xFFB45309))

            // JUGAR PARTIDA LIBRE
            Button(
                onClick = {
                    boardViewModel.prepararJuego(8, GameMode.LIBRE, false)
                    navController.navigate("juego")
                },
                modifier = Modifier.fillMaxWidth(0.6f).height(50.dp), colors = btnColor
            ) { Text("Jugar Partida Libre", fontSize = 16.sp) }

            Spacer(modifier = Modifier.height(16.dp))

            // PRÁCTICA 8X8 (PUZZLE)
            Button(
                onClick = {
                    boardViewModel.prepararJuego(8, GameMode.PUZZLE, true)
                    boardViewModel.iniciarNivelDePrueba()
                    navController.navigate("juego")
                },
                modifier = Modifier.fillMaxWidth(0.6f).height(50.dp), colors = btnColor
            ) { Text("Prácticas (8x8)", fontSize = 16.sp) }

            Spacer(modifier = Modifier.height(16.dp))

            // PRÁCTICA 4X4 (PUZZLE)
            Button(
                onClick = {
                    boardViewModel.prepararJuego(4, GameMode.PUZZLE, true)
                    navController.navigate("juego")
                },
                modifier = Modifier.fillMaxWidth(0.6f).height(50.dp), colors = accentColor
            ) { Text("Prácticas (4x4)", fontSize = 16.sp) }
        }
    }
}