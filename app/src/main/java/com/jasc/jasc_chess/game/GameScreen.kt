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
import com.jasc.jasc_chess.ui.components.ChessPieceView
import android.util.Log
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

                CementerioRow("IMPERIO (ORO): ", gameState.piezasComidasOro, gameState)
                CementerioRow("IA (PLATA): ", gameState.piezasComidasPlata, gameState)

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

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Texto de Nivel para seguimiento
                    Text(
                        text = "AVANCE ACTUAL: NIVEL ${gameState.nivelActualInt}",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    val esTablero4x4 = gameState.boardSize == 4

                    if (gameState.modoJuego == GameMode.PUZZLE) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = { viewModel.resetToLibre() },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                            ) { Text("SALIR A LIBRE") }
                        }
                    } else {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = {
                                    if (esTablero4x4) viewModel.cargarModo(8, GameMode.LIBRE, null)
                                    else viewModel.cargarModo(4, GameMode.LIBRE, null)
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = if (esTablero4x4) Color(0xFF10B981) else Color(0xFF38BDF8)
                                )
                            ) {
                                Text(text = if (esTablero4x4) "JUGAR AJEDREZ" else "JUGAR 4X4")
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    BotonCircularMedieval({ viewModel.reiniciarPartida() }, "🔄", "REINICIAR", Color(0xFFB45309))
                    BotonCircularMedieval({ viewModel.cambiarDificultad() }, "🏆", "NIVEL", Color(0xFF6D28D9))
                    BotonCircularMedieval({ viewModel.cambiarEstiloFichas() }, "♟️", "PIEZAS", Color(0xFF1E3A8A))
                    BotonCircularMedieval({ viewModel.deshacerJugada() }, "↩️", "DESHACER", Color(0xFF78350F))
                    BotonCircularMedieval({ viewModel.obtenerPistaAyuda() }, "💡", "PISTA", Color(0xFFD97706)) // Llama a tu función
                    BotonCircularMedieval({ viewModel.cambiarTema() }, "🎨", "TEMA", Color(0xFFD93306))
                }

                FooterFirma()
                Spacer(modifier = Modifier.height(40.dp))
            }

// 1. VICTORIA DE PUZZLE
            if (gameState.puzzleResuelto) {
                // Si mensajeFinal es null, mostramos un mensaje por defecto para evitar errores
                VictoryOverlay(
                    message = gameState.mensajeFinal ?: "¡Puzzle Completado!",
                    onDismiss = { viewModel.reiniciarTodoElProgreso() }
                )
            }
// 2. FIN DE PARTIDA GENERAL (Jaque Mate, Tablas o Ahogado)
            else if (gameState.esJaqueMate || gameState.esTablas || gameState.esAhogado) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(top = 100.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Surface(
                        color = Color.Black.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(2.dp, Color.Yellow.copy(alpha = 0.5f)),
                        modifier = Modifier.padding(16.dp).wrapContentSize()
                    ) {
                        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = when {
                                    gameState.esJaqueMate -> "¡JAQUE MATE!"
                                    gameState.esTablas -> "TABLAS"
                                    else -> "FIN DE PARTIDA" // Cubre Ahogado
                                },
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
// En GameScreen.kt, dentro del bloque del AlertDialog:
                            Button(
                                onClick = {
                                    if (gameState.modoJuego == GameMode.LIBRE) {
                                        viewModel.reiniciarPartidaLibre() // Reinicia tablero, ignora niveles
                                    } else {
                                        viewModel.reiniciarPartida()      // TU LÓGICA ORIGINAL de Puzzles intacta
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB45309))
                            ) {
                                Text("JUGAR DE NUEVO")
                            }
                        }
                    }
                }
            }
// 3. Bloqueo por límite de jugadas (Solo sale si NO es fin de partida)
            else if (gameState.esJuegoBloqueado && !gameState.esJaqueMate && !gameState.esAhogado) {
                Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.8f)), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "¡LÍMITE DE JUGADAS!", color = Color.Red, fontSize = 32.sp, fontWeight = FontWeight.Black)
                        Text(text = "Reiniciando nivel...", color = Color.White, fontSize = 18.sp)
                    }
                }
            }
