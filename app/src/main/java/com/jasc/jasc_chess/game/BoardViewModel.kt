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
import com.jasc.jasc_chess.data.local.PuzzleRepository
import com.jasc.jasc_chess.model.ChessPuzzle
import com.jasc.jasc_chess.model.Move // Basado en tu archivo Move.kt
import android.util.Log
class BoardViewModel : ViewModel() {
    // 1. Agrega esta variable al inicio de la clase
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    init {
        Log.d("DEBUG_PIEZAS", "EL VIEWMODEL SE HA INICIALIZADO")
        // Inicializamos con partidaIniciada = false
        _gameState.update { it.copy(pieces = emptyList(), modoJuego = GameMode.PUZZLE, partidaIniciada = false) }
        cargarNivel(1)
        iniciarTemporizadorReloj()
    }

    // --- 1. CONFIGURACIÓN E INICIALIZACIÓN ---
// (Aquí van: setupInitialBoard, reiniciarPartida, cargarNivel, siguientePuzzle, cargarTableroDesdeFen, obtenerTipoDesdeChar)
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
    // --- 1. CONFIGURACIÓN E INICIALIZACIÓN ---
// Cambia tu firma actual por esta que acepta el parámetro opcional
    fun configurarPartida(nuevoSize: Int, nuevoModo: GameMode, puzzle: ChessPuzzle? = null) {
        val piezasNuevas = if (puzzle != null) {
            // Si hay puzzle, cargamos desde su FEN
            FENParser.parse(puzzle.fen, nuevoSize)
        } else if (nuevoSize == 4) {
            // Caso 4x4 por defecto sin puzzle
            listOf(
                ChessPiece("k_oro_4x4", PieceType.REY, PieceColor.ORO, Position(3, 1)),
                ChessPiece("p_oro_4x4", PieceType.PEON, PieceColor.ORO, Position(2, 1)),
                ChessPiece("k_plata_4x4", PieceType.REY, PieceColor.PLATA, Position(0, 1)),
                ChessPiece("p_plata_4x4", PieceType.PEON, PieceColor.PLATA, Position(1, 1))
            )
        } else {
            generarPiezasIniciales(nuevoSize)
        }

        _gameState.update {
            it.copy(
                boardSize = nuevoSize,
                modoJuego = nuevoModo,
                pieces = piezasNuevas,
                currentPuzzle = puzzle, // Asignamos el puzzle si existe
                currentTurn = PieceColor.ORO,
                partidaIniciada = true,
                selectedPosition = null,
                validMoves = emptyList(),
                esJaqueMate = false,
                esTablas = false
            )
        }
    }

    // --- 2. REINICIO MAESTRO (Corrige el error de basura en cementerio) ---
    fun reiniciarPartida() {
        // Obtenemos el tamaño actual del tablero para mantener el modo (4x4 o 8x8)
        val sizeActual = _gameState.value.boardSize
        val modoActual = _gameState.value.modoJuego

        // Generamos el tablero desde cero
        val piezasNuevas = generarPiezasIniciales(sizeActual)

        _gameState.update { it.copy(
            pieces = piezasNuevas,
            historialTableros = listOf(piezasNuevas),
            piezasComidasOro = emptyList(),
            piezasComidasPlata = emptyList(),
            selectedPosition = null,
            validMoves = emptyList(),
            currentTurn = PieceColor.ORO,
            esJaque = false,
            esJaqueMate = false,
            esTablas = false,
            esAhogado = false,
            ganador = null,
            // --- ESTO ES LO QUE HACE QUE FUNCIONE ---
            partidaIniciada = true,       // Mantiene el tablero desbloqueado
            modoTiempoActivado = false,   // Resetea el reloj a "esperando"
            oroTimeMillis = 600000L,      // Resetea el tiempo de Oro
            plataTimeMillis = 600000L     // Resetea el tiempo de Plata
        )}
    }

