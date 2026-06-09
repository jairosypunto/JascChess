package com.jasc.jasc_chess.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.jasc.jasc_chess.model.*
import com.jasc.jasc_chess.data.engine.MoveValidator
import com.jasc.jasc_chess.data.engine.AIEngine
// Asegúrate de añadir este import en BoardViewModel.kt
import com.jasc.jasc_chess.data.engine.FENParser
import com.jasc.jasc_chess.model.ChessPuzzle
import com.jasc.jasc_chess.model.Move // Basado en tu archivo Move.kt
import android.util.Log
import com.jasc.jasc_chess.data.local.NivelRepository
// En BoardViewModel.kt, añade esto en la sección de imports:
import com.jasc.jasc_chess.data.local.NivelConfig
class BoardViewModel : ViewModel() {
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    // --- CORRECCIÓN EN EL INICIALIZADOR ---
    init {
        configurarPartida(8, GameMode.LIBRE)
        iniciarTemporizadorReloj()
    }
    // 1. Esta función es la que llamas desde el SelectorNivelesScreen
    fun cargarPartida(nivelId: Int) {
        val config = NivelRepository.totalNiveles[nivelId]

        if (config != null) {
            // IMPORTANTE: Pasamos config.size para que el motor sepa que el tablero creció
            configurarPartida(config.size, GameMode.PUZZLE)

            _gameState.update { it.copy(
                pieces = config.piezas,
                nivelActualInt = nivelId,
                currentTurn = config.turnoInicial,
                boardSize = config.size // <--- ESTO ES LO QUE HACE QUE EL 5x5 Y 6x6 SE DIBUJEN BIEN
            )}
        }
    }
fun configurarPartida(nuevoSize: Int, nuevoModo: GameMode, puzzle: ChessPuzzle? = null) {
    Log.d("JASC_DEBUG", "Iniciando configuración: tamaño=$nuevoSize, modo=$nuevoModo")

    val piezasNuevas = try {
        puzzle?.let { FENParser.parse(it.fen, nuevoSize) }
            ?: generarPiezasParaModo(nuevoSize)
    } catch (e: Exception) {
        Log.e("JASC_ERROR", "Error crítico en FEN: ${puzzle?.fen}", e)
        generarPiezasParaModo(nuevoSize)
    }

    _gameState.update { currentState ->
        currentState.copy(
            boardSize = nuevoSize,
            modoJuego = nuevoModo,
            pieces = piezasNuevas,
            currentPuzzle = puzzle,
            puzzleStepIndex = 0,
            currentTurn = PieceColor.ORO,
            partidaIniciada = true,
            selectedPosition = null,

            // FUERZA LA LISTA A SER List<Move> explícitamente
            validMoves = if (nuevoModo == GameMode.PUZZLE && puzzle != null) {
                puzzle.requiredMoves.toList()
            } else {
                emptyList<Move>()
            },

            esJaqueMate = false,
            esTablas = false,
            esJaque = false,
            piezasComidasOro = emptyList(),
            piezasComidasPlata = emptyList(),
            historialTableros = listOf(piezasNuevas),
            lastUpdate = System.currentTimeMillis()
        )
    }
}
    fun esCoronacionValida(pieza: ChessPiece, destino: Position, size: Int): Boolean {
        if (pieza.type != PieceType.PEON) return false
        val filaMeta = if (pieza.color == PieceColor.ORO) 0 else size - 1
        return destino.row == filaMeta
    }

    fun generarCodigoDeNivel() {
        val piezas = _gameState.value.pieces

        android.util.Log.e("DEBUG_TOTAL", "--- INICIO CÓDIGO ---")
        android.util.Log.e("DEBUG_TOTAL", "NivelConfig(piezas = listOf(")

        piezas.forEach { p ->
            val linea = "    ChessPiece(\"${p.id}\", PieceType.${p.type}, PieceColor.${p.color}, Position(${p.position.row}, ${p.position.col})),"
            android.util.Log.e("DEBUG_TOTAL", linea)
        }

        android.util.Log.e("DEBUG_TOTAL", "), turnoInicial = PieceColor.ORO)")
        android.util.Log.e("DEBUG_TOTAL", "--- FIN CÓDIGO ---")
    }

