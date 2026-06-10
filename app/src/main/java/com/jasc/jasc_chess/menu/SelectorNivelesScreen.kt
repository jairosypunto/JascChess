package com.jasc.jasc_chess.menu


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jasc.jasc_chess.data.PreferencesManager
import com.jasc.jasc_chess.game.BoardViewModel

@Composable
fun SelectorNivelesScreen(navController: NavController, viewModel: BoardViewModel) {
    val context = LocalContext.current
    val nivelMaximo = remember { mutableStateOf(PreferencesManager.obtenerNivelMaximo(context)) }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF0F172A))) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 40.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text("CAMINO DEL IMPERIO", color = Color.Yellow, fontSize = 24.sp, fontWeight = FontWeight.Black)
                Spacer(modifier = Modifier.height(30.dp))
            }

            items(16) { index ->
                val nivel = index + 1
                val estaDesbloqueado = nivel <= nivelMaximo.value
                val estaCompletado = nivel < nivelMaximo.value

                val horizontalPadding = when (index % 4) {
                    1 -> 100.dp
                    3 -> (-100).dp
                    else -> 0.dp
                }

                NivelNode(
                    nivel = nivel,
                    estaDesbloqueado = estaDesbloqueado,
                    estaCompletado = estaCompletado,
                    modifier = Modifier.offset(x = horizontalPadding),
                    onClick = {
                        if (estaDesbloqueado) {
                            // Llamada a la lógica del juego
                            viewModel.cargarPartida(nivel)
                            navController.navigate("juego")
                        }
                    }
                )

                if (nivel < 16) {
                    Box(modifier = Modifier.height(40.dp).width(4.dp).background(if (estaCompletado) Color.Yellow else Color.Gray.copy(alpha = 0.3f)))
                }
            }
        }
    }
}

@Composable
fun NivelNode(
    nivel: Int,
    estaDesbloqueado: Boolean,
    estaCompletado: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Surface(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .clickable(enabled = estaDesbloqueado) { onClick() },
            color = when {
                estaCompletado -> Color(0xFFF59E0B)
                estaDesbloqueado -> Color(0xFF6D28D9)
                else -> Color(0xFF334155)
            },
            border = if (estaDesbloqueado) BorderStroke(4.dp, Color.White.copy(alpha = 0.5f)) else null
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (estaDesbloqueado) {
                    Text(
                        text = if (estaCompletado) "✓" else nivel.toString(),
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        Text(
            text = "ETAPA $nivel",
            color = if (estaDesbloqueado) Color.White else Color.Gray,
            fontSize = 10.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}