    private fun generarPiezasIniciales(size: Int): List<ChessPiece> {
        val piezas = mutableListOf<ChessPiece>()

        if (size == 4) {
            // --- CONFIGURACIÓN TÁCTICA 4X4 (Corregida y validada) ---
            // Usamos IDs claros y posiciones seguras dentro del rango 0..3

            // PLATA (Oponente - Parte superior del tablero)
            piezas.add(ChessPiece("k_plata_4x4", PieceType.REY, PieceColor.PLATA, Position(0, 1)))
            piezas.add(ChessPiece("q_plata_4x4", PieceType.REINA, PieceColor.PLATA, Position(0, 2)))
            piezas.add(ChessPiece("p_plata_1", PieceType.PEON, PieceColor.PLATA, Position(1, 1)))
            piezas.add(ChessPiece("p_plata_2", PieceType.PEON, PieceColor.PLATA, Position(1, 2)))

            // ORO (Jugador - Parte inferior del tablero)
            piezas.add(ChessPiece("k_oro_4x4", PieceType.REY, PieceColor.ORO, Position(3, 1)))
            piezas.add(ChessPiece("q_oro_4x4", PieceType.REINA, PieceColor.ORO, Position(3, 2)))
            piezas.add(ChessPiece("p_oro_1", PieceType.PEON, PieceColor.ORO, Position(2, 1)))
            piezas.add(ChessPiece("p_oro_2", PieceType.PEON, PieceColor.ORO, Position(2, 2)))

        } else {
            // --- LÓGICA 8X8 (Tu lógica actual funciona bien aquí) ---
            for (i in 0..7) {
                piezas.add(ChessPiece("p_plata_$i", PieceType.PEON, PieceColor.PLATA, Position(1, i)))
                piezas.add(ChessPiece("p_oro_$i", PieceType.PEON, PieceColor.ORO, Position(6, i)))
            }
            val linea = listOf(PieceType.TORRE, PieceType.CABALLO, PieceType.ALFIL, PieceType.REINA, PieceType.REY, PieceType.ALFIL, PieceType.CABALLO, PieceType.TORRE)
            linea.forEachIndexed { i, type ->
                piezas.add(ChessPiece("t_plata_$i", type, PieceColor.PLATA, Position(0, i)))
                piezas.add(ChessPiece("t_oro_$i", type, PieceColor.ORO, Position(7, i)))
            }
        }
        return piezas
    }

    fun cargarNivel(id: Int) {
        val nivel: ChessPuzzle? = PuzzleRepository.levels.find { it.id == id }

        if (nivel != null) {
            // --- AQUÍ ESTÁ LA CLAVE ---
            // Si el nivel tiene is4x4 = true, forzamos el tamaño y limpiamos el estado previo
            val size = if (nivel.is4x4) 4 else 8

            _gameState.update {
                it.copy(
                    pieces = emptyList(), // Limpieza total antes de procesar el nuevo FEN
                    currentPuzzle = nivel,
                    boardSize = size,
                    modoJuego = GameMode.PUZZLE,
                    selectedPosition = null,
                    validMoves = emptyList()
                )
            }

            // Ahora cargamos el FEN con el tamaño correcto
            cargarTableroDesdeFen(nivel.fen)
        } else {
            Log.e("BoardViewModel", "Error: No se encontró el puzzle con ID $id")
        }
    }

    fun cargarPuzzle(puzzle: ChessPuzzle) {
        val size = if (puzzle.is4x4) 4 else 8
        val piezas = FENParser.parse(puzzle.fen, size)

        _gameState.update { it.copy(
            currentPuzzle = puzzle,
            boardSize = size,
            pieces = piezas,
            puzzleStepIndex = 0, // ¡ESTO ES CLAVE!
            selectedPosition = null,
            currentTurn = PieceColor.ORO,
            modoJuego = GameMode.PUZZLE
        )}
    }