    var modoEdicion by mutableStateOf(false)

    fun activarModoEdicion() {
        vaciarTablero() // Llamamos a la función de limpieza
        modoEdicion = true // Activamos la visibilidad del panel y botones
    }
    var piezaSeleccionadaParaColocar by mutableStateOf<Pair<PieceType, PieceColor>?>(null)

    fun manejarEdicionTablero(pos: Position) {
        // 1. Validamos que tengamos una pieza seleccionada antes de actuar
        val seleccion = piezaSeleccionadaParaColocar ?: return

        _gameState.update { currentState ->
            val piezasActuales = currentState.pieces.toMutableList()
            val piezaExistente = piezasActuales.find { it.position == pos }

            val nuevasPiezas = if (piezaExistente != null) {
                // Si la pieza que está ahí es del mismo tipo y color, podrías querer borrarla
                // Si quieres rotar, aquí cambiarías la lógica. Por ahora, borramos.
                piezasActuales.apply { remove(piezaExistente) }
            } else {
                // Si la casilla está vacía, añadimos la nueva con el color seleccionado
                piezasActuales.add(
                    ChessPiece(
                        id = "p_${pos.row}_${pos.col}_${System.currentTimeMillis()}", // ID único temporal
                        type = seleccion.first,
                        color = seleccion.second,
                        position = pos
                    )
                )
                piezasActuales
            }

            // Retornamos el estado copiado
            currentState.copy(pieces = nuevasPiezas.toList())
        }
    }

    fun vaciarTablero() {
        _gameState.update {
            it.copy(
                pieces = emptyList(), // Tablero vacío
                selectedPosition = null,
                validMoves = emptyList(),
                esJaqueMate = false,
                esTablas = false
            )
        }
    }


    fun reiniciarPartida() {
        val nivelActual = _gameState.value.nivelActualInt
        Log.d("DEBUG_NIVEL", "Reinciando nivel: $nivelActual")
        cambiarNivel(nivelActual)
    }

    fun onCellSelected(pos: Position?) { // Cambiado a Position?
        val state = _gameState.value

        // Si pasamos null, deseleccionamos inmediatamente
        if (pos == null) {
            _gameState.update { it.copy(selectedPosition = null, validMoves = emptyList()) }
            return
        }

        // 1. Verificación de límites estricta
        if (!MoveValidator.esPosicionValida(pos.row, pos.col, state.boardSize)) {
            Log.e("JASC_DEBUG", "Posición fuera de rango: $pos")
            return
        }

        // 2. Delegación
        when (state.modoJuego) {
            GameMode.PUZZLE -> manejarSeleccionPuzzle(pos)
            GameMode.LIBRE -> ejecutarMovimientoLibre(pos)
            else -> Log.w("JASC_DEBUG", "Modo de juego no soportado: ${state.modoJuego}")
        }
    }

    private fun manejarSeleccionPuzzle(pos: Position) {
        val state = _gameState.value
        val origen = state.selectedPosition
        val piezaTocada = state.pieces.find { it.position == pos }
        Log.d("DEBUG_SELECT", "Tocaste: $pos | Origen actual: $origen")
        // 1. SI YA HABÍA UNA SELECCIÓN (ESTE ES EL PUNTO CLAVE)
        if (origen != null) {
            // A) Si toco la misma pieza: DESELECCIONAR
            if (origen == pos) {
                Log.d("DEBUG_SELECT", "¡Coincidencia detectada! Limpiando...")
                _gameState.update { it.copy(selectedPosition = null, validMoves = emptyList()) }
                return
            }

            // B) Si toco OTRA pieza mía: CAMBIAR SELECCIÓN
            if (piezaTocada != null && piezaTocada.color == state.currentTurn) {
                val nuevasValidas = MoveValidator.obtenerMovimientosValidos(piezaTocada, state.pieces, state.boardSize)
                _gameState.update {
                    it.copy(selectedPosition = pos, validMoves = nuevasValidas.map { dest -> Move(from = pos, to = dest) })
                }
                return
            }

            // C) Si toco una casilla destino válida: MOVER
            val esMovimientoValido = state.validMoves.any { it.to == pos }
            if (esMovimientoValido) {
                validarJugadaPuzzle(origen, pos)
                return
            }
        }

        // 2. SI NO HABÍA NADA SELECCIONADO (O toqué una pieza enemiga/vacía)
        // Intentamos seleccionar una pieza propia
        if (piezaTocada != null && piezaTocada.color == state.currentTurn) {
            val posicionesValidas = MoveValidator.obtenerMovimientosValidos(piezaTocada, state.pieces, state.boardSize)
            _gameState.update {
                it.copy(
                    selectedPosition = pos,
                    validMoves = posicionesValidas.map { dest -> Move(from = pos, to = dest) }
                )
            }
        } else {
            // Si toqué una casilla vacía sin tener nada seleccionado, limpiamos todo
            _gameState.update { it.copy(selectedPosition = null, validMoves = emptyList()) }
        }
    }