// 4. Aviso flotante de JAQUE
            else if (gameState.esJaque) {
                Box(modifier = Modifier.fillMaxSize().padding(top = 150.dp), contentAlignment = Alignment.TopCenter) {
                    Surface(color = Color.Red.copy(alpha = 0.9f), shape = RoundedCornerShape(50), shadowElevation = 8.dp) {
                        Text(text = "¡JAQUE AL REY!", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp))
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
    onClick: () -> Unit
) {
    val isDarkCell = (position.row + position.col) % 2 == 1
    val piece = gameState.pieces.find { it.position == position }
    val isSelected = (gameState.selectedPosition == position)

    val esModoPuzzleBloqueado = gameState.modoJuego == GameMode.PUZZLE && gameState.puzzleStepIndex >= 2
    val estaDeshabilitado = esModoPuzzleBloqueado || gameState.esJuegoBloqueado

    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .graphicsLayer { clip = false }
            .background(
                when {
                    // CORRECCIÓN: Solo el Rey derrotado (el que no es el ganador) se pinta de rojo
                    gameState.esJaqueMate && piece?.type == PieceType.REY && piece.color != gameState.ganador -> Color(0xFFB91C1C)
                    isSelected -> Color(0xB3F59E0B)
                    gameState.casillaPista == position -> Color(0xAAFB00FF)
                    !estaDeshabilitado && gameState.validMoves.any { it.to == position } -> Color(0xAA10B981)
                    isDarkCell -> colores.second
                    else -> colores.first
                }
            )
            .clickable(enabled = !estaDeshabilitado, onClick = { onClick() }),
        contentAlignment = Alignment.Center
    ) {
        piece?.let { p ->
            ChessPieceView(
                piece = p,
                isSelected = isSelected,
                // CORRECCIÓN: Ahora sí enviamos el ID del recurso que corresponde
                resId = obtenerResourcePieza(p.type, p.color, gameState.estiloSeleccionado),
                simbolo = obtenerSimboloTexto(p.type, gameState.estiloSeleccionado, p.color),
                esJaqueMate = gameState.esJaqueMate,
                colorGanador = gameState.ganador
            )
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
fun VictoryOverlay(message: String, onDismiss: () -> Unit) {
    // Fondo semitransparente que cubre toda la pantalla
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.85f)), // Un poco más oscuro para que resalte el mensaje
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

            Spacer(modifier = Modifier.height(16.dp)) // Espacio entre título y mensaje

            Text(
                text = message,
                color = Color.White,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp)) // Espacio entre mensaje y botón
            val viewModel: BoardViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
            Button(
                onClick = {
                    viewModel.reiniciarTodoElProgreso()
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B)),
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text("Volver al Inicio", color = Color.White, fontWeight = FontWeight.Bold)
            }
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

// 1. Corrección del nombre del recurso y typo
// Asegúrate de que tus archivos en res/drawable se llamen:
// "rey_caido_blanco" y "rey_caido_negro" (o cámbialos aquí por los que tengas)

private fun obtenerResourcePieza(
    tipo: PieceType,
    color: PieceColor,
    estilo: EstiloFichas,
    esJaqueMate: Boolean = false,
    colorGanador: PieceColor? = null // Añade este parámetro
): Int? {



    return when (estilo) {
        // --- ESTILO TRADICIONAL ---
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

        // --- ESTILO TRADICIONAL1 ---
        EstiloFichas.TRADICIONAL1 -> if (color == PieceColor.ORO) {
            when (tipo) {
                PieceType.PEON -> R.drawable.tradicional1_peon_blanco; PieceType.TORRE -> R.drawable.tradicional1_torre_blanca
                PieceType.CABALLO -> R.drawable.tradicional1_caballo_blanco; PieceType.ALFIL -> R.drawable.tradicional1_alfil_blanco
                PieceType.REINA -> R.drawable.tradicional1_reina_blanca; PieceType.REY -> R.drawable.tradicional1_rey_blanco
            }
        } else {
            when (tipo) {
                PieceType.PEON -> R.drawable.tradicional1_peon_negro; PieceType.TORRE -> R.drawable.tradicional1_torre_negra
                PieceType.CABALLO -> R.drawable.tradicional1_caballo_negro; PieceType.ALFIL -> R.drawable.tradicional1_alfil_negro
                PieceType.REINA -> R.drawable.tradicional1_reina_negra; PieceType.REY -> R.drawable.tradicional1_rey_negro
            }
        }
        // --- ESTILO TRADICIONAL3 ---
        EstiloFichas.TRADICIONAL3 -> if (color == PieceColor.ORO) {
            when (tipo) {
                PieceType.PEON -> R.drawable.tradicional3_peon_blanco; PieceType.TORRE -> R.drawable.tradicional3_torre_blanca
                PieceType.CABALLO -> R.drawable.tradicional3_caballo_blanco; PieceType.ALFIL -> R.drawable.tradicional3_alfil_blanco
                PieceType.REINA -> R.drawable.tradicional3_reina_blanca; PieceType.REY -> R.drawable.tradicional3_rey_blanco
            }
        } else {
            when (tipo) {
                PieceType.PEON -> R.drawable.tradicional3_peon_negro; PieceType.TORRE -> R.drawable.tradicional3_torre_negra
                PieceType.CABALLO -> R.drawable.tradicional3_caballo_negro; PieceType.ALFIL -> R.drawable.tradicional3_alfil_negro
                PieceType.REINA -> R.drawable.tradicional3_reina_negra; PieceType.REY -> R.drawable.tradicional3_rey_negro
            }
        }
        // --- ESTILO ROMANO ---
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