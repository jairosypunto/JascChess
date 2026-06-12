package com.jasc.jasc_chess.game

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale // <-- SOLUCIONA ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource // <-- SOLUCIONA painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign // <-- SOLUCIONA TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage

// Importaciones de tus componentes locales
import com.jasc.jasc_chess.R
import com.jasc.jasc_chess.model.*
import com.jasc.jasc_chess.ui.components.TimerComponent // <-- SOLUCIONA TimerComponent
import com.jasc.jasc_chess.ui.components.ChessPieceView // <-- SOLUCIONA ChessPieceView
import com.jasc.jasc_chess.audio.SoundManager
import com.jasc.jasc_chess.audio.VideoPlayer


@Composable
fun GameScreen(
    viewModel: BoardViewModel,
    navController: NavController
) {
    val gameState by viewModel.gameState.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current

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
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color(0xFF0F1E36))) {
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Perfil
                Surface(modifier = Modifier.padding(top = 12.dp, bottom = 20.dp).size(60.dp).clip(CircleShape).border(2.dp, Color(0xFFFFD700), CircleShape), color = Color(0xFF1E293B)) {
                    Box(contentAlignment = Alignment.Center) { Text("👤", fontSize = 28.sp) }
                }

                // Info partida
                Column(modifier = Modifier.fillMaxWidth().background(Color(0x1F000000)), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.padding(top = 12.dp, bottom = 12.dp).background(Color(0xFF1E293B), RoundedCornerShape(20.dp)).border(1.dp, Color(0xFFFFD700), RoundedCornerShape(20.dp)).padding(horizontal = 24.dp, vertical = 6.dp).clickable { viewModel.cambiarDificultad() }) {
                        Text(text = "NIVEL: ${gameState.nivelActual.name}", color = Color(0xFFFFD700), fontSize = 12.sp, fontWeight = FontWeight.Black)
                    }
                    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp).shadow(4.dp, RoundedCornerShape(12.dp)).background(Color(0xFF1E293B), RoundedCornerShape(12.dp)).clickable { viewModel.alternarModoTiempo() }) {
                        if (gameState.modoTiempoActivado) TimerComponent(gameState.oroTimeMillis, gameState.plataTimeMillis, gameState.currentTurn == PieceColor.ORO, Modifier.fillMaxWidth())
                        else Box(modifier = Modifier.fillMaxWidth().height(80.dp), contentAlignment = Alignment.Center) { Text(text = "⏱️ TIEMPO DESACTIVADO", color = Color(0xFF94A3B8), fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    }
                    Text(text = if (gameState.currentTurn == PieceColor.ORO) "TURNO: IMPERIO 👑" else "TURNO: IA PLATA ⚔️", color = if (gameState.currentTurn == PieceColor.ORO) Color(0xFFF59E0B) else Color(0xFF38BDF8), fontSize = 13.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(vertical = 12.dp))
                }

                CementerioRow("IMPERIO (ORO): ", gameState.piezasComidasOro, gameState)

                // Tablero
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
                                        CasillaView(currentPos, gameState, colores, gameState.isEditingMode) {
                                            if (gameState.isEditingMode) viewModel.manejarEdicionTablero(currentPos)
                                            else if (gameState.selectedPosition == currentPos) viewModel.onCellSelected(null)
                                            else if (gameState.modoJuego == GameMode.PUZZLE && gameState.selectedPosition != null) viewModel.validarJugadaPuzzle(gameState.selectedPosition!!, currentPos)
                                            else viewModel.onCellSelected(currentPos)
                                        }
                                    }
                                }
                            }
                            Row(modifier = Modifier.fillMaxWidth().height(18.dp).padding(bottom = 2.dp), horizontalArrangement = Arrangement.SpaceAround) {
                                listOf("a", "b", "c", "d", "e", "f", "g", "h").take(gameState.boardSize).forEach {
                                    Text(text = it, color = Color(0xFFE2E8F0), fontSize = 10.sp, fontWeight = FontWeight.Black)
                                }
                            }
                        }
                    }
                }

                CementerioRow("IA (PLATA): ", gameState.piezasComidasPlata, gameState)

                // Fila botones
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                    BotonCircularMedieval({ viewModel.reiniciarSegunModo() }, "🔄", "REINICIAR", Color(0xFFB45309))
                    BotonCircularMedieval({ viewModel.cambiarDificultad() }, "🏆", "NIVEL", Color(0xFF6D28D9))
                    BotonCircularMedieval({ viewModel.cambiarEstiloFichas() }, "♟️", "PIEZAS", Color(0xFF1E3A8A))
                    BotonCircularMedieval({ viewModel.reiniciarTodoElProgreso() }, "💣", "RESET", Color.Red)
                    BotonCircularMedieval({ viewModel.obtenerPistaAyuda() }, "💡", "PISTA", Color(0xFFD97706))
                    BotonCircularMedieval({ viewModel.cambiarTema() }, "🎨", "TEMA", Color(0xFFD93306))
                    BotonCircularMedieval({ viewModel.deshacerJugada() }, "↩️", "DESHACER", Color(0xFF57534E))
                }