    private fun ejecutarMovimientoLibre(position: Position) {
        val currentState = _gameState.value
        val origen = currentState.selectedPosition

        if (origen == null) {
            // --- SELECCIÓN DE PIEZA ---
            val pieza = currentState.pieces.find { it.position == position && it.color == currentState.currentTurn }
            if (pieza != null) {
                val posicionesValidas = MoveValidator.obtenerMovimientosValidos(pieza, currentState.pieces, currentState.boardSize)
                _gameState.update {
                    it.copy(
                        selectedPosition = position,
                        validMoves = posicionesValidas.map { dest -> Move(from = position, to = dest) }
                    )
                }
            }
        } else {
            // --- EJECUCIÓN DE MOVIMIENTO ---
            val movimiento = currentState.validMoves.find { it.to == position }
            if (movimiento != null) {
                // 1. Ejecutar el movimiento y actualizar el tablero
                aplicarMovimiento(origen, position)

                // 2. IMPORTANTE: Obtenemos el estado justo después del movimiento
                val estadoPostMovimiento = _gameState.value

                // 3. Evaluar estados (Jaque, Mate, Tablas) basándose en el nuevo tablero
                evaluarEstadoFinal(estadoPostMovimiento.pieces, estadoPostMovimiento.currentTurn)

                // 4. Lógica de IA: Solo ejecutar si NO hay fin de partida (Jaque Mate o Tablas)
                val estadoFinal = _gameState.value
                if (estadoFinal.currentTurn == PieceColor.PLATA && !estadoFinal.esJaqueMate && !estadoFinal.esTablas) {
                    viewModelScope.launch {
                        delay(500)
                        ejecutarTurnoIA()
                    }
                }
            } else {
                // Si el usuario toca una casilla inválida, reseteamos la selección
                _gameState.update { it.copy(selectedPosition = null, validMoves = emptyList()) }
            }
        }
    }

    fun validarJugadaPuzzle(origen: Position, destino: Position) {
        val state = _gameState.value
        val movimiento = state.validMoves.find { it.to == destino } ?: return

        val piezasDespues = realizarDesplazamiento(state.pieces, origen, destino)
        val nuevoStep = state.puzzleStepIndex + 1

        evaluarEstadoFinal(piezasDespues, PieceColor.PLATA)
        val estadoPostMovimiento = _gameState.value

        if (estadoPostMovimiento.esJaqueMate) {
            subirDeNivelAutomatico()
        } else if (nuevoStep >= 2) { // Cambiado a 2 para forzar el límite que mencionaste
            // REINICIO DE NIVEL
            val nivelActual = state.nivelActualInt

            // Limpiamos todo antes de recargar
// ... dentro de validarJugadaPuzzle, en el bloque del límite (nuevoStep >= 2)
            _gameState.update { it.copy(esJuegoBloqueado = true, selectedPosition = null, validMoves = emptyList()) }

            viewModelScope.launch {
                delay(800)
                // Usamos el ID del nivel para recargar
                cambiarNivel(state.nivelActualInt)
            }
        } else {
            // Continuar el puzzle
            _gameState.update {
                it.copy(
                    pieces = piezasDespues,
                    puzzleStepIndex = nuevoStep,
                    selectedPosition = null,
                    validMoves = emptyList()
                )
            }

            viewModelScope.launch {
                delay(500)
                if (state.currentPuzzle != null) ejecutarRespuestaIA(state.currentPuzzle)
                else ejecutarTurnoIA()
            }
        }
    }

