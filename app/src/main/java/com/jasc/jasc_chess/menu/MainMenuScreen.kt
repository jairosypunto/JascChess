package com.jasc.jasc_chess.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape // <-- IMPORTANTE
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current
    val buttonShape = RoundedCornerShape(50) // Definimos la forma aquí una sola vez

    // Modificador común para botones profesionales
    val buttonModifier = Modifier
        .fillMaxWidth(0.75f)
        .height(60.dp)
        .shadow(10.dp, buttonShape)

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.45f)))

        // Perfil (Top End)
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
                painter = painterResource(id = R.drawable.perfil2),
                contentDescription = "Ir a Perfil",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Historia (Top Start)
        TextButton(
            onClick = { navController.navigate("history_screen") },
            modifier = Modifier.align(Alignment.TopStart).padding(24.dp)
        ) {
            Icon(Icons.Default.Info, contentDescription = null, tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Historia", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }

        // Contenido Central
        Column(
            modifier = Modifier.fillMaxSize().padding(top = 300.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "JASC CHESS",
                fontSize = 42.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            // Botón 1: Partida Libre
            Button(
                onClick = { boardViewModel.cargarModo(8, GameMode.LIBRE, null); navController.navigate("juego") },
                modifier = buttonModifier,
                shape = buttonShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0843C))
            ) {
                Text("Jugar Partida Libre", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Botón 2: Prácticas
            Button(
                onClick = { boardViewModel.iniciarModoPractica(context, true); navController.navigate("juego") },
                modifier = buttonModifier,
                shape = buttonShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB45309))
            ) {
                Text("Prácticas Profesionales", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Botón 3: Niveles
            Button(
                onClick = { navController.navigate("selector_niveles") },
                modifier = buttonModifier,
                shape = buttonShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4D7C0F))
            ) {
                Text("Seleccionar Nivel", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            // Espaciador flexible para empujar el logo al fondo
            Spacer(modifier = Modifier.weight(1f))

            // Logo en la base
            Image(
                painter = painterResource(id = R.drawable.logoprincipal),
                contentDescription = "Logo",
                modifier = Modifier.size(300.dp).padding(bottom = 16.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}