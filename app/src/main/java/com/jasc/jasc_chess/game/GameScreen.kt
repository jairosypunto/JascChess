package com.jasc.jasc_chess.game

import androidx.lifecycle.viewmodel.compose.viewModel
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.jasc.jasc_chess.R
import com.jasc.jasc_chess.model.*
import com.jasc.jasc_chess.ui.components.TimerComponent

@Composable
fun GameScreen(viewModel: BoardViewModel = viewModel()) {
    val gameState by viewModel.gameState.collectAsState()
    val scrollState = rememberScrollState()

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? -> imageUri = uri }

    val temas = listOf(
        Pair(Color(0xFFF8FAFC), Color(0xFF1E3A8A)), Pair(Color(0xFFE5C185), Color(0xFF4A2E1B)),
        Pair(Color(0xFFE8EDDF), Color(0xFF2D5A27)), Pair(Color(0xFFF2F3F4), Color(0xFF708090)),
        Pair(Color(0xFFFFFDD0), Color(0xFF8B4513)), Pair(Color(0xFFD1E8E2), Color(0xFF2E8B57)),
        Pair(Color(0xFFF5E6CC), Color(0xFF5D4037)), Pair(Color(0xFFE0E0E0), Color(0xFF333333)),
        Pair(Color(0xFFFEF3C7), Color(0xFF92400E)), Pair(Color(0xFFFCE7F3), Color(0xFFBE185D)),
        Pair(Color(0xFFE0F2FE), Color(0xFF0369A1)), Pair(Color(0xFFFEE2E2), Color(0xFFB91C1C)),
        Pair(Color(0xFFFFF0F5), Color(0xFF800080)), Pair(Color(0xFFFDF5E6), Color(0xFFA0522D))
    )
    val colores = temas[gameState.temaActual % temas.size]

    Scaffold { paddingValues ->
        // 1. Usamos un Box principal para permitir superposiciones (Overlays)
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color(0xFF0F1E36))) {

            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Perfil
                Surface(modifier = Modifier.padding(top = 16.dp, bottom = 20.dp).size(60.dp).clip(CircleShape).border(2.dp, Color(0xFFFFD700), CircleShape).clickable { launcher.launch("image/*") }, color = Color(0xFF1E293B)) {
                    if (imageUri != null) AsyncImage(model = imageUri, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                    else Box(contentAlignment = Alignment.Center) { Text("👤", fontSize = 28.sp) }
                }

                // Info partida
                Column(modifier = Modifier.fillMaxWidth().background(Color(0x1F000000)), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.padding(top = 16.dp, bottom = 12.dp).background(Color(0xFF1E293B), RoundedCornerShape(20.dp)).border(1.dp, Color(0xFFFFD700), RoundedCornerShape(20.dp)).padding(horizontal = 24.dp, vertical = 6.dp).clickable { viewModel.cambiarDificultad() }) {
                        Text(text = "NIVEL: ${gameState.nivelActual.name}", color = Color(0xFFFFD700), fontSize = 12.sp, fontWeight = FontWeight.Black)
                    }
                    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp).shadow(4.dp, RoundedCornerShape(12.dp)).background(Color(0xFF1E293B), RoundedCornerShape(12.dp)).clickable { viewModel.alternarModoTiempo() }) {
                        if (gameState.modoTiempoActivado) TimerComponent(gameState.oroTimeMillis, gameState.plataTimeMillis, gameState.currentTurn == PieceColor.ORO, Modifier.fillMaxWidth())
                        else Box(modifier = Modifier.fillMaxWidth().height(80.dp), contentAlignment = Alignment.Center) { Text(text = "⏱️ TIEMPO DESACTIVADO", color = Color(0xFF94A3B8), fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    }
                    Text(text = if (gameState.currentTurn == PieceColor.ORO) "TURNO: IMPERIO 👑" else "TURNO: IA PLATA ⚔️", color = if (gameState.currentTurn == PieceColor.ORO) Color(0xFFF59E0B) else Color(0xFF38BDF8), fontSize = 13.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(vertical = 12.dp))
                }
                if (gameState.esJaque) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 4.dp)
                            .background(Color.Red.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("¡JAQUE!", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
                // Tablero completo
                Box(modifier = Modifier.padding(horizontal = 1.dp, vertical = 1.dp).fillMaxWidth().aspectRatio(1f).shadow(20.dp, RoundedCornerShape(6.dp)).background(Color(0xFF4A2E1B)).border(4.dp, Color(0xFF2D1B10), RoundedCornerShape(6.dp)).padding(start = 4.dp, end = 4.dp, bottom = 4.dp, top = 12.dp), contentAlignment = Alignment.Center) {
                    Row(modifier = Modifier.fillMaxSize()) {
                        Column(modifier = Modifier.fillMaxHeight().width(12.dp).padding(vertical = 4.dp), verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.CenterHorizontally) {
                            for (i in gameState.boardSize downTo 1) Text(text = i.toString(), color = Color(0xFFE2E8F0), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
                            for (row in 0 until gameState.boardSize) {
                                Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
                                    for (col in 0 until gameState.boardSize) {
                                        val currentPos = Position(row, col)
                                        CasillaView(currentPos, gameState, colores) {
                                            if (gameState.modoJuego == GameMode.PUZZLE) {
                                                if (gameState.selectedPosition == null) viewModel.onCellSelected(currentPos)
                                                else viewModel.validarJugadaPuzzle(gameState.selectedPosition!!, currentPos)
                                            } else { viewModel.onCellSelected(currentPos) }
                                        }
                                    }
                                }
                            }
                            Row(modifier = Modifier.fillMaxWidth().height(18.dp).padding(bottom = 2.dp), horizontalArrangement = Arrangement.SpaceAround) {
                                listOf("a", "b", "c", "d", "e", "f", "g", "h").take(gameState.boardSize).forEach { Text(text = it, color = Color(0xFFE2E8F0), fontSize = 10.sp, fontWeight = FontWeight.Black) }
                            }
                        }
                    }
                }

                CementerioRow("CAPTURAS IA: ", gameState.piezasComidasPlata, gameState)
                CementerioRow("CAPTURAS JUGADOR: ", gameState.piezasComidasOro, gameState)

                // Botones
                if (gameState.modoJuego == GameMode.PUZZLE) {
                    Button(onClick = { viewModel.siguientePuzzle() }, modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 8.dp)) { Text("SIGUIENTE PUZZLE") }
                    Button(onClick = { viewModel.configurarPartida(8, GameMode.LIBRE) }, modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A))) { Text("VOLVER A PARTIDA LIBRE") }
                } else {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { viewModel.configurarPartida(8, GameMode.PUZZLE) }, modifier = Modifier.weight(1f)) { Text("PRÁCTICA 8X8") }
                        OutlinedButton(onClick = { viewModel.configurarPartida(4, GameMode.PUZZLE) }, modifier = Modifier.weight(1f)) { Text("PUZZLE 4X4") }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                    BotonCircularMedieval({ viewModel.reiniciarPartida() }, "🔄", "REINICIAR", Color(0xFFB45309))
                    BotonCircularMedieval({ viewModel.cambiarDificultad() }, "🏆", "NIVEL", Color(0xFF6D28D9))
                    BotonCircularMedieval({ viewModel.cambiarEstiloFichas() }, "♟️", "PIEZAS", Color(0xFF1E3A8A))
                    BotonCircularMedieval({ viewModel.deshacerJugada() }, "↩️", "DESHACER", Color(0xFF78350F))
                    BotonCircularMedieval({ viewModel.obtenerPistaAyuda() }, "💡", "PISTA", Color(0xFFD97706))
                }
                FooterFirma()
                Spacer(modifier = Modifier.height(40.dp))
            }

            // 2. AVISOS DE ESTADO (Jaque Mate / Tablas) - SE DIBUJAN SOBRE EL TABLERO
            if (gameState.esJaqueMate) {
                Surface(color = Color.Black.copy(alpha = 0.85f), modifier = Modifier.fillMaxSize()) {
                    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("¡JAQUE MATE!", color = Color.Red, fontSize = 48.sp, fontWeight = FontWeight.Black)
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(onClick = { viewModel.reiniciarPartida() }) { Text("JUGAR DE NUEVO") }
                    }
                }