    // --- MÉTODO ÚNICO DE INICIALIZACIÓN ---
    fun prepararJuego(nuevoSize: Int, nuevoModo: GameMode, esPuzzle: Boolean = false) {
        _gameState.update {
            val piezas = if (esPuzzle) emptyList() else generarPiezasIniciales(nuevoSize)
            it.copy(
                boardSize = nuevoSize,
                modoJuego = nuevoModo,
                pieces = piezas,
                currentTurn = PieceColor.ORO,
                partidaIniciada = true,
                selectedPosition = null,
                validMoves = emptyList(),
                esJaque = false,
                esJaqueMate = false,
                esAhogado = false,
                esTablas = false,
                ganador = null,
                piezasComidasOro = emptyList(),
                piezasComidasPlata = emptyList(),
                historialTableros = listOf(piezas)
            )
        }
    }


    fun onCellSelected(pos: Position) {
        val state = _gameState.value

        if (state.modoJuego == GameMode.PUZZLE) {
            val puzzle = state.currentPuzzle ?: return
            val origen = state.selectedPosition

            if (origen == null) {
                // Primer clic: Seleccionar pieza propia
                val pieza = state.pieces.find { it.position == pos && it.color == state.currentTurn }
                if (pieza != null) {
                    _gameState.update { it.copy(selectedPosition = pos) }
                }
            } else {
                // Segundo clic: Validar contra la SOLUCIÓN del puzzle
                val jugadaCorrecta = puzzle.solution.getOrNull(state.puzzleStepIndex)

                if (jugadaCorrecta != null && jugadaCorrecta.from == origen && jugadaCorrecta.to == pos) {
                    // 1. Ejecutar movimiento jugador
                    val nuevasPiezas = realizarDesplazamiento(state.pieces, origen, pos)

                    _gameState.update { it.copy(
                        pieces = nuevasPiezas,
                        puzzleStepIndex = it.puzzleStepIndex + 1,
                        selectedPosition = null,
                        currentTurn = PieceColor.PLATA
                    )}

                    // 2. Respuesta automática de la IA (del repositorio)
                    viewModelScope.launch {
                        delay(600)
                        val respuestaIA = puzzle.enemyMoves.getOrNull(state.puzzleStepIndex - 1)
                        if (respuestaIA != null) {
                            val piezasFinales = realizarDesplazamiento(_gameState.value.pieces, respuestaIA.from, respuestaIA.to)
                            _gameState.update { it.copy(
                                pieces = piezasFinales,
                                currentTurn = PieceColor.ORO
                            )}
                        }
                    }
                } else {
                    // Movimiento fallido
                    _gameState.update { it.copy(selectedPosition = null) }
                }
            }
        } else {
            // MODO LIBRE: Tu motor original
            ejecutarMovimientoLibre(pos)
        }
    }

    private fun ejecutarRespuestaAutomatica() {
        val state = _gameState.value
        val puzzle = state.currentPuzzle ?: return

        // Aquí accedemos correctamente al movimiento de la IA según el paso actual
        val movimientoIA = puzzle.enemyMoves.getOrNull(state.puzzleStepIndex - 1) ?: return

        val nuevasPiezas = realizarDesplazamiento(state.pieces, movimientoIA.from, movimientoIA.to)

        _gameState.update { it.copy(
            pieces = nuevasPiezas,
            currentTurn = PieceColor.ORO
        )}
    }

    private fun ejecutarMovimientoLibre(position: Position) {
        val currentState = _gameState.value
        if (!currentState.partidaIniciada) return

        val origen = currentState.selectedPosition

        if (origen == null) {
            // Fase de Selección
            val pieza = currentState.pieces.find { it.position == position && it.color == currentState.currentTurn }
            if (pieza != null) {
                _gameState.update {
                    it.copy(
                        selectedPosition = position,
                        validMoves = MoveValidator.obtenerMovimientosValidos(pieza, it.pieces, it.boardSize)
                    )
                }
            }
        } else {
            // Fase de Ejecución
            if (currentState.validMoves.contains(position)) {
                aplicarMovimiento(origen, position)

                // IA responde solo si el movimiento fue legal y es su turno
                if (_gameState.value.currentTurn == PieceColor.PLATA) {
                    viewModelScope.launch {
                        delay(500) // Tiempo de "pensamiento"
                        ejecutarTurnoIA()
                    }
                }
            } else {
                // Si hace clic en otro lugar, deseleccionamos
                _gameState.update { it.copy(selectedPosition = null, validMoves = emptyList()) }
            }
        }
    }