    fun reiniciarPartidaLibre() {
        val size = 8 // Forzamos siempre 8x8 para el modo libre
        val piezasNuevas = generarPiezasParaModo(size)

        _gameState.update {
            it.copy(
                boardSize = size, // <--- Crucial: fuerza el tamaño 8x8
                pieces = piezasNuevas,
                historialTableros = listOf(piezasNuevas),
                esJaqueMate = false,
                esTablas = false,
                esAhogado = false,
                esJaque = false, // <--- Limpieza del estado de jaque
                currentTurn = PieceColor.ORO,
                selectedPosition = null,
                validMoves = emptyList(),
                piezasComidasOro = emptyList(),
                piezasComidasPlata = emptyList(),
                esJuegoBloqueado = false,
                ganador = null,
                casillaPista = null
            )
        }
    }
    fun resetToLibre() {
        val sizeActual = _gameState.value.boardSize
        val piezasIniciales = generarPiezasParaModo(sizeActual)

        _gameState.update {
            it.copy(
                modoJuego = GameMode.LIBRE,
                pieces = piezasIniciales,
                historialTableros = listOf(piezasIniciales),
                currentPuzzle = null,
                puzzleStepIndex = 0,
                piezasComidasOro = emptyList(), // Limpia el cementerio al reiniciar
                piezasComidasPlata = emptyList(),
                currentTurn = PieceColor.ORO,
                selectedPosition = null,
                validMoves = emptyList()
            )
        }
    }
    // Nueva función para limpieza total sin tocar Repository
    fun iniciarModoLibre(size: Int) {
        val piezasNuevas = NivelRepository.generarSetupPorDefecto(size)

        _gameState.update { currentState ->
            currentState.copy(
                boardSize = size,
                modoJuego = GameMode.LIBRE,
                pieces = piezasNuevas,
                currentPuzzle = null,       // Fundamental: quita el puzzle
                nivelActualInt = 0,         // Resetea a 0 para que no detecte nivel
                currentTurn = PieceColor.ORO,
                selectedPosition = null,
                validMoves = emptyList(),
                esJaqueMate = false,
                esTablas = false,
                esJaque = false,
                piezasComidasOro = emptyList(),
                piezasComidasPlata = emptyList(),
                historialTableros = listOf(piezasNuevas)
            )
        }
    }
    private fun iniciarTemporizadorReloj() {
        viewModelScope.launch {
            while (true) {
                val state = _gameState.value
                // Solo actualizamos si el juego está activo y no ha terminado
                if (state.modoTiempoActivado && !state.esJaqueMate && !state.esTablas && !state.esAhogado && state.modoJuego == GameMode.LIBRE) {
                    // Actualizar cada 1 segundo es suficiente para un juego de ajedrez
                    delay(1000)
                    _gameState.update { s ->
                        if (s.currentTurn == PieceColor.ORO)
                            s.copy(oroTimeMillis = (s.oroTimeMillis - 1000L).coerceAtLeast(0L))
                        else
                            s.copy(plataTimeMillis = (s.plataTimeMillis - 1000L).coerceAtLeast(0L))
                    }
                } else {
                    // Si el juego está en pausa o terminado, dormimos el hilo más tiempo
                    delay(2000)
                }
            }
        }
    }

    fun alternarModoTiempo() {
        _gameState.update { it.copy(modoTiempoActivado = !it.modoTiempoActivado) }
    }
    fun cambiarDificultad() {
        _gameState.update { it.copy(nivelActual = when(it.nivelActual) {
            NivelDificultad.PRINCIPIANTE -> NivelDificultad.INTERMEDIO
            NivelDificultad.INTERMEDIO -> NivelDificultad.AVANZADO
            else -> NivelDificultad.PRINCIPIANTE
        })}
    }
    fun cambiarTema() { _gameState.update { it.copy(temaActual = (it.temaActual + 1) % 8) } }
    private fun cambiarNivel(n: Int) {
        val config = NivelRepository.obtenerNivel(n) ?: return

        _gameState.update { currentState ->
            currentState.copy(
                nivelActualInt = n,
                boardSize = config.size,
                pieces = config.piezas,
                modoJuego = GameMode.PUZZLE, // <--- ESTO EVITA QUE SALTE A 8x8
                currentTurn = config.turnoInicial,
                esJaque = false,
                esJaqueMate = false,
                esJuegoBloqueado = false,
                puzzleStepIndex = 0,
                selectedPosition = null,
                validMoves = emptyList()
            )
        }
    }

