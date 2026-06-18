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
import com.jasc.jasc_chess.game.BoardViewModel
import com.jasc.jasc_chess.data.local.PreferencesManager
import com.jasc.jasc_chess.data.local.NivelRepository // <--- ESTO FALTA
import androidx.compose.foundation.shape.RoundedCornerShape // <-- IMPORTACIÓN NECESARIA
import androidx.compose.runtime.mutableIntStateOf // <-- OPTIMIZACIÓN

@Composable
fun SelectorNivelesScreen(navController: NavController, viewModel: BoardViewModel) {
    val context = LocalContext.current

    // Usamos 'remember' con 'mutableIntStateOf' para mantener los valores
    val nivelMaximo = remember { mutableIntStateOf(PreferencesManager.obtenerNivelMaximo(context)) }
    val puntosTotales = remember { mutableIntStateOf(PreferencesManager.obtenerPuntos(context)) }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF0F172A))) {
        // Header de Puntos
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, end = 20.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Surface(
                color = Color(0xFF1E293B),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color.Yellow)
            ) {
                Text(
                    text = "💰 PUNTOS: ${puntosTotales.intValue}",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = Color.Yellow,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        // Lista de niveles
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 80.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text("CAMINO DEL IMPERIO", color = Color.Yellow, fontSize = 24.sp, fontWeight = FontWeight.Black)
                Spacer(modifier = Modifier.height(30.dp))
            }

            items(NivelRepository.totalNiveles.size) { index ->
                val nivel = index + 1
                val estaDesbloqueado = nivel <= nivelMaximo.intValue
                val estaCompletado = nivel < nivelMaximo.intValue

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
                            // IMPORTANTE: Al llamar a cargarPartida, el ViewModel ya
                            // actualiza el State con maxPasosConfigurado y el nivel correcto.
                            viewModel.cargarPartida(nivel)

                            // Navegamos inmediatamente después.
                            navController.navigate("juego")
                        }
                    }
                )

                // Conector visual entre niveles
                if (nivel < NivelRepository.totalNiveles.size) {
                    Box(
                        modifier = Modifier
                            .height(40.dp)
                            .width(4.dp)
                            .background(if (estaCompletado) Color.Yellow else Color.Gray.copy(alpha = 0.3f))
                    )
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
    // Lógica para detectar si es nivel 10, 20, 30...
    val esNivelEspecial = nivel % 10 == 0

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.padding(vertical = 8.dp)) {
        Surface(
            modifier = Modifier
                .size(75.dp)
                .clip(CircleShape)
                .clickable(enabled = estaDesbloqueado) { onClick() },
            // CAMBIO: Si es especial, usamos dorado; si no, tu lógica original
            color = when {
                esNivelEspecial -> Color(0xFFFFD700) // Dorado para niveles de coronación
                estaCompletado -> Color(0xFF10B981) // Verde para completado
                estaDesbloqueado -> Color(0xFF6D28D9) // Morado para activo
                else -> Color(0xFF334155) // Gris para bloqueado
            },
            border = if (estaDesbloqueado) BorderStroke(3.dp, Color.White) else null
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (estaDesbloqueado) {
                    // CAMBIO: Si es especial, mostramos corona; si no, número o check
                    Text(
                        text = when {
                            esNivelEspecial -> "👑"
                            estaCompletado -> "✓"
                            else -> nivel.toString()
                        },
                        color = if (esNivelEspecial) Color.Black else Color.White,
                        fontSize = if (esNivelEspecial) 30.sp else 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Icon(Icons.Default.Lock, contentDescription = "Bloqueado", tint = Color.Gray)
                }
            }
        }
        Text(
            text = "ETAPA $nivel",
            color = if (estaDesbloqueado) Color.White else Color.Gray,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}