    // 2. LA VALIDACIÓN (Corregida para manejar el turno y la respuesta)
// Dentro de BoardViewModel.kt
    fun validarJugadaPuzzle(origen: Position, destino: Position) {
        val state = _gameState.value
        val puzzle = state.currentPuzzle ?: return
        val jugadaEsperada = puzzle.solution.getOrNull(state.puzzleStepIndex)

        if (jugadaEsperada != null && jugadaEsperada.from == origen && jugadaEsperada.to == destino) {

            // 1. Ejecutar movimiento jugador
            val piezasDespuesUsuario = realizarDesplazamiento(state.pieces, origen, destino)

            // Actualizamos estado intermedio
            val stateIntermedio = state.copy(
                pieces = piezasDespuesUsuario,
                puzzleStepIndex = state.puzzleStepIndex + 1,
                selectedPosition = null,
                currentTurn = PieceColor.PLATA
            )
            _gameState.value = stateIntermedio
            evaluarEstadoFinal(stateIntermedio, PieceColor.PLATA) // Evaluar tras mover el jugador

            // 2. IA responde
            viewModelScope.launch {
                delay(600)
                val respuestaIA = puzzle.enemyMoves.getOrNull(state.puzzleStepIndex)
                if (respuestaIA != null) {
                    val piezasFinales = realizarDesplazamiento(_gameState.value.pieces, respuestaIA.from, respuestaIA.to)
                    val stateFinal = _gameState.value.copy(
                        pieces = piezasFinales,
                        currentTurn = PieceColor.ORO
                    )
                    _gameState.value = stateFinal
                    evaluarEstadoFinal(stateFinal, PieceColor.ORO) // Evaluar tras respuesta IA
                }
            }
        } else {
            _gameState.update { it.copy(selectedPosition = null) }
        }
    }


    // Dentro de BoardViewModel.kt
    fun siguientePuzzle() {
        val currentState = _gameState.value
        // Filtramos niveles según el tamaño actual
        val es4x4 = currentState.boardSize == 4
        val currentId = currentState.currentPuzzle?.id ?: 0

        // Buscamos el siguiente puzzle que coincida con el modo actual
        val nextPuzzle = PuzzleRepository.levels.find { it.id > currentId && it.is4x4 == es4x4 }
            ?: PuzzleRepository.levels.first { it.is4x4 == es4x4 } // Si llega al final, reinicia

        configurarPartida(if (nextPuzzle.is4x4) 4 else 8, GameMode.PUZZLE, nextPuzzle)
    }

    private fun cargarTableroDesdeFen(fen: String) {
        val partes = fen.split(" ")
        val boardPart = partes[0]
        val turnChar = if (partes.size > 1) partes[1] else "w" // "w" para ORO, "b" para PLATA

        val nuevasPiezas = mutableListOf<ChessPiece>()
        val filas = boardPart.split("/")

        filas.forEachIndexed { rowIndex, rowString ->
            var colIndex = 0
            for (char in rowString) {
                if (char.isDigit()) {
                    colIndex += char.digitToInt()
                } else if (char.isLetter()) {
                    val color = if (char.isUpperCase()) PieceColor.ORO else PieceColor.PLATA
                    val type = obtenerTipoDesdeChar(char)
                    val id = "${type}_${color}_${rowIndex}_${colIndex}"
                    nuevasPiezas.add(ChessPiece(id, type, color, Position(rowIndex, colIndex)))
                    colIndex++
                }
            }
        }

        _gameState.update { it.copy(
            pieces = nuevasPiezas.toList(),
            currentTurn = if (turnChar == "w") PieceColor.ORO else PieceColor.PLATA,
            puzzleStepIndex = 0,
            selectedPosition = null,
            validMoves = emptyList()
        )}
    }



// --- 2. CONTROL DEL RELOJ Y ESTADO ---
// (Aquí van: iniciarTemporizadorReloj, alternarActividadReloj, cambiarDificultad, cambiarTema, cambiarEstiloFichas, resetToLibre, iniciarModoPuzzle, iniciarPuzzleDePrueba)
private fun iniciarTemporizadorReloj() {
    viewModelScope.launch {
        while (true) {
            delay(100)
            _gameState.update { state ->
                // SOLO resta tiempo si el usuario activó el modo tiempo
                if (state.modoTiempoActivado && !state.esJaqueMate && !state.esTablas && state.modoJuego == GameMode.LIBRE) {
                    if (state.currentTurn == PieceColor.ORO) {
                        state.copy(oroTimeMillis = (state.oroTimeMillis - 100L).coerceAtLeast(0L))
                    } else {
                        state.copy(plataTimeMillis = (state.plataTimeMillis - 100L).coerceAtLeast(0L))
                    }
                } else {
                    // Si modoTiempoActivado es false, el reloj se queda quieto para siempre
                    state
                }
            }
        }
    }
}