// Corrección del overlay de Tablas en GameScreen.kt
            } else if (gameState.esTablas || gameState.esAhogado) {
                Surface(color = Color.Black.copy(alpha = 0.8f), modifier = Modifier.fillMaxSize()) {
                    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("¡JUEGO TERMINADO: TABLAS!", color = Color.Yellow, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(onClick = { viewModel.reiniciarPartida() }) { Text("JUGAR DE NUEVO") }
                    }
                }
            }
        }
    }
}
@Composable
fun RowScope.CasillaView(position: Position, gameState: GameState, colores: Pair<Color, Color>, onClick: () -> Unit) {
    val isDarkCell = (position.row + position.col) % 2 == 1
    val piece = gameState.pieces.find { it.position == position }

    Box(
        modifier = Modifier.weight(1f).fillMaxHeight().background(
            when {
                gameState.selectedPosition == position -> Color(0xB3F59E0B)
                gameState.validMoves.contains(position) -> Color(0xAA10B981)
                isDarkCell -> colores.second
                else -> colores.first
            }
        ).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        piece?.let { p ->
            val resId = obtenerResourcePieza(p.type, p.color, gameState.estiloSeleccionado)
            if (resId != null) {
                Image(painter = painterResource(id = resId), contentDescription = null, modifier = Modifier.fillMaxSize())
            } else {
                // AQUÍ ESTABA EL ERROR: No estabas llamando al símbolo si no había imagen
                Text(
                    text = obtenerSimboloTexto(p.type, gameState.estiloSeleccionado, p.color),
                    fontSize = 24.sp
                )
            }
        }
    }
}

