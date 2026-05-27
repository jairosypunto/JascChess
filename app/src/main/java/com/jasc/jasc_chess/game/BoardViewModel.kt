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
import com.jasc.jasc_chess.model.PuzzleRepository
import com.jasc.jasc_chess.model.ChessPuzzle
import com.jasc.jasc_chess.model.Move // Basado en tu archivo Move.kt
import android.util.Log
class BoardViewModel : ViewModel() {
    private var progresoPuzzle: Int = 0
    private val _gameState = MutableStateFlow(GameState())
    private var esTurnoDelJugadorEnPuzzle: Boolean = true
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()
    init {
        iniciarTemporizadorReloj() // Solo el reloj, nada de cargar niveles aquí
    }

    fun iniciarPartidaLibre() {
        val piezas = setupInitialBoard()
        _gameState.update { it.copy(
            pieces = piezas,
            modoJuego = GameMode.LIBRE,
            partidaIniciada = true,
            esJaqueMate = false
        )}
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
    fun reiniciarPartida() {
        val piezasIniciales = setupInitialBoard()
        _gameState.update {
            it.copy(
                pieces = piezasIniciales,
                historialTableros = listOf(piezasIniciales),
                selectedPosition = null,
                validMoves = emptyList(),
                currentTurn = PieceColor.ORO,
                esJaque = false,
                esJaqueMate = false,    // <--- ESTO ES LO QUE TE FALTA
                esTablas = false,       // <--- ESTO ES LO QUE TE FALTA
                ganador = null,
                partidaIniciada = true, // Reiniciamos el juego
                casillaPista = null
            )
        }
    }
    // Agrega esto dentro de tu función cargarNivel
    fun cargarNivel(id: Int) {
        val nivel = PuzzleRepository.levels.find { it.id == id }
        if (nivel != null) {
            // AQUÍ ESTÁ EL ERROR: asegúrate de que esto limpie todo
            _gameState.update { it.copy(
                pieces = emptyList(), // Limpia piezas previas
                currentPuzzle = nivel,
                modoJuego = GameMode.PUZZLE
            ) }
            cargarTableroDesdeFen(nivel.fen) // ESTA FUNCIÓN DEBE CARGAR LAS PIEZAS DEL FEN
        } else {
            Log.e("DEBUG_PUZZLE", "Nivel $id no encontrado")
        }
    }

    fun siguientePuzzle() {
        val idActual = _gameState.value.currentPuzzle?.id ?: 0
        val proximoId = idActual + 1
        val nivel = PuzzleRepository.levels.find { it.id == proximoId }

        if (nivel != null) {
            Log.d("DEBUG_PUZZLE", "Cargando siguiente puzzle: $proximoId")
            // 1. Resetear piezas para asegurar que se borre el tablero anterior
            _gameState.update { it.copy(pieces = emptyList(), currentPuzzle = nivel) }
            // 2. Cargar el nuevo FEN
            cargarTableroDesdeFen(nivel.fen)
        } else {
            Log.d("DEBUG_PUZZLE", "No hay más niveles, reiniciando al primer nivel disponible")
            // En lugar de forzar el 1, busca el primero de la lista
            val primerNivel = PuzzleRepository.levels.minByOrNull { it.id }
            if (primerNivel != null) {
                cargarNivel(primerNivel.id)
            }
        }
    }

    private fun cargarTableroDesdeFen(fen: String) {
        val nuevasPiezas = mutableListOf<ChessPiece>()
        // Tomamos solo la parte del tablero (antes del espacio)
        val boardPart = fen.split(" ")[0]
        val filas = boardPart.split("/")

        filas.forEachIndexed { rowIndex, rowString ->
            var colIndex = 0
            for (char in rowString) {
                when {
                    char.isDigit() -> {
                        // char.digitToInt() es la forma moderna en Kotlin
                        colIndex += char.digitToInt()
                    }
                    char.isLetter() -> {
                        val color = if (char.isUpperCase()) PieceColor.ORO else PieceColor.PLATA
                        val type = obtenerTipoDesdeChar(char)

                        // CORRECCIÓN: Usamos llaves {} para evitar que el compilador confunda el guion bajo
                        val id = "${type}_${color}_${rowIndex}_${colIndex}"

                        nuevasPiezas.add(ChessPiece(id, type, color, Position(rowIndex, colIndex)))
                        colIndex++
                    }
                }
            }
        }

        _gameState.update {
            it.copy(
                pieces = nuevasPiezas.toList(), // Convertimos a lista inmutable para asegurar actualización
                selectedPosition = null,
                validMoves = emptyList(),
                esJaqueMate = false
            )
        }
    }

// --- 2. CONTROL DEL RELOJ Y ESTADO ---
// (Aquí van: iniciarTemporizadorReloj, alternarActividadReloj, cambiarDificultad, cambiarTema, cambiarEstiloFichas, resetToLibre, iniciarModoPuzzle, iniciarPuzzleDePrueba)
    private fun iniciarTemporizadorReloj() {
    viewModelScope.launch {
        while (true) {
            delay(100)
            _gameState.update { state ->
                // EL RELOJ SE DETIENE SI: partidaIniciada es false o terminó la partida
                if (state.partidaIniciada && !state.esJaqueMate && !state.esTablas && state.modoJuego == GameMode.LIBRE) {
                    if (state.currentTurn == PieceColor.ORO) {
                        state.copy(oroTimeMillis = (state.oroTimeMillis - 100L).coerceAtLeast(0L))
                    } else {
                        state.copy(plataTimeMillis = (state.plataTimeMillis - 100L).coerceAtLeast(0L))
                    }
                } else {
                    state
                }
            }
        }
    }
}

    fun alternarActividadReloj() { _gameState.update { it.copy(partidaIniciada = !it.partidaIniciada) } }

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
    fun resetToLibre() {
        val piezasIniciales = setupInitialBoard()
        _gameState.update { it.copy(
            modoJuego = GameMode.LIBRE,
            pieces = piezasIniciales,
            partidaIniciada = true,
            esJaqueMate = false
        )}
    }
    fun iniciarModoPuzzle(puzzle: ChessPuzzle) {
        // 1. Cargamos el FEN primero
        cargarTableroDesdeFen(puzzle.fen)

        // 2. IMPORTANTE: Guardamos el objeto completo del puzzle en el estado
        _gameState.update { it.copy(
            modoJuego = GameMode.PUZZLE,
            currentPuzzle = puzzle, // Aquí guardas la lista de movimientos fresca
            partidaIniciada = true,
            esJaqueMate = false
        ) }
    }
    // OPCIÓN RECOMENDADA: Reemplaza la función antigua por un acceso directo al nivel 43
    fun iniciarNivelDePrueba() {
        // Esto carga un nivel real de tu repositorio,
        // manteniendo la lógica limpia y sin hardcodeo.
        cargarNivel(43)
    }

    // --- 3. GESTIÓN DE INTERACCIÓN (INPUT DEL USUARIO) ---
    // (Aquí va: onCellSelected)

    fun onCellSelected(pos: Position) {
        val state = _gameState.value

        if (state.modoJuego == GameMode.PUZZLE) {
            // --- LÓGICA DE PUZZLE ---
            if (state.selectedPosition == null) {
                // Seleccionar pieza propia
                val pieza = state.pieces.find { it.position == pos }
                if (pieza != null && pieza.color == state.currentTurn) {
                    _gameState.update { it.copy(selectedPosition = pos) }
                }
            } else {
                // Intentar mover la pieza seleccionada
                val origen = state.selectedPosition!!
                // En puzzles, verificamos si el destino es alcanzable (puedes usar MoveValidator o lógica simplificada)
                val movimientosLegales = MoveValidator.obtenerMovimientosValidos(
                    state.pieces.find { it.position == origen }!!, state.pieces
                )

                if (movimientosLegales.contains(pos)) {
                    // Ejecutamos el movimiento del jugador y activamos la respuesta de la IA del puzzle
                    gestionarModoPuzzle(origen, pos)
                    _gameState.update { it.copy(selectedPosition = null) }
                } else {
                    // Si el clic no es válido, deseleccionamos
                    _gameState.update { it.copy(selectedPosition = null) }
                }
            }
        } else {
            // --- LÓGICA DE PARTIDA LIBRE ---
            ejecutarMovimientoLibre(pos)
        }
    }

    // --- 4. EJECUCIÓN DE MOVIMIENTOS (MOTOR DE JUEGO) ---
    // (Aquí van: ejecutarMovimiento, ejecutarMovimientoLibre, gestionarModoPuzzle, aplicarMovimiento, realizarDesplazamiento, deshacerJugada, obtenerPistaAyuda, ejecutarTurnoIA, esTorreDeEnroque)
    fun ejecutarMovimiento(origen: Position, destino: Position) {
        val state = _gameState.value
        if (state.modoJuego == GameMode.PUZZLE) {
            gestionarModoPuzzle(origen, destino)
        } else {
            aplicarMovimiento(origen, destino)
        }
    }
    private fun ejecutarMovimientoLibre(position: Position) {
        val currentState = _gameState.value

        // 1. Si el juego terminó, bloqueamos cualquier acción
        if (currentState.esJaqueMate || currentState.esTablas) return

        if (currentState.selectedPosition == null) {
            // Lógica de selección original (la que ya tenías)
            val pieceAtPosition = currentState.pieces.find { it.position == position }
            if (pieceAtPosition != null && pieceAtPosition.color == currentState.currentTurn) {
                val movimientosLegales = MoveValidator.obtenerMovimientosValidos(pieceAtPosition, currentState.pieces).filter { destino ->
                    val tableroSimulado = currentState.pieces.filterNot { it.position == destino || it.position == pieceAtPosition.position }
                        .plus(pieceAtPosition.copy(position = destino))
                    !estaElReyEnJaque(currentState.currentTurn, tableroSimulado)
                }
                _gameState.update { it.copy(selectedPosition = position, fichaInspeccionada = position, validMoves = movimientosLegales) }
            }
        } else {
            // Lógica de ejecución
            if (currentState.validMoves.contains(position)) {
                val origen = currentState.selectedPosition!!

                // Ejecutamos el movimiento. Esta función YA evalúa jaque, mate y ahogado.
                aplicarMovimiento(origen, position)

                // Limpiamos selección y validamos si la IA debe jugar
                val estadoPostMovimiento = _gameState.value
                if (!estadoPostMovimiento.esJaqueMate && !estadoPostMovimiento.esTablas) {
                    viewModelScope.launch {
                        delay(600)
                        ejecutarTurnoIA()
                    }
                }
            } else {
                // Si el clic no fue en un movimiento válido, limpiamos la selección
                _gameState.update { it.copy(selectedPosition = null, fichaInspeccionada = null, validMoves = emptyList()) }
            }
        }
    }
    // Reemplaza tu función gestionarModoPuzzle actual por esta:


    // 2. Modifica gestionarModoPuzzle para seguir el guion
    private fun gestionarModoPuzzle(origen: Position, destino: Position) {
        if (!esTurnoDelJugadorEnPuzzle) return

        esTurnoDelJugadorEnPuzzle = false

        _gameState.update { state ->
            val piezasPostJugador = realizarDesplazamiento(state.pieces, origen, destino)

            // Obtenemos el siguiente movimiento de la secuencia (asumiendo que Move tiene from/to)
            val movimientoIA = state.currentPuzzle?.sequence?.getOrNull(progresoPuzzle)

            if (movimientoIA != null) {
                val piezasConIA = realizarDesplazamiento(piezasPostJugador, movimientoIA.from, movimientoIA.to)
                progresoPuzzle++ // Incrementamos el progreso
                state.copy(pieces = piezasConIA)
            } else {
                state.copy(pieces = piezasPostJugador)
            }
        }

        // Devolvemos turno
        esTurnoDelJugadorEnPuzzle = true
    }
    private fun aplicarMovimiento(origen: Position, destino: Position) {
        // PROTECCIÓN PARA PUZZLES
        if (_gameState.value.modoJuego == GameMode.PUZZLE) return
        _gameState.update { currentState ->
            val pieza = currentState.pieces.find { it.position == origen } ?: return@update currentState
            val piezaCapturada = currentState.pieces.find { it.position == destino }
            val esOro = currentState.currentTurn == PieceColor.ORO

            // --- 1. Lógica de Enroque ---
            val esEnroque = pieza.type == PieceType.REY && Math.abs(destino.col - origen.col) == 2

            // --- 2. Lógica de Promoción ---
            val esFilaPromocion = (pieza.color == PieceColor.ORO && destino.row == 0) ||
                    (pieza.color == PieceColor.PLATA && destino.row == 7)
            val tipoFinal = if (pieza.type == PieceType.PEON && esFilaPromocion) PieceType.REINA else pieza.type
            val piezaMovida = pieza.copy(position = destino, type = tipoFinal)

            var nuevasPiezas = currentState.pieces.filterNot { it.position == origen || it.position == destino }
                .plus(piezaMovida)

            if (esEnroque) {
                val torre = currentState.pieces.find { esTorreDeEnroque(it, destino) }
                if (torre != null) {
                    val colDestinoTorre = if (destino.col < origen.col) 3 else 5
                    nuevasPiezas = nuevasPiezas.filterNot { it == torre }
                        .plus(torre.copy(position = Position(origen.row, colDestinoTorre)))
                }
            }

            // --- 3. Lógica de Capturas ---
            val nuevasComidasOro = if (piezaCapturada != null && !esOro) currentState.piezasComidasOro + piezaCapturada else currentState.piezasComidasOro
            val nuevasComidasPlata = if (piezaCapturada != null && esOro) currentState.piezasComidasPlata + piezaCapturada else currentState.piezasComidasPlata

            val estadoIntermedio = currentState.copy(
                pieces = nuevasPiezas,
                piezasComidasOro = nuevasComidasOro,
                piezasComidasPlata = nuevasComidasPlata,
                selectedPosition = null,
                fichaInspeccionada = null, // Limpia el zoom al terminar el movimiento
                validMoves = emptyList(),
                currentTurn = if (esOro) PieceColor.PLATA else PieceColor.ORO,
                haMovidoRey = if (pieza.type == PieceType.REY) true else currentState.haMovidoRey
            )

            // --- 4. Evaluación ---
            val turnoOponente = if (esOro) PieceColor.PLATA else PieceColor.ORO
            val enJaque = estaElReyEnJaque(turnoOponente, estadoIntermedio.pieces)
            val esMate = enJaque && verificarSiEsJaqueMate(turnoOponente, estadoIntermedio.pieces)
            val esAhogado = !enJaque && esAhogado(turnoOponente, estadoIntermedio.pieces)
            val tablasPorMaterial = verificarTablasPorMaterial(estadoIntermedio.pieces)

            estadoIntermedio.copy(
                esJaque = enJaque,
                esJaqueMate = esMate,
                esAhogado = esAhogado,
                esTablas = esAhogado || tablasPorMaterial,
                ganador = if (esMate) (if (turnoOponente == PieceColor.ORO) PieceColor.PLATA else PieceColor.ORO) else null,
                partidaIniciada = !(esMate || esAhogado || tablasPorMaterial)
            )
        }
    }
    // Asegúrate de que esta función acepte el tablero actual y no solo valide reglas de ajedrez puro
    fun realizarDesplazamiento(piezas: List<ChessPiece>, origen: Position, destino: Position): List<ChessPiece> {
        val nuevasPiezas = piezas.toMutableList()

        // 1. Eliminamos la pieza en destino si existe (captura)
        nuevasPiezas.removeAll { it.position == destino }

        // 2. Movemos la pieza de origen
        val pieza = nuevasPiezas.find { it.position == origen }
        if (pieza != null) {
            val indice = nuevasPiezas.indexOf(pieza)
            nuevasPiezas[indice] = pieza.copy(position = destino)
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
        val recomendacion = AIEngine.calcularMejorMovimiento(estado.pieces, estado.nivelActual)
        if (recomendacion != null) { _gameState.update { it.copy(casillaPista = recomendacion.second) } }
    }
    private fun ejecutarTurnoIA() {
        val estadoActual = _gameState.value
        if (estadoActual.esJaqueMate || estadoActual.esTablas) return

        val mejorMovimiento = AIEngine.calcularMejorMovimiento(estadoActual.pieces, estadoActual.nivelActual)

        if (mejorMovimiento != null) {
            val (pieza, destino) = mejorMovimiento

            // 1. APLICAMOS LA REGLA DE PROMOCIÓN ANTES DE MOVER
            val piezaPromocionada = aplicarPromocionSiNecesaria(pieza, destino)
            val piezaMovida = piezaPromocionada.copy(position = destino)

            val piezaCapturada = estadoActual.pieces.find { it.position == destino }
            val nuevasPiezas = estadoActual.pieces.filterNot {
                it.position == pieza.position || it.position == destino
            }.plus(piezaMovida)

            val nuevasComidasOro = if (piezaCapturada != null) estadoActual.piezasComidasOro + piezaCapturada else estadoActual.piezasComidasOro

            val estadoIA = estadoActual.copy(
                pieces = nuevasPiezas,
                piezasComidasOro = nuevasComidasOro,
                currentTurn = PieceColor.ORO,
                historialTableros = estadoActual.historialTableros + listOf(nuevasPiezas)
            )

            _gameState.update { estadoIA }

            // 2. Evaluamos el estado final usando el color del jugador (ORO)
            evaluarEstadoFinal(estadoIA, PieceColor.ORO)
        }
    }

    // Agrega esta función en la sección de UTILIDADES o al final del ViewModel
    private fun ejecutarDefensaIA(piezasActuales: List<ChessPiece>) {
        viewModelScope.launch {
            delay(600) // Un tiempo razonable

            val mejorDefensa = AIEngine.calcularMejorMovimiento(piezasActuales, NivelDificultad.INFIERNO)

            if (mejorDefensa != null) {
                val (pieza, destino) = mejorDefensa
                val nuevasPiezas = realizarDesplazamiento(piezasActuales, pieza.position, destino)

                // ACTUALIZACIÓN DE ESTADO SEGURA
                _gameState.update { it.copy(pieces = nuevasPiezas) }
                Log.d("DEBUG_IA", "IA movió pieza a $destino")
            } else {
                Log.e("DEBUG_IA", "La IA no encontró movimiento")
            }

            // Devolvemos el turno SIEMPRE
            esTurnoDelJugadorEnPuzzle = true
        }
    }

    // Agrega esto como función privada en tu BoardViewModel
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
    val enJaque = estaElReyEnJaque(colorTurno, state.pieces)
    val esMate = enJaque && verificarSiEsJaqueMate(colorTurno, state.pieces)
    val esAhogado = !enJaque && esAhogado(colorTurno, state.pieces)
    val tablasPorMaterial = verificarTablasPorMaterial(state.pieces)

    Log.d("JascChessDebug", "EVALUACIÓN: Turno $colorTurno | Jaque: $enJaque | Mate: $esMate | Ahogado: $esAhogado")

    _gameState.update {
        it.copy(
            esJaque = enJaque,
            esJaqueMate = esMate,
            esAhogado = esAhogado,
            esTablas = esAhogado || tablasPorMaterial,
            ganador = if (esMate) (if (colorTurno == PieceColor.ORO) PieceColor.PLATA else PieceColor.ORO) else null,
            partidaIniciada = !(esMate || esAhogado || tablasPorMaterial)
        )
    }
}
    private fun estaElReyEnJaque(colorDelRey: PieceColor, piezasTablero: List<ChessPiece>): Boolean {
        val rey = piezasTablero.find { it.type == PieceType.REY && it.color == colorDelRey } ?: return false
        val colorOponente = if (colorDelRey == PieceColor.ORO) PieceColor.PLATA else PieceColor.ORO

        // USAMOS TU FUNCIÓN "CASILLAESTAATACADA" (Esto hará que deje de estar en gris)
        return casillaEstaAtacada(rey.position, colorOponente, piezasTablero)
    }
    private fun verificarSiEsJaqueMate(colorTurnoEntrante: PieceColor, piezasTablero: List<ChessPiece>): Boolean {
        if (!estaElReyEnJaque(colorTurnoEntrante, piezasTablero)) return false

        val piezasAliadas = piezasTablero.filter { it.color == colorTurnoEntrante }

        for (pieza in piezasAliadas) {
            val movimientosPosibles = MoveValidator.obtenerMovimientosValidos(pieza, piezasTablero)
            for (destino in movimientosPosibles) {
                val simulacionPiezas = piezasTablero.filterNot { it.position == destino || it.position == pieza.position }
                    .plus(pieza.copy(position = destino))

                if (!estaElReyEnJaque(colorTurnoEntrante, simulacionPiezas)) {
                    Log.d("JascChessMate", "NO es Mate. Salvación: ${pieza.type} a $destino")
                    return false
                }
            }
        }
        Log.d("JascChessMate", "MATE DETECTADO para $colorTurnoEntrante")
        return true
    }
    private fun esAhogado(color: PieceColor, piezas: List<ChessPiece>): Boolean {
        // 1. Si está en jaque, NO es ahogado.
        if (estaElReyEnJaque(color, piezas)) return false

        // 2. Si tiene cualquier movimiento legal, NO es ahogado.
        val piezasAliadas = piezas.filter { it.color == color }
        for (pieza in piezasAliadas) {
            val movimientosPosibles = MoveValidator.obtenerMovimientosValidos(pieza, piezas)
            for (destino in movimientosPosibles) {
                val simulacion = piezas.filterNot { it.position == destino || it.position == pieza.position }
                    .plus(pieza.copy(position = destino))

                if (!estaElReyEnJaque(color, simulacion)) {
                    return false // ¡Encontró un movimiento legal!
                }
            }
        }

        Log.d("JascChessMate", "AHOGADO DETECTADO para $color")
        return true
    }
    private fun verificarTablasPorMaterial(piezas: List<ChessPiece>): Boolean = piezas.size == 2 && piezas.all { it.type == PieceType.REY }

    // --- 6. UTILIDADES DE ATAQUE (NO RECURSIVAS) ---
    // (Aquí van: casillaEstaAtacada, esPiezaAtacandoCasilla, estaBloqueado)
    fun casillaEstaAtacada(posicion: Position, atacanteColor: PieceColor, piezas: List<ChessPiece>): Boolean {
    return piezas.any { pieza ->
        pieza.color == atacanteColor && esPiezaAtacandoCasilla(pieza, posicion, piezas)
    }
}
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

    // --- 7. UTILIDADES AUXILIARES Y DE FORMATO ---

    private fun esTorreDeEnroque(piece: ChessPiece, destinoRey: Position): Boolean {
        if (piece.type != PieceType.TORRE) return false
        val colRequerida = if (destinoRey.col == 6) 7 else 0
        return piece.position.col == colRequerida && piece.position.row == destinoRey.row
    }

    private fun obtenerTipoDesdeChar(char: Char): PieceType {
        return when (char.lowercaseChar()) {
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