// BLOQUE DE EDICIÓN CORREGIDO CON BOTÓN DE CIERRE
                if (gameState.isEditingMode) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF1E293B), RoundedCornerShape(12.dp))
                                .padding(16.dp), // Aumenté el padding interno para que se vea mejor
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            PanelEdicion(viewModel = viewModel)

                            Spacer(modifier = Modifier.height(15.dp))

                            Button(
                                onClick = { viewModel.finalizarYGuardarNivel() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)) // Color dorado/naranja para guardar
                            ) {
                                Text("GUARDAR NIVEL", fontWeight = FontWeight.Bold)
                            }
                        }

                        // BOTÓN "X" PARA CERRAR
                        IconButton(
                            onClick = { viewModel.cerrarModoEdicion() },
                            modifier = Modifier
                                .align(Alignment.TopEnd) // Lo posiciona en la esquina superior derecha
                                .padding(4.dp)
                        ) {
                            Text("✕", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                FooterFirma(viewModel)
            }

// --- 1. VIDEO DE FELICITACIÓN ---
            val videoAEjecutar = gameState.videoEventoPendiente

            if (videoAEjecutar != null) {
                VideoFelicitacion(
                    nivel = gameState.nivelActualInt,
                    onDismiss = {
                        // 1. Guardamos el progreso antes de limpiar el evento
                        viewModel.guardarProgresoFinal(context)

                        // 2. Limpiamos el estado para que el video se cierre
                        viewModel.limpiarVideoEvento()
                    }
                )
            }

            // --- 2. OVERLAY DE VICTORIA (Solo aparece si NO hay video reproduciéndose) ---
            if (gameState.esJaqueMate && !gameState.estaCargandoNivel && videoAEjecutar == null) {
                VictoryOverlay(
                    message = gameState.mensajeFinal ?: "¡Puzzle Completado!",
                    viewModel = viewModel,
                    onDismiss = { navController.popBackStack() }
                )
            }

            // --- 3. AVISO DE ERROR (Capa superior absoluta) ---
            if (gameState.mensajeError != null) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        color = Color.Red.copy(alpha = 0.9f),
                        shape = RoundedCornerShape(8.dp),
                        shadowElevation = 8.dp
                    ) {
                        Text(
                            text = gameState.mensajeError!!,
                            color = Color.White,
                            modifier = Modifier.padding(16.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.CasillaView(
    position: Position,
    gameState: GameState,
    colores: Pair<Color, Color>,
    modoEdicion: Boolean,
    onClick: () -> Unit
) {
    val isDarkCell = (position.row + position.col) % 2 == 1
    val piece = gameState.pieces.find { it.position == position }
    val isSelected = (gameState.selectedPosition == position)
    val esMovimientoValido = !modoEdicion && gameState.validMoves.any { it.to == position }
    val esJaqueMateAlRey = gameState.esJaqueMate && piece?.type == PieceType.REY && piece.color != gameState.ganador

    Box(
        modifier = Modifier
            .weight(1f) // OBLIGATORIO: Distribuye el espacio correctamente
            .fillMaxHeight()
            .background(if (isDarkCell) colores.second else colores.first)
            .then(if (esJaqueMateAlRey) Modifier.background(Color(0xFFB91C1C).copy(alpha = 0.6f)) else Modifier)
            .clickable(
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                indication = ripple(bounded = true),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // Marcador de selección
        if (isSelected) {
            Box(modifier = Modifier.fillMaxSize().border(4.dp, Color(0xFFE6B400).copy(alpha = 0.8f)))
        }

        // Marcador de movimiento válido
        if (esMovimientoValido) {
            Canvas(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                drawCircle(color = Color(0xFF88B378).copy(alpha = 0.7f), radius = size.minDimension / 3f)
            }
        }

        // Pista IA
        if (gameState.casillaPista == position) {
            Box(modifier = Modifier.size(12.dp).background(Color.Magenta, CircleShape))
        }

        // Renderizado de pieza
        piece?.let { p ->
            ChessPieceView(
                piece = p,
                isSelected = isSelected,
                resId = obtenerResourcePieza(p.type, p.color, gameState.estiloSeleccionado),
                simbolo = obtenerSimboloTexto(p.type, gameState.estiloSeleccionado, p.color),
                esJaqueMate = gameState.esJaqueMate,
                colorGanador = gameState.ganador
            )
        }
    }
}
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
fun VictoryOverlay(
    message: String,
    viewModel: BoardViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        SoundManager.play(R.raw.victoria)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.85f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "¡FELICIDADES!",
                color = Color.Yellow,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                color = Color.White,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    // 1. Ejecutamos la función que guarda nivel y avanza
                    viewModel.avanzarAlSiguienteNivel(context)
                    viewModel.guardarProgresoFinal(context)
                    // 2. Cerramos el overlay
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B)),
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text("Continuar Camino", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun FooterFirma(viewModel: BoardViewModel) {
    var mostrarDialogoClave by remember { mutableStateOf(false) }
    var claveIngresada by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        // Icono invisible o muy pequeño que dispara el diálogo
        Icon(
            painter = painterResource(id = android.R.drawable.ic_menu_edit),
            contentDescription = "Edit",
            modifier = Modifier.size(16.dp).clickable { mostrarDialogoClave = true },
            tint = Color.Gray
        )

        HorizontalDivider(modifier = Modifier.width(40.dp), thickness = 5.dp, color = Color(0xFF64748B))
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "DESARROLLADO POR JAIRO SALAZAR CASTAÑO", fontSize = 7.sp, fontWeight = FontWeight.Black, color = Color.White)
        Text(text = "© 2026 JascChess Pro", fontSize = 9.sp, color = Color(0xFF64748B))
    }

    if (mostrarDialogoClave) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogoClave = false
                claveIngresada = "" // Limpiar al cerrar
            },
            title = { Text("Modo Autor") },
            text = {
                TextField(
                    value = claveIngresada,
                    onValueChange = { claveIngresada = it },
                    label = { Text("Clave") },
                    visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                    singleLine = true
                )
            },
            confirmButton = {
                Button(onClick = {
                    if (claveIngresada == "4792") { // Cámbiala por tu clave
                        viewModel.activarModoEdicion()
                        mostrarDialogoClave = false
                        claveIngresada = ""
                    }
                }) { Text("Acceder") }
            }
        )
    }
}
@Composable
fun BotonCircularMedieval(
    onClick: () -> Unit,
    icono: String,
    texto: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(8.dp)) // Forma para el área de clic
            .clickable { onClick() } // <-- El clic debe estar aquí
    ) {
        Box(
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape)
                .background(color)
                .border(2.dp, Color.White.copy(alpha = 0.3f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = icono, fontSize = 20.sp)
        }
        Text(text = texto, fontSize = 9.sp, color = Color.White)
    }
}
@Composable
fun PanelEdicion(viewModel: BoardViewModel) {
    val tipos = listOf(PieceType.PEON, PieceType.TORRE, PieceType.CABALLO, PieceType.ALFIL, PieceType.REINA, PieceType.REY)
    val colores = listOf(PieceColor.ORO, PieceColor.PLATA)

    Column(modifier = Modifier.padding(8.dp).background(Color(0xFF1E293B), RoundedCornerShape(8.dp)).padding(8.dp)) {
        Text("Selecciona una pieza para jugar:", color = Color.White, fontWeight = FontWeight.Bold)

        tipos.forEach { tipo ->
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(vertical = 2.dp)) {
                colores.forEach { color ->
                    val esSeleccionada = viewModel.piezaSeleccionadaParaColocar == (tipo to color)
                    Button(
                        onClick = { viewModel.piezaSeleccionadaParaColocar = tipo to color },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (esSeleccionada) Color.Green else (if (color == PieceColor.ORO) Color(0xFFF59E0B) else Color(0xFF94A3B8))
                        )
                    ) {
                        Text("${tipo.name.take(1)}${color.name.take(1)}")
                    }
                }
            }
        }
    }
}
private fun obtenerResourcePieza(
    tipo: PieceType,
    color: PieceColor,
    estilo: EstiloFichas
): Int? {
    return when (estilo) {
        EstiloFichas.TRADICIONAL -> if (color == PieceColor.ORO) {
            when (tipo) {
                PieceType.PEON -> R.drawable.tradicional_peon_blanco
                PieceType.TORRE -> R.drawable.tradicional_torre_blanca
                PieceType.CABALLO -> R.drawable.tradicional_caballo_blanco
                PieceType.ALFIL -> R.drawable.tradicional_alfil_blanco
                PieceType.REINA -> R.drawable.tradicional_reina_blanca
                PieceType.REY -> R.drawable.tradicional_rey_blanco
            }
        } else {
            when (tipo) {
                PieceType.PEON -> R.drawable.tradicional_peon_negro
                PieceType.TORRE -> R.drawable.tradicional_torre_negra
                PieceType.CABALLO -> R.drawable.tradicional_caballo_negro
                PieceType.ALFIL -> R.drawable.tradicional_alfil_negro
                PieceType.REINA -> R.drawable.tradicional_reina_negra
                PieceType.REY -> R.drawable.tradicional_rey_negro
            }
        }
        EstiloFichas.TRADICIONAL1 -> if (color == PieceColor.ORO) {
            when (tipo) {
                PieceType.PEON -> R.drawable.tradicional1_peon_blanco
                PieceType.TORRE -> R.drawable.tradicional1_torre_blanca
                PieceType.CABALLO -> R.drawable.tradicional1_caballo_blanco
                PieceType.ALFIL -> R.drawable.tradicional1_alfil_blanco
                PieceType.REINA -> R.drawable.tradicional1_reina_blanca
                PieceType.REY -> R.drawable.tradicional1_rey_blanco
            }
        } else {
            when (tipo) {
                PieceType.PEON -> R.drawable.tradicional1_peon_negro
                PieceType.TORRE -> R.drawable.tradicional1_torre_negra
                PieceType.CABALLO -> R.drawable.tradicional1_caballo_negro
                PieceType.ALFIL -> R.drawable.tradicional1_alfil_negro
                PieceType.REINA -> R.drawable.tradicional1_reina_negra
                PieceType.REY -> R.drawable.tradicional1_rey_negro
            }
        }

        EstiloFichas.TRADICIONAL2 -> if (color == PieceColor.ORO) {
            when (tipo) {
                PieceType.PEON -> R.drawable.tradicional2_peon_blanco
                PieceType.TORRE -> R.drawable.tradicional2_torre_blanca
                PieceType.CABALLO -> R.drawable.tradicional2_caballo_blanco
                PieceType.ALFIL -> R.drawable.tradicional2_alfil_blanco
                PieceType.REINA -> R.drawable.tradicional2_reina_blanca
                PieceType.REY -> R.drawable.tradicional2_rey_blanco
            }
        } else {
            when (tipo) {
                PieceType.PEON -> R.drawable.tradicional2_peon_negro
                PieceType.TORRE -> R.drawable.tradicional2_torre_negra
                PieceType.CABALLO -> R.drawable.tradicional2_caballo_negro
                PieceType.ALFIL -> R.drawable.tradicional2_alfil_negro
                PieceType.REINA -> R.drawable.tradicional2_reina_negra
                PieceType.REY -> R.drawable.tradicional2_rey_negro
            }
        }
        EstiloFichas.TRADICIONAL3 -> if (color == PieceColor.ORO) {
            when (tipo) {
                PieceType.PEON -> R.drawable.tradicional3_peon_blanco
                PieceType.TORRE -> R.drawable.tradicional3_torre_blanca
                PieceType.CABALLO -> R.drawable.tradicional3_caballo_blanco
                PieceType.ALFIL -> R.drawable.tradicional3_alfil_blanco
                PieceType.REINA -> R.drawable.tradicional3_reina_blanca
                PieceType.REY -> R.drawable.tradicional3_rey_blanco
            }
        } else {
            when (tipo) {
                PieceType.PEON -> R.drawable.tradicional3_peon_negro
                PieceType.TORRE -> R.drawable.tradicional3_torre_negra
                PieceType.CABALLO -> R.drawable.tradicional3_caballo_negro
                PieceType.ALFIL -> R.drawable.tradicional3_alfil_negro
                PieceType.REINA -> R.drawable.tradicional3_reina_negra
                PieceType.REY -> R.drawable.tradicional3_rey_negro
            }
        }
        EstiloFichas.ROMANO -> if (color == PieceColor.ORO) {
            when (tipo) {
                PieceType.PEON -> R.drawable.romano_peon_blanco
                PieceType.TORRE -> R.drawable.romano_torre_blanca
                PieceType.CABALLO -> R.drawable.romano_caballo_blanco
                PieceType.ALFIL -> R.drawable.romano_alfil_blanco
                PieceType.REINA -> R.drawable.romano_reina_blanca
                PieceType.REY -> R.drawable.romano_rey_blanco
            }
        } else {
            when (tipo) {
                PieceType.PEON -> R.drawable.romano_peon_negro
                PieceType.TORRE -> R.drawable.romano_torre_negra
                PieceType.CABALLO -> R.drawable.romano_caballo_negro
                PieceType.ALFIL -> R.drawable.romano_alfil_blanco // Asegúrate que tu archivo se llame así
                PieceType.REINA -> R.drawable.romano_reina_negra
                PieceType.REY -> R.drawable.romano_rey_negro
            }
        }

    }
}
@Composable
fun VideoFelicitacion(nivel: Int, onDismiss: () -> Unit) {
    val mensaje = when (nivel) {
        10 -> "¡Has alcanzado el nivel 10: tu estrategia brilla, tu paciencia vence y tu visión domina el tablero."
        20 -> "¡Has superado el nivel 20: tu mente teje estrategias, tu paciencia abre caminos y cada jugada fortalece tu visión en el tablero."
        else -> "¡Victoria magistral!"
    }

    // Definimos los recursos de video de forma segura
    val videoRes = when (nivel) {
        10 -> R.raw.video_felicitacion_10
        20 -> R.raw.video_felicitacion_20
        else -> R.raw.victoria // Asegúrate de que victoria.mp3 exista en /raw
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.95f))
    ) {
        // Reproductor de video ocupando todo el fondo
        VideoPlayer(
            videoRes = videoRes,
            onDismiss = onDismiss
        )

        // Texto informativo flotando en la parte inferior
        Text(
            text = mensaje,
            color = Color.Yellow,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp)
        )

        // Indicador opcional para cerrar
        Text(
            text = "(Toca la pantalla para continuar)",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        )
    }
}

private fun obtenerSimboloTexto(tipo: PieceType, estilo: EstiloFichas, color: PieceColor): String {
    // Como ya usas imágenes para todo, devolvemos cadena vacía
    return ""
}