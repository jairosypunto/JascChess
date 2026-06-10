package com.jasc.jasc_chess.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
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

// --- 2. ESQUINA SUPERIOR DERECHA: TU LOGO COMO BOTÓN DE PERFIL ---

// Aquí reemplazamos el Icon(Icons.Default.Person) por tu imagen

        IconButton(

            onClick = { navController.navigate("perfil") },

            modifier = Modifier

                .align(Alignment.TopEnd)

                .padding(32.dp)

                .size(50.dp)

                .clip(CircleShape)

                .background(Color.White.copy(alpha = 0.2f))

        ) {

            Image(

                painter = painterResource(id = R.drawable.perfil2), // El mismo logo

                contentDescription = "Ir a Perfil",

                contentScale = ContentScale.Crop,

                modifier = Modifier.fillMaxSize()

            )

        }

        // --- BOTÓN DISIMULADO DE HISTORIA (NUEVO) ---
        // Ubicado en la parte superior izquierda, sutil y elegante
        TextButton(
            onClick = { navController.navigate("history_screen") },
            modifier = Modifier.align(Alignment.TopStart).padding(24.dp)
        ) {
            Icon(Icons.Default.Info, contentDescription = null, tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Historia", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "JASC CHESS", fontSize = 32.sp, fontWeight = FontWeight.Black, color = Color.White, modifier = Modifier.padding(bottom = 12.dp))
            Image(painter = painterResource(id = R.drawable.logoprincipal), contentDescription = "Logo", modifier = Modifier.size(100.dp).padding(bottom = 4.dp))

            val btnColor = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B))
            val accentColor = ButtonDefaults.buttonColors(containerColor = Color(0xFFB45309))

            // 1. JUGAR PARTIDA LIBRE
            Button(
                onClick = {
                    boardViewModel.cargarModo(8, GameMode.LIBRE, null)
                    navController.navigate("juego")
                },
                modifier = Modifier.fillMaxWidth(0.6f).height(50.dp), colors = btnColor
            ) { Text("Jugar Partida Libre", fontSize = 16.sp) }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. PRÁCTICA 4X4
            Button(
                onClick = {
                    boardViewModel.cargarModo(4, GameMode.LIBRE, null)
                    navController.navigate("juego")
                },
                modifier = Modifier.fillMaxWidth(0.6f).height(50.dp), colors = accentColor
            ) { Text("Prácticas (4x4)", fontSize = 16.sp) }

            // 4. SELECCIONAR NIVEL (Nuevo botón)
            Button(
                onClick = { navController.navigate("selector_niveles") },
                modifier = Modifier.fillMaxWidth(0.6f).height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A6741)) // Color verde oscuro para diferenciar
            ) {
                Text("Seleccionar Nivel", fontSize = 16.sp)
            }
        }
    }
}