    private fun generarPiezasParaModo(size: Int, nivel: Int? = null): List<ChessPiece> {
        // Si se pasa un nivel, intentamos cargar su configuración
        if (nivel != null) {
            val config = NivelRepository.totalNiveles[nivel]
            if (config != null) return config.piezas
        }

        // Si no hay nivel, usamos el generador dinámico que pusimos en el Repository
        return NivelRepository.generarSetupPorDefecto(size)
    }

    private fun subirDeNivelAutomatico() {
        val estadoActual = _gameState.value
        val siguienteNivel = estadoActual.nivelActualInt + 1

        // Verificamos si existe en el mapa antes de intentar cargar
        if (NivelRepository.totalNiveles.containsKey(siguienteNivel)) {
            Log.d("JASC_DEBUG", "Avanzando al nivel: $siguienteNivel")
            viewModelScope.launch {
                delay(1000)
                cambiarNivel(siguienteNivel)
            }
        } else {
            // Si no hay más niveles, finalizamos con mensaje de victoria
            _gameState.update {
                it.copy(
                    puzzleResuelto = true,
                    mensajeFinal = "¡Increíble! Has dominado todos los niveles."
                )
            }
        }
    }

    private fun aplicarMovimiento(origen: Position, destino: Position) {
        _gameState.update { currentState ->
            val piezas = currentState.pieces.toMutableList()
            val piezaMoviendose = piezas.find { it.position == origen } ?: return@update currentState
            val piezaCapturada = piezas.find { it.position == destino }

            // --- 1. Lógica de Enroque ---
            if (piezaMoviendose.type == PieceType.REY && Math.abs(destino.col - origen.col) == 2) {
                val esCorto = destino.col > origen.col
                val torreY = origen.row
                val torreX = if (esCorto) currentState.boardSize - 1 else 0
                val torre = piezas.find { it.type == PieceType.TORRE && it.position.row == torreY && it.position.col == torreX }
                torre?.let { piezas[piezas.indexOf(it)] = it.copy(position = Position(torreY, if (esCorto) 5 else 3)) }
            }

            // --- 2. Capturas y Cementerio ---
            val nuevasComidasOro = currentState.piezasComidasOro.toMutableList()
            val nuevasComidasPlata = currentState.piezasComidasPlata.toMutableList()

            if (piezaCapturada != null) {
                piezas.remove(piezaCapturada)
                if (piezaCapturada.color == PieceColor.ORO) nuevasComidasOro.add(piezaCapturada)
                else nuevasComidasPlata.add(piezaCapturada)
            }

            // --- 3. Mover y Promocionar ---
            var piezaFinal = piezaMoviendose.copy(position = destino, hasMoved = true)

            // Regla de Promoción usando tu función validada
            if (piezaFinal.type == PieceType.PEON && esCoronacionValida(piezaFinal, destino, currentState.boardSize)) {
                piezaFinal = piezaFinal.copy(type = PieceType.REINA)
            }

            // IMPORTANTE: Aquí actualizamos la lista con la pieza modificada
            val indice = piezas.indexOf(piezaMoviendose)
            piezas[indice] = piezaFinal

            // --- 4. Cálculo de Alertas y Estado ---
            val turnoSiguiente = if (currentState.currentTurn == PieceColor.ORO) PieceColor.PLATA else PieceColor.ORO
            val enJaque = estaElReyEnJaque(turnoSiguiente, piezas, currentState.boardSize)
            val esMate = enJaque && verificarSiEsJaqueMate(turnoSiguiente, piezas, currentState.boardSize)
            val esAhogado = !enJaque && esAhogado(turnoSiguiente, piezas, currentState.boardSize)

// --- 5. Retorno del Estado ---
            currentState.copy(
                pieces = piezas, // MANTENER LAS PIEZAS ACTUALES, NO REINICIAR
                piezasComidasOro = nuevasComidasOro,
                piezasComidasPlata = nuevasComidasPlata,
                esJaque = enJaque,
                esJaqueMate = esMate,
                esAhogado = esAhogado,
                // Solo marcamos tablas, no forzamos cambios en las piezas
                esTablas = (esAhogado || verificarTablasPorMaterial(piezas)) && !esMate,
                selectedPosition = null,
                validMoves = emptyList(),
                currentTurn = turnoSiguiente,
                // IMPORTANTE: NO AGREGUES EL HISTORIAL DE TABLEROS SI NO ES NECESARIO
                // O ASEGÚRATE DE QUE LA LISTA ES LA ACTUAL Y NO UNA GENERADA
                historialTableros = currentState.historialTableros + listOf(piezas),
                casillaPista = null
            )
        }
    }

