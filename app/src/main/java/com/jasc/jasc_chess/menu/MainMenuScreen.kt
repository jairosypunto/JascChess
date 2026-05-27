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

@Composable
fun MainMenuScreen(
    navController: NavController,
    boardViewModel: BoardViewModel
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // 1. IMAGEN DE FONDO
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Capa de oscurecimiento sutil para que el texto resalte
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)))

        // 2. ICONO DE PERFIL (Disimulado y elegante)
        IconButton(
            onClick = { navController.navigate("perfil") },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(24.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.2f))
        ) {
            Icon(Icons.Default.Person, contentDescription = "Perfil", tint = Color.White)
        }

        // 3. CONTENIDO CENTRAL
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logojasc),
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp).padding(bottom = 24.dp)
            )

            Text(
                text = "JASC CHESS",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            // Botones con estilo terracota (personaliza el color aquí)
            val btnColor = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B))

            Button(
                onClick = { boardViewModel.resetToLibre(); navController.navigate("juego") },
                modifier = Modifier.fillMaxWidth(0.6f).height(50.dp),
                colors = btnColor
            ) {
                Text("Jugar Partida Libre", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { boardViewModel.iniciarNivelDePrueba(); navController.navigate("juego") },
                modifier = Modifier.fillMaxWidth(0.6f).height(50.dp),
                colors = btnColor
            ) {
                Text("Ir a Prácticas", fontSize = 16.sp)
            }
        }
    }
}