    // Esta es la función que disparas con el clic en el reloj
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

    fun cambiarEstiloFichas() {
        _gameState.update {
            it.copy(estiloSeleccionado = when(it.estiloSeleccionado) {
                EstiloFichas.TRADICIONAL -> EstiloFichas.ROMANO
                EstiloFichas.ROMANO -> EstiloFichas.EGIPCIO
                EstiloFichas.EGIPCIO -> EstiloFichas.GLADIADOR
                else -> EstiloFichas.TRADICIONAL
            })
        }
    }

    // Dentro de BoardViewModel.kt

    fun cambiarModoTablero(nuevoSize: Int) {
        _gameState.update { currentState ->
            // Definimos el nuevo modo basándonos en el tamaño
            val nuevoModo = if (nuevoSize == 4) GameMode.PUZZLE else GameMode.LIBRE

            currentState.copy(
                boardSize = nuevoSize,
                modoJuego = nuevoModo,
                // Llamamos a la función que limpia y coloca piezas según el tamaño
                pieces = generarPiezasParaModo(nuevoSize),
                selectedPosition = null,
                validMoves = emptyList(),
                partidaIniciada = true
            )
        }
    }

    private fun generarPiezasParaModo(size: Int): List<ChessPiece> {
        return if (size == 4) {
            listOf(
                ChessPiece("k_oro_mini", PieceType.REY, PieceColor.ORO, Position(3, 1)),
                ChessPiece("k_plata_mini", PieceType.REY, PieceColor.PLATA, Position(0, 1)),
                // Agrega un par de peones para ver algo de acción
                ChessPiece("p_oro_mini", PieceType.PEON, PieceColor.ORO, Position(2, 1)),
                ChessPiece("p_plata_mini", PieceType.PEON, PieceColor.PLATA, Position(1, 1))
            )
        } else {
            setupInitialBoard()
        }
    }
    fun resetToLibre() {
        val piezasIniciales = setupInitialBoard()
        _gameState.update { it.copy(
            modoJuego = GameMode.LIBRE,
            pieces = piezasIniciales,
            // CAMBIO: Ahora empieza en FALSE, esperando tu clic
            partidaIniciada = true,
            esJaqueMate = false
        )}
    }

    fun iniciarNivelDePrueba() {
        // Esto carga un nivel real de tu repositorio,
        // manteniendo la lógica limpia y sin hardcodeo.
        cargarNivel(43)
    }