// MANTENEMOS TUS FUNCIONES EXACTAS: SIN CAMBIOS NI MODIFICACIONES
@Composable
fun CementerioRow(label: String, piezas: List<ChessPiece>, gameState: GameState) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp).background(Color(0x33FFFFFF), RoundedCornerShape(6.dp)).padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
        piezas.forEach { pieza ->
            val resId = obtenerResourcePieza(pieza.type, pieza.color, gameState.estiloSeleccionado)
            if (resId != null) Image(painter = painterResource(id = resId), contentDescription = null, modifier = Modifier.size(22.dp))
            else Text(text = obtenerSimboloTexto(pieza.type, gameState.estiloSeleccionado, pieza.color), fontSize = 18.sp, color = Color.White)
        }
    }
}

@Composable
fun FooterFirma() {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalDivider(modifier = Modifier.width(40.dp), thickness = 5.dp, color = Color(0xFF64748B))
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "DESARROLLADO POR JAIRO SALAZAR CASTAÑO", fontSize = 7.sp, fontWeight = FontWeight.Black, color = Color.White)
        Text(text = "© 2026 JascChess Pro", fontSize = 9.sp, color = Color(0xFF64748B))
    }
}

@Composable
fun BotonCircularMedieval(onClick: () -> Unit, icono: String, rotulo: String, fondoColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.size(54.dp).shadow(6.dp, CircleShape).clip(CircleShape).background(fondoColor).border(2.dp, Color(0xFFFFD700), CircleShape).clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(text = icono, fontSize = 22.sp)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = rotulo, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF94A3B8))
    }
}

private fun obtenerResourcePieza(tipo: PieceType, color: PieceColor, estilo: EstiloFichas): Int? {
    return when (estilo) {
        EstiloFichas.TRADICIONAL -> if (color == PieceColor.ORO) {
            when (tipo) {
                PieceType.PEON -> R.drawable.tradicional_peon_blanco; PieceType.TORRE -> R.drawable.tradicional_torre_blanca
                PieceType.CABALLO -> R.drawable.tradicional_caballo_blanco; PieceType.ALFIL -> R.drawable.tradicional_alfil_blanco
                PieceType.REINA -> R.drawable.tradicional_reina_blanca; PieceType.REY -> R.drawable.tradicional_rey_blanco
            }
        } else {
            when (tipo) {
                PieceType.PEON -> R.drawable.tradicional_peon_negro; PieceType.TORRE -> R.drawable.tradicional_torre_negra
                PieceType.CABALLO -> R.drawable.tradicional_caballo_negro; PieceType.ALFIL -> R.drawable.tradicional_alfil_negro
                PieceType.REINA -> R.drawable.tradicional_reina_negra; PieceType.REY -> R.drawable.tradicional_rey_negro
            }
        }
        EstiloFichas.ROMANO -> if (color == PieceColor.ORO) {
            when (tipo) {
                PieceType.PEON -> R.drawable.romano_peon_blanco; PieceType.TORRE -> R.drawable.romano_torre_blanca
                PieceType.CABALLO -> R.drawable.romano_caballo_blanco; PieceType.ALFIL -> R.drawable.romano_alfil_blanco
                PieceType.REINA -> R.drawable.romano_reina_blanca; PieceType.REY -> R.drawable.romano_rey_blanco
            }
        } else {
            when (tipo) {
                PieceType.PEON -> R.drawable.romano_peon_negro; PieceType.TORRE -> R.drawable.romano_torre_negra
                PieceType.CABALLO -> R.drawable.romano_caballo_negro; PieceType.ALFIL -> R.drawable.romano_alfil_negro
                PieceType.REINA -> R.drawable.romano_reina_negra; PieceType.REY -> R.drawable.romano_rey_negro
            }
        }
        else -> null
    }
}

private fun obtenerSimboloTexto(tipo: PieceType, estilo: EstiloFichas, color: PieceColor): String {
    return when (estilo) {
        EstiloFichas.EGIPCIO -> if (color == PieceColor.ORO) when(tipo){ PieceType.REY->"👑"; PieceType.REINA->"☀️"; PieceType.TORRE->"🔺"; PieceType.CABALLO->"🦁"; PieceType.ALFIL->"🏹"; PieceType.PEON->"🏺" } else when(tipo){ PieceType.REY->"🐈"; PieceType.REINA->"🧙‍♀️"; PieceType.TORRE->"🧱"; PieceType.CABALLO->"🐪"; PieceType.ALFIL->"🔱"; PieceType.PEON->"🗿" }
        EstiloFichas.GLADIADOR -> if (color == PieceColor.ORO) when(tipo){ PieceType.REY->"👑"; PieceType.REINA->"👸"; PieceType.TORRE->"🏛️"; PieceType.CABALLO->"🐎"; PieceType.ALFIL->"🔱"; PieceType.PEON->"🛡️" } else when(tipo){ PieceType.REY->"🪖"; PieceType.REINA->"🧙‍♀️"; PieceType.TORRE->"🏰"; PieceType.CABALLO->"🐴"; PieceType.ALFIL->"🏹"; PieceType.PEON->"🪓" }
        else -> ""
    }
}