    fun realizarDesplazamiento(piezas: List<ChessPiece>, origen: Position, destino: Position): List<ChessPiece> {
        val nuevasPiezas = piezas.toMutableList()
        val piezaMovida = nuevasPiezas.find { it.position == origen } ?: return piezas
        val piezaCapturada = nuevasPiezas.find { it.position == destino }

        if (piezaCapturada != null) {
            _gameState.update { state ->
                if (piezaCapturada.color == PieceColor.ORO)
                    state.copy(piezasComidasOro = state.piezasComidasOro + piezaCapturada)
                else
                    state.copy(piezasComidasPlata = state.piezasComidasPlata + piezaCapturada)
            }
            nuevasPiezas.remove(piezaCapturada)
        }

        val idx = nuevasPiezas.indexOf(piezaMovida)
        if (idx != -1) nuevasPiezas[idx] = piezaMovida.copy(position = destino)

        return nuevasPiezas
    }
    fun deshacerJugada() {
        val currentState = _gameState.value
        val historial = currentState.historialTableros

        // 1. Validamos que el historial tenga sentido
        if (historial.size >= 2) {
            val nuevoHistorial = historial.dropLast(1)
            val tableroAnterior = nuevoHistorial.last()

            // 2. Validación crítica: Comparamos el tamaño del tablero anterior con el actual
            // Si el tamaño cambió (o algo está mal), forzamos limpieza en lugar de un error visual
            if (tableroAnterior.size != currentState.pieces.size && currentState.boardSize != 8) {
                Log.e("JASC_DEBUG", "Error de consistencia en historial, reseteando...")
                reiniciarPartida()
                return
            }

            _gameState.update {
                it.copy(
                    pieces = tableroAnterior,
                    historialTableros = nuevoHistorial,
                    currentTurn = if (it.currentTurn == PieceColor.ORO) PieceColor.PLATA else PieceColor.ORO,
                    esJaque = false,
                    esJaqueMate = false,
                    esTablas = false,
                    esAhogado = false,
                    selectedPosition = null,
                    validMoves = emptyList()
                )
            }
        } else {
            Log.d("JASC_DEBUG", "No hay más movimientos para deshacer en esta partida")
        }
    }
    fun obtenerPistaAyuda() {
        _gameState.update { currentState ->
            // Si ya hay una pista activa (casillaPista != null), la apagamos (null)
            if (currentState.casillaPista != null) {
                currentState.copy(casillaPista = null)
            } else {
                // Si no hay, calculamos la recomendación
                val recomendacion = AIEngine.calcularMejorMovimiento(
                    currentState.pieces,
                    currentState.nivelActual,
                    currentState.boardSize
                )
                // Asignamos la nueva posición de pista
                currentState.copy(casillaPista = recomendacion?.second)
            }
        }
    }
    private fun ejecutarRespuestaIA(puzzle: ChessPuzzle) {
        viewModelScope.launch {
            delay(600)
            val step = _gameState.value.puzzleStepIndex - 1
            val respuestaIA = puzzle.enemyMoves.getOrNull(step) ?: return@launch

            val piezaIA = _gameState.value.pieces.find { it.position == respuestaIA.from }
            val esValido = piezaIA != null && MoveValidator.esMovimientoValido(
                piezaIA, respuestaIA.to, _gameState.value.pieces, _gameState.value.boardSize
            )

            if (esValido) {
                val piezasActualizadas = realizarDesplazamiento(_gameState.value.pieces, respuestaIA.from, respuestaIA.to)
                // Usamos la función corregida
                evaluarEstadoFinal(piezasActualizadas, PieceColor.ORO)
            }
        }
    }