    private fun aplicarMovimiento(origen: Position, destino: Position) {
        _gameState.update { currentState ->
            val pieza = currentState.pieces.find { it.position == origen } ?: return@update currentState
            val piezaCapturada = currentState.pieces.find { it.position == destino }

            // 1. Promoción
            val esFilaPromocion = (pieza.type == PieceType.PEON) &&
                    ((pieza.color == PieceColor.ORO && destino.row == 0) ||
                            (pieza.color == PieceColor.PLATA && destino.row == currentState.boardSize - 1))
            val tipoFinal = if (esFilaPromocion) PieceType.REINA else pieza.type
            val piezaMovida = pieza.copy(position = destino, type = tipoFinal)

            // 2. Lógica de piezas: Limpiar origen/destino y añadir nueva posición
            var nuevasPiezas = currentState.pieces.filterNot {
                it.position == origen || it.position == destino
            }.plus(piezaMovida)

            // 3. Manejo de Enroque
            if (pieza.type == PieceType.REY && Math.abs(destino.col - origen.col) == 2) {
                val esCorto = destino.col > origen.col
                val colTorreOrigen = if (esCorto) 7 else 0
                val colTorreDestino = if (esCorto) 5 else 3

                val torre = currentState.pieces.find {
                    it.type == PieceType.TORRE && it.color == pieza.color &&
                            it.position.col == colTorreOrigen && it.position.row == origen.row
                }

                if (torre != null) {
                    val nuevaTorre = torre.copy(position = Position(origen.row, colTorreDestino))
                    nuevasPiezas = nuevasPiezas.filterNot { it == torre }.plus(nuevaTorre)
                }
            }

            // 4. Cementerio
            val nuevasComidasOro = if (piezaCapturada != null && piezaCapturada.color == PieceColor.ORO)
                currentState.piezasComidasOro + piezaCapturada else currentState.piezasComidasOro
            val nuevasComidasPlata = if (piezaCapturada != null && piezaCapturada.color == PieceColor.PLATA)
                currentState.piezasComidasPlata + piezaCapturada else currentState.piezasComidasPlata

            val turnoOponente = if (currentState.currentTurn == PieceColor.ORO) PieceColor.PLATA else PieceColor.ORO

            // 5. Evaluación (Jaque, Mate, etc)
            val enJaque = estaElReyEnJaque(turnoOponente, nuevasPiezas, currentState.boardSize)
            val esMate = enJaque && verificarSiEsJaqueMate(turnoOponente, nuevasPiezas, currentState.boardSize)
            val esAhogado = !enJaque && esAhogado(turnoOponente, nuevasPiezas, currentState.boardSize)
            val tablasMaterial = verificarTablasPorMaterial(nuevasPiezas)

            currentState.copy(
                pieces = nuevasPiezas,
                piezasComidasOro = nuevasComidasOro,
                piezasComidasPlata = nuevasComidasPlata,
                selectedPosition = null,
                validMoves = emptyList(),
                currentTurn = turnoOponente,
                esJaque = enJaque,
                esJaqueMate = esMate,
                esAhogado = esAhogado,
                esTablas = esAhogado || tablasMaterial,
                ganador = if (esMate) currentState.currentTurn else null,
                partidaIniciada = !(esMate || esAhogado || tablasMaterial)
            )
        }
    }
    // Asegúrate de que esta función acepte el tablero actual y no solo valide reglas de ajedrez puro
    fun realizarDesplazamiento(piezas: List<ChessPiece>, origen: Position, destino: Position): List<ChessPiece> {
        val nuevasPiezas = piezas.toMutableList()
        val piezaMovida = nuevasPiezas.find { it.position == origen } ?: return piezas

        // Eliminamos pieza destino si existe (captura)
        nuevasPiezas.removeAll { it.position == destino }

        // Movemos pieza
        val idx = nuevasPiezas.indexOf(piezaMovida)
        if (idx != -1) {
            nuevasPiezas[idx] = piezaMovida.copy(position = destino)
        }

        return nuevasPiezas
    }
    fun deshacerJugada() {
        val historial = _gameState.value.historialTableros
        if (historial.size >= 3) {
            val tableroAnterior = historial[historial.size - 3]
            _gameState.update { it.copy(pieces = tableroAnterior, historialTableros = historial.dropLast(2), currentTurn = PieceColor.ORO, esJaque = false, esJaqueMate = false, casillaPista = null) }
        } else if (historial.size == 2) {
            val tableroInicial = historial[0]
            _gameState.update { it.copy(pieces = tableroInicial, historialTableros = listOf(tableroInicial), currentTurn = PieceColor.ORO, esJaque = false, esJaqueMate = false, casillaPista = null) }
        }
    }
    fun obtenerPistaAyuda() {
        val estado = _gameState.value
        // AGREGADO: size
        val recomendacion = AIEngine.calcularMejorMovimiento(estado.pieces, estado.nivelActual, estado.boardSize)
        if (recomendacion != null) { _gameState.update { it.copy(casillaPista = recomendacion.second) } }
    }
    private fun ejecutarTurnoIA() {
        val estadoActual = _gameState.value
        if (estadoActual.esJaqueMate || estadoActual.esTablas) return

        val mejorMovimiento = AIEngine.calcularMejorMovimiento(estadoActual.pieces, estadoActual.nivelActual, estadoActual.boardSize)

        if (mejorMovimiento != null) {
            val (pieza, destino) = mejorMovimiento

            // 1. APLICAMOS LA REGLA DE PROMOCIÓN ANTES DE MOVER
            val piezaPromocionada = aplicarPromocionSiNecesaria(pieza, destino)
            val piezaMovida = piezaPromocionada.copy(position = destino)

            val piezaCapturada = estadoActual.pieces.find { it.position == destino }
            val nuevasPiezas = estadoActual.pieces.filterNot {
                it.position == pieza.position || it.position == destino
            }.plus(piezaMovida)

            val nuevasComidasOro = if (piezaCapturada != null && piezaCapturada.color == PieceColor.ORO)
                estadoActual.piezasComidasOro + piezaCapturada else estadoActual.piezasComidasOro
            val nuevasComidasPlata = if (piezaCapturada != null && piezaCapturada.color == PieceColor.PLATA)
                estadoActual.piezasComidasPlata + piezaCapturada else estadoActual.piezasComidasPlata

            val estadoIA = estadoActual.copy(
                pieces = nuevasPiezas,
                piezasComidasOro = nuevasComidasOro,
                piezasComidasPlata = nuevasComidasPlata,
                currentTurn = PieceColor.ORO,
                historialTableros = estadoActual.historialTableros + listOf(nuevasPiezas)
            )

            _gameState.update { estadoIA }

            // 2. Evaluamos el estado final usando el color del jugador (ORO)
            // para ver si la IA dejó al jugador en Jaque o Ahogado
            evaluarEstadoFinal(estadoIA, PieceColor.ORO)
        }
    }

