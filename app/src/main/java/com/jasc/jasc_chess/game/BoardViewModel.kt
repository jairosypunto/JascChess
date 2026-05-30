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
class BoardViewModel : ViewModel() {
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    // --- CORRECCIÓN EN EL INICIALIZADOR ---
    init {
        configurarPartida(8, GameMode.LIBRE)
        iniciarTemporizadorReloj()
    }

    private fun setupInitialBoard(): List<ChessPiece> {
        val initialPieces = mutableListOf<ChessPiece>()
        initialPieces.add(ChessPiece("t_plata_1", PieceType.TORRE, PieceColor.PLATA, Position(0, 0)))
        initialPieces.add(ChessPiece("c_plata_1", PieceType.CABALLO, PieceColor.PLATA, Position(0, 1)))
        initialPieces.add(ChessPiece("a_plata_1", PieceType.ALFIL, PieceColor.PLATA, Position(0, 2)))
        initialPieces.add(ChessPiece("q_plata", PieceType.REINA, PieceColor.PLATA, Position(0, 3)))
        initialPieces.add(ChessPiece("k_plata", PieceType.REY, PieceColor.PLATA, Position(0, 4)))
        initialPieces.add(ChessPiece("a_plata_2", PieceType.ALFIL, PieceColor.PLATA, Position(0, 5)))
        initialPieces.add(ChessPiece("c_plata_2", PieceType.CABALLO, PieceColor.PLATA, Position(0, 6)))
        initialPieces.add(ChessPiece("t_plata_2", PieceType.TORRE, PieceColor.PLATA, Position(0, 7)))
        for (i in 0..7) { initialPieces.add(ChessPiece("p_plata_$i", PieceType.PEON, PieceColor.PLATA, Position(1, i))) }
        for (i in 0..7) { initialPieces.add(ChessPiece("p_oro_$i", PieceType.PEON, PieceColor.ORO, Position(6, i))) }
        initialPieces.add(ChessPiece("t_oro_1", PieceType.TORRE, PieceColor.ORO, Position(7, 0)))
        initialPieces.add(ChessPiece("c_oro_1", PieceType.CABALLO, PieceColor.ORO, Position(7, 1)))
        initialPieces.add(ChessPiece("a_oro_1", PieceType.ALFIL, PieceColor.ORO, Position(7, 2)))
        initialPieces.add(ChessPiece("q_oro", PieceType.REINA, PieceColor.ORO, Position(7, 3)))
        initialPieces.add(ChessPiece("k_oro", PieceType.REY, PieceColor.ORO, Position(7, 4)))
        initialPieces.add(ChessPiece("a_oro_2", PieceType.ALFIL, PieceColor.ORO, Position(7, 5)))
        initialPieces.add(ChessPiece("c_oro_2", PieceType.CABALLO, PieceColor.ORO, Position(7, 6)))
        initialPieces.add(ChessPiece("t_oro_2", PieceType.TORRE, PieceColor.ORO, Position(7, 7)))
        return initialPieces
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
    fun reiniciarPartida() {
        val nivelActual = _gameState.value.nivelActualInt
        Log.d("DEBUG_NIVEL", "Reinciando nivel: $nivelActual")
        cambiarNivel(nivelActual)
    }

    fun onCellSelected(pos: Position) {
        val state = _gameState.value

        // 1. Verificación de límites estricta
        if (!MoveValidator.esPosicionValida(pos.row, pos.col, state.boardSize)) {
            Log.e("JASC_DEBUG", "Posición fuera de rango: $pos")
            return
        }

        // 2. Delegación a funciones específicas (así el IDE reconoce que se usan)
        when (state.modoJuego) {
            GameMode.PUZZLE -> manejarSeleccionPuzzle(pos)
            GameMode.LIBRE -> ejecutarMovimientoLibre(pos)
            // El 'else' es obligatorio si el compilador cree que pueden existir más estados
            else -> Log.w("JASC_DEBUG", "Modo de juego no soportado: ${state.modoJuego}")
        }
    }

    private fun manejarSeleccionPuzzle(pos: Position) {
        val state = _gameState.value
        val origen = state.selectedPosition

        if (origen == null) {
            val pieza = state.pieces.find { it.position == pos && it.color == state.currentTurn }
            if (pieza != null) _gameState.update { it.copy(selectedPosition = pos) }
        } else {
            validarJugadaPuzzle(origen, pos)
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
        // 1. Calculamos las piezas nuevas
        val piezasDespues = realizarDesplazamiento(state.pieces, origen, destino)
        val nuevoStep = state.puzzleStepIndex + 1

        // 2. Evaluamos el estado basado en ese movimiento
        evaluarEstadoFinal(piezasDespues, PieceColor.PLATA)
        val esMate = _gameState.value.esJaqueMate

        if (esMate) {
            subirDeNivelAutomatico()
        } else if (nuevoStep >= 2) {
            // --- AQUÍ ESTÁ EL BLINDAJE ---
            // 1. Bloqueamos primero
            _gameState.update { it.copy(pieces = piezasDespues, esJuegoBloqueado = true) }

            // 2. Reiniciamos capturando el nivel actual EXACTO antes del delay
            val nivelQueDebeMantenerse = state.nivelActualInt

            viewModelScope.launch {
                delay(1000)
                Log.d("JASC_DEBUG", "Forzando reinicio del nivel: $nivelQueDebeMantenerse")
                // Llamamos a cambiarNivel con el nivel capturado, no con una lectura asíncrona
                cambiarNivel(nivelQueDebeMantenerse)
            }
        } else {
            _gameState.update { it.copy(pieces = piezasDespues, puzzleStepIndex = nuevoStep, casillaPista = null) }
            state.currentPuzzle?.let { ejecutarRespuestaIA(it) }
        }
    }

    fun reiniciarPartidaLibre() {
        val size = _gameState.value.boardSize
        // Genera las piezas estándar (8x8)
        val piezasNuevas = generarPiezasParaModo(size)

        _gameState.update {
            it.copy(
                pieces = piezasNuevas,
                historialTableros = listOf(piezasNuevas),
                esJaqueMate = false,
                esTablas = false,
                esAhogado = false,
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
    private fun iniciarTemporizadorReloj() {
        viewModelScope.launch {
            while (true) {
                val state = _gameState.value
                if (state.modoTiempoActivado && !state.esJaqueMate && !state.esTablas && state.modoJuego == GameMode.LIBRE) {
                    delay(100) // Solo espera 100ms si el modo tiempo está activo
                    _gameState.update { s ->
                        if (s.currentTurn == PieceColor.ORO)
                            s.copy(oroTimeMillis = (s.oroTimeMillis - 100L).coerceAtLeast(0L))
                        else
                            s.copy(plataTimeMillis = (s.plataTimeMillis - 100L).coerceAtLeast(0L))
                    }
                } else {
                    delay(1000) // Si no está activo, espera 1 segundo para no consumir CPU
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
            NivelDificultad.INTERMEDIO -> NivelDificultad.INFIERNO
            else -> NivelDificultad.PRINCIPIANTE
        })}
    }
    fun cambiarTema() { _gameState.update { it.copy(temaActual = (it.temaActual + 1) % 8) } }

    private fun generarPiezasParaModo(size: Int, nivel: Int? = null): List<ChessPiece> {
        if (size == 8) return setupInitialBoard()

        // Usamos el nivel pasado como argumento, o si es nulo, el nivel guardado en el estado
        val nivelAUsar = nivel ?: _gameState.value.nivelActualInt

        val config = NivelRepository.totalNiveles[nivelAUsar]

        // Si no encuentra el nivel (por seguridad), no regreses el nivel 1, regresa una lista vacía
        // o maneja el error para que el tablero no se pinte con piezas erróneas
        return validarPiezas(config?.piezas ?: emptyList(), size)
    }

    private fun subirDeNivelAutomatico() {
        val estadoActual = _gameState.value
        val siguienteNivel = estadoActual.nivelActualInt + 1
        val maximoNiveles = NivelRepository.totalNiveles.size

        if (siguienteNivel <= maximoNiveles) {
            Log.d("JASC_DEBUG", "Avanzando al nivel: $siguienteNivel")
            viewModelScope.launch {
                delay(1000)
                limpiarEstadoDePartida()
                cambiarNivel(siguienteNivel)
            }
        } else {
            // AQUÍ ESTÁ LA CLAVE: Cuando termina el juego, marcamos que terminó
            // y NO incrementamos el nivel para que no ocurra un error de índice.
            Log.d("JASC_DEBUG", "Juego completado, no hay más niveles.")
            _gameState.update {
                it.copy(
                    puzzleResuelto = true,
                    esJaqueMate = false, // Limpiamos el estado de mate
                    mensajeFinal = "¡Increíble! Has dominado todos los niveles. ¡Eres un Gran Maestro!"
                )
            }
        }
    }

    private fun validarPiezas(piezas: List<ChessPiece>, size: Int): List<ChessPiece> {
        return piezas.filter {
            it.position.row in 0 until size && it.position.col in 0 until size
        }.also {
            if (it.size < piezas.size) Log.e("JASC_ERROR", "¡Piezas fuera de rango eliminadas!")
        }
    }
    fun limpiarEstadoDePartida() {
        _gameState.update { currentState ->
            currentState.copy(
                esJaque = false,
                esJaqueMate = false,
                selectedPosition = null,
                validMoves = emptyList()
            )
        }
    }

    private fun cambiarNivel(n: Int) {
        val config = NivelRepository.totalNiveles[n]

        if (config == null) {
            Log.e("JASC_ERROR", "ERROR CRÍTICO: No existe la configuración para el nivel $n")
            return // No hacemos nada, evitamos cargar el nivel 1 por error
        }

        _gameState.update { currentState ->
            currentState.copy(
                nivelActualInt = n,
                pieces = config.piezas, // Forzamos las piezas exactas del nivel N
                currentTurn = config.turnoInicial,
                esJaque = false,
                esJaqueMate = false,
                esJuegoBloqueado = false,
                historialTableros = listOf(config.piezas),
                puzzleStepIndex = 0,
                selectedPosition = null,
                validMoves = emptyList(),
                puzzleResuelto = false,
                mensajeFinal = null,
                // IMPORTANTE: Limpiamos el cementerio al cambiar de nivel
                piezasComidasOro = emptyList(),
                piezasComidasPlata = emptyList()
            )
        }
    }

    private fun aplicarMovimiento(origen: Position, destino: Position) {
        _gameState.update { currentState ->
            val piezas = currentState.pieces.toMutableList()
            val piezaMoviendose = piezas.find { it.position == origen }
            val piezaCapturada = piezas.find { it.position == destino }

            // --- 1. Lógica de Enroque ---
            if (piezaMoviendose?.type == PieceType.REY && Math.abs(destino.col - origen.col) == 2) {
                val esCorto = destino.col > origen.col
                val torreY = origen.row
                val torreX = if (esCorto) currentState.boardSize - 1 else 0
                val torre = piezas.find { it.type == PieceType.TORRE && it.position.row == torreY && it.position.col == torreX }
                torre?.let { piezas[piezas.indexOf(it)] = it.copy(position = Position(torreY, if (esCorto) 5 else 3)) }
            }

            // --- 2. Capturas y Cementerio ---
            // Preparamos listas temporales basadas en el estado actual
            var nuevasComidasOro = currentState.piezasComidasOro
            var nuevasComidasPlata = currentState.piezasComidasPlata

            if (piezaCapturada != null) {
                piezas.remove(piezaCapturada)
                // Si es ORO, se añade al cementerio de Oro (y viceversa)
                if (piezaCapturada.color == PieceColor.ORO) {
                    nuevasComidasOro = nuevasComidasOro + piezaCapturada
                } else {
                    nuevasComidasPlata = nuevasComidasPlata + piezaCapturada
                }
            }


// 3. Mover la pieza y verificar promoción
            if (piezaMoviendose != null) {
                var piezaFinal = piezaMoviendose.copy(position = destino, hasMoved = true)

                // REGLA DE PROMOCIÓN
                val filaPromocion = if (piezaMoviendose.color == PieceColor.ORO) 0 else currentState.boardSize - 1
                if (piezaMoviendose.type == PieceType.PEON && destino.row == filaPromocion) {
                    piezaFinal = piezaFinal.copy(type = PieceType.REINA) // Promociona a Reina
                }

                val indice = piezas.indexOf(piezaMoviendose)
                piezas[indice] = piezaFinal
            }

            // --- 4. Cálculo de Alertas y Estado ---
            val turnoSiguiente = if (currentState.currentTurn == PieceColor.ORO) PieceColor.PLATA else PieceColor.ORO
            val enJaque = estaElReyEnJaque(turnoSiguiente, piezas, currentState.boardSize)
            val esMate = enJaque && verificarSiEsJaqueMate(turnoSiguiente, piezas, currentState.boardSize)
            val esAhogado = !enJaque && esAhogado(turnoSiguiente, piezas, currentState.boardSize)

            // --- 5. Retorno del Estado ---
            currentState.copy(
                pieces = piezas,
                piezasComidasOro = nuevasComidasOro,
                piezasComidasPlata = nuevasComidasPlata,
                esJaque = enJaque,
                esJaqueMate = esMate,
                esAhogado = esAhogado,
                esTablas = esAhogado || verificarTablasPorMaterial(piezas),
                selectedPosition = null,
                validMoves = emptyList(),
                currentTurn = turnoSiguiente,
                historialTableros = currentState.historialTableros + listOf(piezas),
                casillaPista = null // <--- AÑADE ESTA LÍNEA AQUÍ
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
            // 5. Si después de todo no hay movimientos posibles, es el fin de la partida
            Log.d("JASC_IA", "No hay movimientos legales, fin de partida.")
            // El estado final de mate/tablas ya debería haber sido detectado por evaluarEstadoFinal
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
        cambiarNivel(1)
    }

}