    private fun evaluarEstadoFinal(piezas: List<ChessPiece>, colorTurno: PieceColor) {
        val size = _gameState.value.boardSize
        val enJaque = estaElReyEnJaque(colorTurno, piezas, size)
        val esMate = enJaque && verificarSiEsJaqueMate(colorTurno, piezas, size)
        val esAhogado = !enJaque && esAhogado(colorTurno, piezas, size)
        val esTablasMaterial = verificarTablasPorMaterial(piezas)

        // Identificamos al Rey que está sufriendo el mate para marcar su estado
        val reyDerrotado = if (esMate) piezas.find { it.type == PieceType.REY && it.color == colorTurno } else null

        _gameState.update {
            it.copy(
                pieces = if (esMate) {
                    // Marcamos al rey como caído en la lista de piezas
                    piezas.map { pieza ->
                        if (pieza == reyDerrotado) pieza.copy(isFallen = true) else pieza
                    }
                } else piezas,
                esJaque = enJaque,
                esJaqueMate = esMate,
                esJuegoBloqueado = false, // Bloqueo desactivado para evitar alertas intrusivas
                posicionReyDerrotado = reyDerrotado?.position, // Guardamos la posición para el cambio de color
                esAhogado = esAhogado,
                esTablas = esAhogado || esTablasMaterial,
                ganador = if (esMate) (if (colorTurno == PieceColor.ORO) PieceColor.PLATA else PieceColor.ORO) else null
            )
        }

        if (esMate && size == 4) {
            subirDeNivelAutomatico()
        }
    }

    private fun estaElReyEnJaque(colorDelRey: PieceColor, piezasTablero: List<ChessPiece>, size: Int): Boolean {
        val rey = piezasTablero.find { it.type == PieceType.REY && it.color == colorDelRey } ?: return false
        val colorOponente = if (colorDelRey == PieceColor.ORO) PieceColor.PLATA else PieceColor.ORO
        return MoveValidator.esCasillaAmenazadaPorGeometria(rey.position, colorOponente, piezasTablero, size)
    }

    private fun verificarSiEsJaqueMate(colorTurnoEntrante: PieceColor, piezasTablero: List<ChessPiece>, size: Int): Boolean {
        // Si no hay jaque, no es mate
        if (!estaElReyEnJaque(colorTurnoEntrante, piezasTablero, size)) return false

        val piezasAliadas = piezasTablero.filter { it.color == colorTurnoEntrante }
        for (pieza in piezasAliadas) {
            // Usamos size: Int directamente
            val movimientosPosibles = MoveValidator.obtenerMovimientosValidos(pieza, piezasTablero, size)
            for (destino in movimientosPosibles) {
                val simulacion = piezasTablero.filterNot { it.position == destino || it.position == pieza.position }
                    .plus(pieza.copy(position = destino))
                if (!estaElReyEnJaque(colorTurnoEntrante, simulacion, size)) return false
            }
        }
        return true
    }

