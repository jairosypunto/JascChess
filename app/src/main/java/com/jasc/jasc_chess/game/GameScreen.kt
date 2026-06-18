package com.jasc.jasc_chess.game

import android.util.Log
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jasc.jasc_chess.R
import com.jasc.jasc_chess.model.*
import com.jasc.jasc_chess.ui.components.* // Importa tus componentes locales
// Animaciones
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween

// UI y Recursos
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.layout.ContentScale

// Tus clases internas (Asegúrate de que existan en tu proyecto)
import com.jasc.jasc_chess.audio.SoundManager
import com.jasc.jasc_chess.audio.VideoPlayer

import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat // <--- ESTE ES EL IMPORT CLAVE
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun GameScreen(
    viewModel: BoardViewModel,
    navController: NavController
) {
    val gameState by viewModel.gameState.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    // Inicializar el contexto en el ViewModel para que funcionen los puntos y videos
// Inicializar el contexto sin resetear el tablero
    LaunchedEffect(Unit) {
        viewModel.setContext(context)

        // Solo configuramos por defecto si el tablero está vacío o no se ha iniciado
        // Esto evita que al entrar a "Prácticas", se sobrescriba con el 8x8 del inicio
        if (gameState.pieces.isEmpty()) {
            viewModel.configurarPartida(8, GameMode.LIBRE)
        }
    }

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
        // Box contenedor principal con el fondo

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF0F1E36))
        ) {
            // --- 1. CONTENIDO PRINCIPAL ---
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Perfil
// 1. Perfil más compacto
                Surface(
                    modifier = Modifier
                        .padding(vertical = 8.dp) // Reducido de 12.dp/20.dp a 8.dp
                        .size(50.dp) // Un poco más pequeño para dar aire
                        .clip(CircleShape)
                        .border(2.dp, Color(0xFFFFD700), CircleShape),
                    color = Color(0xFF1E293B)
                ) {
                    Box(contentAlignment = Alignment.Center) { Text("👤", fontSize = 24.sp) }
                }

// 2. Info partida organizada
                Column(
                    modifier = Modifier.fillMaxWidth().background(Color(0x1F000000)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Nivel (Chip compacto)
                    Surface(
                        modifier = Modifier.padding(vertical = 8.dp).clickable { viewModel.cambiarDificultad() },
                        color = Color(0xFF1E293B),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, Color(0xFFFFD700))
                    ) {
                        Text(
                            text = "NIVEL: ${gameState.nivelActual.name}",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                            color = Color(0xFFFFD700),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    // Timer / Modo Tiempo (Ya no ocupa 80dp, es auto-ajustable)
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 4.dp)
                            .heightIn(min = 35.dp) // Altura mínima elegante
                            .clickable { viewModel.alternarModoTiempo() },
                        color = Color(0xFF1E293B),
                        shape = RoundedCornerShape(8.dp),
                        shadowElevation = 2.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (gameState.modoTiempoActivado) {
                                TimerComponent(gameState.oroTimeMillis, gameState.plataTimeMillis, gameState.currentTurn == PieceColor.ORO, Modifier.fillMaxWidth())
                            } else {
                                Text(text = "⏱️ TIEMPO DESACTIVADO", color = Color(0xFF94A3B8), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Turno (Texto simple y pegado al bloque anterior)
                    Text(
                        text = if (gameState.currentTurn == PieceColor.ORO) "TURNO: IMPERIO 👑" else "TURNO: IA PLATA ⚔️",
                        color = if (gameState.currentTurn == PieceColor.ORO) Color(0xFFF59E0B) else Color(0xFF38BDF8),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                    )
                }
// --- CABECERA DE NIVEL PROFESIONAL MUESTRA LOS NIVELES Y LOS PASOS---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp), // Reducido de 16.dp a 8.dp
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "LEVEL ${gameState.nivelActualInt}",
                            fontSize = 11.sp, // Reducido para ser más sutil
                            color = Color.White.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Bold
                        )
                        // Eliminamos el Spacer gigante y ponemos el texto
                        Text(
                            text = "Mate in ${gameState.maxPasosConfigurado}",
                            fontSize = 24.sp, // Reducido de 36.sp para ganar espacio
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFFFD700) // Dorado para resaltar
                        )
                    }
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

// Edición
                if (gameState.isEditingMode) {
                    // Definimos el estado local para el input de pasos
                    var pasosInput by remember { mutableStateOf(gameState.maxPasosConfigurado.toString()) }

                    Box(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF1E293B), RoundedCornerShape(12.dp))
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            PanelEdicion(viewModel = viewModel)

                            Spacer(modifier = Modifier.height(15.dp))

                            // --- NUEVO CAMPO PARA PASOS ---
                            TextField(
                                value = pasosInput,
                                onValueChange = { if (it.all { char -> char.isDigit() }) pasosInput = it },
                                label = { Text("Máximos Pasos Permitidos") },
                                // Ahora que tienes los imports, estas líneas dejarán de dar error
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth().height(55.dp),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(15.dp))

                            Button(
                                onClick = {
                                    // 1. Actualizamos el ViewModel con el valor antes de guardar
                                    viewModel.actualizarMaxPasos(pasosInput.toIntOrNull() ?: 4)
                                    // 2. Guardamos el nivel
                                    viewModel.finalizarYGuardarNivel()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706))
                            ) {
                                Text("GUARDAR NIVEL", fontWeight = FontWeight.Bold)
                            }
                        }

                        IconButton(
                            onClick = { viewModel.cerrarModoEdicion() },
                            modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)
                        ) {
                            Text("✕", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                FooterFirma(viewModel)
            }

            // --- 2. OVERLAYS FLOTANTES ---
            val videoAEjecutar = gameState.videoEventoPendiente

            // Lógica de video cada 10 niveles
            if (gameState.esJaqueMate && !gameState.estaCargandoNivel && gameState.nivelActualInt % 10 == 0) {
                // Se asume que tu ViewModel gestiona la asignación a videoEventoPendiente
            }

            if (videoAEjecutar != null) {
                VideoFelicitacion(
                    nivel = gameState.nivelActualInt,
                    onDismiss = {
                        viewModel.guardarProgresoFinal(context)
                        viewModel.limpiarVideoEvento()
                    }
                )
            }

            if (gameState.esJaqueMate && !gameState.estaCargandoNivel && videoAEjecutar == null) {
                VictoryOverlay(
                    message = gameState.mensajeFinal ?: "¡Puzzle Completado!",
                    viewModel = viewModel,
                    onDismiss = { navController.popBackStack() }
                )
            }

            if (gameState.dialogoAcertijoVisible) {
                var respuestaUsuario by remember { mutableStateOf("") }
                AlertDialog(
                    onDismissRequest = { viewModel.cerrarDialogoAcertijo(); respuestaUsuario = "" },
                    title = { Text("¡Acertijo!", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(text = gameState.acertijoActual ?: "¿Cuál es la respuesta?", style = MaterialTheme.typography.bodyMedium)
                            if (gameState.mensajeError != null) {
                                Text(text = gameState.mensajeError!!, style = MaterialTheme.typography.bodySmall, color = Color.Red, fontWeight = FontWeight.Bold)
                            }
                            TextField(value = respuestaUsuario, onValueChange = { respuestaUsuario = it }, label = { Text("Respuesta") }, modifier = Modifier.fillMaxWidth().height(55.dp), singleLine = true)
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { viewModel.verificarRespuestaAcertijo(respuestaUsuario); respuestaUsuario = "" }) { Text("Validar") }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.cerrarDialogoAcertijo(); respuestaUsuario = "" }) { Text("✕") }
                    }
                )
            }