    private fun aplicarPromocionSiNecesaria(pieza: ChessPiece, destino: Position): ChessPiece {
        // boardSize nos ayuda a saber cuál es la fila final sin importar el tamaño del tablero
        val esFilaPromocion = (pieza.color == PieceColor.ORO && destino.row == 0) ||
                (pieza.color == PieceColor.PLATA && destino.row == _gameState.value.boardSize - 1)

        return if (pieza.type == PieceType.PEON && esFilaPromocion) {
            pieza.copy(type = PieceType.REINA)
        } else {
            pieza
        }
    }

    // --- 5. LÓGICA DE REGLAS Y EVALUACIÓN (MOTOR DE VALIDACIÓN) ---
    // (Aquí van: evaluarEstadoFinal, estaElReyEnJaque, verificarSiEsJaqueMate, esAhogado, verificarTablasPorMaterial)
    private fun evaluarEstadoFinal(state: GameState, colorTurno: PieceColor) {
        // Pasamos el state.boardSize a todas las funciones
        val enJaque = estaElReyEnJaque(colorTurno, state.pieces, state.boardSize)
        val esMate = enJaque && verificarSiEsJaqueMate(colorTurno, state.pieces, state.boardSize)
        val esAhogado = !enJaque && esAhogado(colorTurno, state.pieces, state.boardSize)

        _gameState.update {
            it.copy(
                esJaque = enJaque,
                esJaqueMate = esMate,
                esAhogado = esAhogado,
                esTablas = esAhogado || verificarTablasPorMaterial(state.pieces),
                partidaIniciada = !(esMate || esAhogado)
            )
        }
    }

    private fun estaElReyEnJaque(colorDelRey: PieceColor, piezasTablero: List<ChessPiece>, size: Int): Boolean {
        val rey = piezasTablero.find { it.type == PieceType.REY && it.color == colorDelRey } ?: return false
        val colorOponente = if (colorDelRey == PieceColor.ORO) PieceColor.PLATA else PieceColor.ORO
        return MoveValidator.esCasillaAmenazadaPorGeometria(rey.position, colorOponente, piezasTablero, size)
    }