    private fun ejecutarTurnoIA() {
        val estadoActual = _gameState.value

        // 1. Obtener todas las piezas de la IA (color PLATA)
        val piezasIA = estadoActual.pieces.filter { it.color == PieceColor.PLATA }

        // 2. Recopilar TODOS los movimientos legales posibles de todas las piezas
        val todosLosMovimientosLegales = mutableListOf<Move>()
        for (pieza in piezasIA) {
            val destinos = MoveValidator.obtenerMovimientosValidos(pieza, estadoActual.pieces, estadoActual.boardSize)
            for (destino in destinos) {
                todosLosMovimientosLegales.add(Move(from = pieza.position, to = destino))
            }
        }

        // 3. Intentar obtener la mejor jugada del motor, si no, tomar la primera legal disponible
        val mejorMovimiento = AIEngine.calcularMejorMovimiento(estadoActual.pieces, estadoActual.nivelActual, estadoActual.boardSize)

        val movimientoElegido = if (mejorMovimiento != null) {
            // La IA encontró una jugada estratégica
            Move(from = mejorMovimiento.first.position, to = mejorMovimiento.second)
        } else {
            // La IA no encontró nada, pero forzamos un movimiento legal para evitar el bloqueo
            todosLosMovimientosLegales.firstOrNull()
        }

        // 4. Ejecución del movimiento
        if (movimientoElegido != null) {
            val piezasActualizadas = realizarDesplazamiento(estadoActual.pieces, movimientoElegido.from, movimientoElegido.to)

            _gameState.update { it.copy(pieces = piezasActualizadas, currentTurn = PieceColor.ORO) }
            evaluarEstadoFinal(piezasActualizadas, PieceColor.ORO)
        } else {
            Log.d("JASC_IA", "No hay movimientos legales, fin de partida.")
            // Forzamos evaluación para detectar mate o tablas
            evaluarEstadoFinal(estadoActual.pieces, PieceColor.ORO)
        }
    }
    private fun esAhogado(color: PieceColor, piezas: List<ChessPiece>, size: Int): Boolean {
        // Si está en jaque, no puede ser ahogado
        if (estaElReyEnJaque(color, piezas, size)) return false

        val piezasAliadas = piezas.filter { it.color == color }
        for (pieza in piezasAliadas) {
            // Usamos size: Int directamente
            val movimientosPosibles = MoveValidator.obtenerMovimientosValidos(pieza, piezas, size)
            for (destino in movimientosPosibles) {
                val simulacion = piezas.filterNot { it.position == destino || it.position == pieza.position }
                    .plus(pieza.copy(position = destino))
                if (!estaElReyEnJaque(color, simulacion, size)) return false
            }
        }
        return true
    }

    private fun verificarTablasPorMaterial(piezas: List<ChessPiece>): Boolean = piezas.size == 2 && piezas.all { it.type == PieceType.REY }
    fun cambiarEstiloFichas() {
        _gameState.update { currentState ->
            val nuevosEstilos = EstiloFichas.entries.toTypedArray() // Reemplazo de values()
            val siguienteIndice = (currentState.estiloSeleccionado.ordinal + 1) % nuevosEstilos.size
            currentState.copy(estiloSeleccionado = nuevosEstilos[siguienteIndice])
        }
    }

    fun cargarModo(size: Int, modo: GameMode, puzzle: ChessPuzzle?, nivel: Int = 1) {
        val piezasNuevas = generarPiezasParaModo(size, nivel)

        _gameState.update { currentState ->
            currentState.copy(
                pieces = piezasNuevas,
                boardSize = size,
                modoJuego = modo,
                currentPuzzle = puzzle,
                nivelActualInt = nivel, // Usa el nivel proporcionado
                historialTableros = listOf(piezasNuevas),
                esJaqueMate = false,
                esTablas = false,
                esAhogado = false,
                ganador = null,
                selectedPosition = null,
                validMoves = emptyList()
            )
        }
    }

    fun reiniciarTodoElProgreso() {
        _gameState.update {
            it.copy(
                puzzleResuelto = false,      // Esto debería quitar el overlay
                esJaqueMate = false,         // Limpiar flag de mate
                mensajeFinal = null,         // Limpiar mensaje
                esJuegoBloqueado = false,    // Limpiar bloqueo
                // Si tu UI depende de esto, también límpialo:
                selectedPosition = null,
                validMoves = emptyList()
            )
        }
        // Después de limpiar el estado, cargamos el nivel 1
        cambiarNivel(1)
    }

    fun finalizarYGuardarNivel() {
        val currentState = _gameState.value

        // 1. Crear la configuración del nivel actual
        val nuevoNivelConfig = NivelConfig(
            id = currentState.nivelActualInt + 1, // O el ID que corresponda
            size = currentState.boardSize,
            piezas = currentState.pieces,
            turnoInicial = PieceColor.ORO
        )

        // 2. Guardar en el repositorio (esto añade el nivel al mapa)
        NivelRepository.guardarNivel(nuevoNivelConfig)

        // 3. Calcular siguiente nivel
        val siguienteNivel = currentState.nivelActualInt + 1

        // 4. Salir de edición y cargar
        modoEdicion = false

        // Cargamos el nivel recién creado (o el siguiente)
        cambiarNivel(siguienteNivel)

        Log.d("JASC_EDICION", "Nivel guardado. Nuevo nivel cargado: $siguienteNivel")
    }

}