// 4. CÓDIGO GENERADO
            if (gameState.codigoGeneradoVisible != null) {
                AlertDialog(
                    onDismissRequest = { viewModel.limpiarCodigoGenerado() },
                    title = { Text("Código de Nivel Generado") },
                    text = {
                        // Al tener el import arriba, este bloque ya es aceptado por el compilador
                        SelectionContainer {
                            Text(text = gameState.codigoGeneradoVisible ?: "Sin código")
                        }
                    },
                    confirmButton = {
                        Button(onClick = { viewModel.limpiarCodigoGenerado() }) {
                            Text("Cerrar")
                        }
                    }
                )
            }
            // 5. REY AHOGADO Y TABLAS
            if (gameState.esAhogado || gameState.esTablas) {
                AlertDialog(
                    onDismissRequest = { /* Bloqueamos cierre */ },
                    title = {
                        Text(
                            text = if (gameState.esAhogado) "¡Rey Ahogado!" else "Tablas",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    text = {
                        Text(
                            text = if (gameState.esAhogado)
                                "El rey no tiene movimientos legales pero no está en jaque. ¡Es un empate!"
                            else "La partida termina en tablas por insuficiencia de material.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    confirmButton = {
                        Button(onClick = {
                            // Aquí ejecutas la limpieza
                            viewModel.reiniciarPartida()
                        }) {
                            Text("Reintentar Nivel")
                        }
                    }
                )
            }

// Este bloque va al final de tu GameScreen, debajo de todo,
// para que se dibuje por encima de cualquier otra cosa.
            if (gameState.mensajeError != null && !gameState.dialogoAcertijoVisible) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.7f)) // Fondo oscuro para dar importancia
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        color = Color(0xFFB91C1C), // Un rojo más profesional
                        shape = RoundedCornerShape(16.dp),
                        shadowElevation = 10.dp
                    ) {
                        Text(
                            text = gameState.mensajeError!!,
                            color = Color.White,
                            modifier = Modifier.padding(24.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
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
            .weight(1f)
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
        if (isSelected) {
            Box(modifier = Modifier.fillMaxSize().border(4.dp, Color(0xFFE6B400).copy(alpha = 0.8f)))
        }

        if (esMovimientoValido) {
            Canvas(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                drawCircle(color = Color(0xFF88B378).copy(alpha = 0.7f), radius = size.minDimension / 3f)
            }
        }

        // --- AQUÍ LA ANIMACIÓN CORRECTA ---
        if (gameState.casillaPista == position) {
            val infiniteTransition = rememberInfiniteTransition(label = "pista")

            // CORRECCIÓN: Definimos el float explícitamente y usamos el método correcto
            val alpha by infiniteTransition.animateFloat(
                initialValue = 0.3f,
                targetValue = 0.9f,
                animationSpec = infiniteRepeatable(
                    animation = tween(800),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "alpha"
            )

            Canvas(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                drawCircle(
                    color = Color.Yellow.copy(alpha = alpha),
                    radius = size.minDimension / 2.2f
                )
            }
        }

        piece?.let { p ->
            ChessPieceView(
                piece = p,
                isSelected = isSelected,
                resId = obtenerResourcePieza(p.type, p.color, gameState.estiloSeleccionado),
                simbolo = "", // Se quitó el argumento innecesario
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
        10 -> "¡Nivel 10 superado! Tu estrategia brilla, tu paciencia vence y tu visión comienza a dominar el tablero."
        20 -> "¡Nivel 20 alcanzado! Tu mente teje estrategias complejas, abres nuevos caminos y tu visión se fortalece con cada jugada."
        30 -> "¡Nivel 30 conquistado! Como un gran estratega, tu mando es firme, tus piezas se mueven con propósito y tu legado en el imperio crece."
        40 -> "¡Nivel 40 dominado! Has alcanzado la maestría: cada movimiento es arte, tu precisión es absoluta y el trono del imperio te aguarda."
        50 -> "¡Nivel 50, leyenda absoluta! Tu dominio del tablero es total; eres el estratega definitivo que el imperio tanto esperaba."
        else -> "¡Victoria magistral! El tablero se inclina ante tu grandeza."
    }

    // Definimos los recursos de video de forma segura
    val videoRes = when (nivel) {
        10 -> R.raw.video_felicitacion_10
        20 -> R.raw.video_felicitacion_20
        30 -> R.raw.video_felicitacion_30
        40 -> R.raw.video_felicitacion_40
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
    // Si no usas los parámetros, añade un guion bajo para silenciar el error
    // o implementa la lógica si quieres que devuelva algo.
    return ""
}