    private fun verificarSiEsJaqueMate(colorTurnoEntrante: PieceColor, piezasTablero: List<ChessPiece>, size: Int): Boolean {
        // Usamos la versión con 3 parámetros
        if (!estaElReyEnJaque(colorTurnoEntrante, piezasTablero, size)) return false

        val piezasAliadas = piezasTablero.filter { it.color == colorTurnoEntrante }

        for (pieza in piezasAliadas) {
            val movimientosPosibles = MoveValidator.obtenerMovimientosValidos(pieza, piezasTablero, size)
            for (destino in movimientosPosibles) {
                val simulacionPiezas = piezasTablero.filterNot { it.position == destino || it.position == pieza.position }
                    .plus(pieza.copy(position = destino))

                // Validación pasando size
                if (!estaElReyEnJaque(colorTurnoEntrante, simulacionPiezas, size)) return false
            }
        }
        return true
    }

    private fun esAhogado(color: PieceColor, piezas: List<ChessPiece>, size: Int): Boolean {
        // Validación pasando size
        if (estaElReyEnJaque(color, piezas, size)) return false

        val piezasAliadas = piezas.filter { it.color == color }

        for (pieza in piezasAliadas) {
            val movimientosPosibles = MoveValidator.obtenerMovimientosValidos(pieza, piezas, size)
            for (destino in movimientosPosibles) {
                val simulacion = piezas.filterNot { it.position == destino || it.position == pieza.position }
                    .plus(pieza.copy(position = destino))

                // Validación pasando size
                if (!estaElReyEnJaque(color, simulacion, size)) return false
            }
        }
        return true
    }

    private fun verificarTablasPorMaterial(piezas: List<ChessPiece>): Boolean = piezas.size == 2 && piezas.all { it.type == PieceType.REY }

    private fun esPiezaAtacandoCasilla(atacante: ChessPiece, objetivo: Position, piezas: List<ChessPiece>): Boolean {
        val dRow = objetivo.row - atacante.position.row
        val dCol = objetivo.col - atacante.position.col
        val absRow = Math.abs(dRow)
        val absCol = Math.abs(dCol)

        return when (atacante.type) {
            PieceType.CABALLO -> (absRow == 2 && absCol == 1) || (absRow == 1 && absCol == 2)
            PieceType.PEON -> {
                val dir = if (atacante.color == PieceColor.ORO) -1 else 1
                dRow == dir && absCol == 1
            }
            PieceType.REY -> absRow <= 1 && absCol <= 1
            PieceType.TORRE -> (dRow == 0 || dCol == 0) && !estaBloqueado(atacante.position, objetivo, piezas)
            PieceType.ALFIL -> (absRow == absCol) && !estaBloqueado(atacante.position, objetivo, piezas)
            PieceType.REINA -> ((dRow == 0 || dCol == 0) || (absRow == absCol)) && !estaBloqueado(atacante.position, objetivo, piezas)
            else -> false
        }
    }
    private fun estaBloqueado(origen: Position, destino: Position, piezas: List<ChessPiece>): Boolean {
        val dRow = Integer.signum(destino.row - origen.row)
        val dCol = Integer.signum(destino.col - origen.col)
        var currRow = origen.row + dRow
        var currCol = origen.col + dCol

        while (currRow != destino.row || currCol != destino.col) {
            if (piezas.any { it.position.row == currRow && it.position.col == currCol }) return true
            currRow += dRow
            currCol += dCol
        }
        return false
    }

    private fun obtenerTipoDesdeChar(c: Char): PieceType {
        return when (c.lowercaseChar()) {
            'p' -> PieceType.PEON
            'r' -> PieceType.TORRE
            'n' -> PieceType.CABALLO
            'b' -> PieceType.ALFIL
            'q' -> PieceType.REINA
            'k' -> PieceType.REY
            else -> PieceType.PEON
        }
    }
}