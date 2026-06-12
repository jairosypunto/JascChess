package com.jasc.jasc_chess.game

import android.content.Context
import com.jasc.jasc_chess.data.local.PreferencesManager
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
import com.jasc.jasc_chess.audio.SoundManager // <--- ESTO ES LO QUE FALTA PARA EL ERROR DE "Unresolved reference 'SoundManager'"
import com.jasc.jasc_chess.R
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

    fun activarModoEdicion() {
        vaciarTablero()
        // Activamos modo edición dentro del flujo de estado
        _gameState.update { it.copy(isEditingMode = true) }
    }

    fun obtenerMensajeDeRecompensa(puntos: Int): String? {
        return when {
            puntos >= 1000 && puntos < 1100 -> "¡Felicidades! Has alcanzado los 1000 puntos. ¡Eres un estratega nato!"
            puntos >= 2500 && puntos < 2600 -> "¡Impresionante! 2500 puntos. ¡El dominio del tablero es tuyo!"
            // Puedes añadir más hitos aquí
            else -> null
        }
    }

    fun configurarPartida(
        nuevoSize: Int,
        nuevoModo: GameMode,
        puzzle: ChessPuzzle? = null,
        nivel: Int = 0 // Agregamos nivel opcional
    ) {
        Log.d("JASC_DEBUG", "Configurando: nivel=$nivel, tamaño=$nuevoSize, modo=$nuevoModo")

        val piezasNuevas = try {
            when {
                // PRIORIDAD 1: Si solicitaste un nivel de la base de datos
                nivel > 0 -> {
                    NivelRepository.obtenerNivel(nivel)?.piezas ?: NivelRepository.generarSetupPorDefecto(nuevoSize)
                }

                // PRIORIDAD 2: Si es un puzzle clásico (de los que ya tenías)
                puzzle != null -> FENParser.parse(puzzle.fen, nuevoSize)

                // PRIORIDAD 3: Modo Libre
                nuevoModo == GameMode.LIBRE && nuevoSize == 8 -> generarSetupEstandar8x8()

                // PRIORIDAD 4: Fallback inteligente (usar generador profesional)
                else -> NivelRepository.generarSetupProfesional(nuevoSize)
            }
        } catch (e: Exception) {
            Log.e("JASC_ERROR", "Error al configurar: ${e.message}")
            generarSetupEstandar8x8()
        }

        _gameState.update { currentState ->
            currentState.copy(
                boardSize = if (nivel > 0) (NivelRepository.obtenerNivel(nivel)?.size ?: nuevoSize) else nuevoSize,
                pieces = piezasNuevas,
                modoJuego = nuevoModo,
                nivelActualInt = nivel, // Guardamos el nivel actual
                currentPuzzle = puzzle,
                // ... (resto de tus campos iguales)
                partidaIniciada = true,
                selectedPosition = null,
                validMoves = if (nuevoModo == GameMode.PUZZLE && puzzle != null) puzzle.requiredMoves.toList() else emptyList(),
                esJaqueMate = false,
                victoriaMostrada = false,
                lastUpdate = System.currentTimeMillis()
            )
        }
    }
    fun esCoronacionValida(pieza: ChessPiece, destino: Position, size: Int): Boolean {
        if (pieza.type != PieceType.PEON) return false
        val filaMeta = if (pieza.color == PieceColor.ORO) 0 else size - 1
        return destino.row == filaMeta
    }

    var modoEdicion by mutableStateOf(false)

    // 1. Variable de estado para la pieza en mano (Observable por Compose)
    var piezaSeleccionadaParaColocar by mutableStateOf<Pair<PieceType, PieceColor>?>(null)

    // 2. Función para manejar el clic en la casilla durante la edición
// Asegúrate de que esta lógica esté en tu ViewModel
    fun manejarEdicionTablero(pos: Position) {
        val seleccion = piezaSeleccionadaParaColocar ?: return // Si no hay nada seleccionado, no hacemos nada

        _gameState.update { currentState ->
            val piezasActuales = currentState.pieces.toMutableList()
            val piezaExistente = piezasActuales.find { it.position == pos }

            val nuevasPiezas = when {
                // Caso: La casilla ya tiene la MISMA pieza que tengo seleccionada -> BORRAR
                piezaExistente != null && piezaExistente.type == seleccion.first && piezaExistente.color == seleccion.second -> {
                    piezasActuales.remove(piezaExistente)
                    piezasActuales
                }
                // Caso: La casilla tiene OTRA pieza -> REEMPLAZAR
                piezaExistente != null -> {
                    piezasActuales.remove(piezaExistente)
                    piezasActuales.add(crearPieza(seleccion, pos))
                    piezasActuales
                }
                // Caso: La casilla está vacía -> COLOCAR
                else -> {
                    piezasActuales.add(crearPieza(seleccion, pos))
                    piezasActuales
                }
            }
            currentState.copy(pieces = nuevasPiezas.toList())
        }
    }

    private fun crearPieza(seleccion: Pair<PieceType, PieceColor>, pos: Position): ChessPiece {
        return ChessPiece(
            id = "p_${pos.row}_${pos.col}_${System.currentTimeMillis()}",
            type = seleccion.first,
            color = seleccion.second,
            position = pos
        )
    }

    fun cerrarModoEdicion() {
        val nivelActual = _gameState.value.nivelActualInt // Obtenemos el nivel en el que estás

        _gameState.update { currentState ->
            currentState.copy(
                isEditingMode = false,
                selectedPosition = null,
                pieces = generarPiezasParaModo(currentState.boardSize, nivelActual) // ESTA LÍNEA RECARGA EL NIVEL
            )
        }
        piezaSeleccionadaParaColocar = null
        Log.d("JASC_EDICION", "Modo edición cerrado y tablero restaurado.")
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

    fun iniciarModoLibre(size: Int) {
        // Si es 8x8, forzamos un setup estándar. Si es 4x4, el que tengas en el Repository.
        val piezasNuevas = if (size == 8) {
            generarSetupEstandar8x8()
        } else {
            NivelRepository.generarSetupPorDefecto(size)
        }

        _gameState.update { currentState ->
            currentState.copy(
                boardSize = size,
                modoJuego = GameMode.LIBRE,
                pieces = piezasNuevas,
                currentPuzzle = null,
                nivelActualInt = 0,
                currentTurn = PieceColor.ORO,
                selectedPosition = null,
                validMoves = emptyList(),
                esJaqueMate = false,
                esTablas = false,
                esJaque = false,
                esAhogado = false,
                piezasComidasOro = emptyList(),
                piezasComidasPlata = emptyList(),
                historialTableros = listOf(piezasNuevas),
                esJuegoBloqueado = false
            )
        }
    }

    private fun generarSetupEstandar8x8(): List<ChessPiece> {
        val piezas = mutableListOf<ChessPiece>()

        // Posiciones de las piezas mayores (FILA 0 y FILA 7)
        val ordenPiezas = listOf(PieceType.TORRE, PieceType.CABALLO, PieceType.ALFIL, PieceType.REINA, PieceType.REY, PieceType.ALFIL, PieceType.CABALLO, PieceType.TORRE)

        for (i in 0..7) {
            // Filas de peones
            piezas.add(ChessPiece("p_oro_$i", PieceType.PEON, PieceColor.ORO, Position(6, i)))
            piezas.add(ChessPiece("p_plata_$i", PieceType.PEON, PieceColor.PLATA, Position(1, i)))

            // Filas principales
            piezas.add(ChessPiece("major_oro_$i", ordenPiezas[i], PieceColor.ORO, Position(7, i)))
            piezas.add(ChessPiece("major_plata_$i", ordenPiezas[i], PieceColor.PLATA, Position(0, i)))
        }

        return piezas
    }

    private fun ejecutarMovimientoLibre(position: Position) {
        val currentState = _gameState.value
        val origen = currentState.selectedPosition

        if (origen == null) {
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
            val movimiento = currentState.validMoves.find { it.to == position }
            if (movimiento != null) {
                aplicarMovimiento(origen, position)

                val estadoPostMovimiento = _gameState.value
                evaluarEstadoFinal(estadoPostMovimiento.pieces, estadoPostMovimiento.currentTurn)

                val estadoFinal = _gameState.value
                // Solo IA si el turno pasó a PLATA y el juego sigue vivo
                if (estadoFinal.currentTurn == PieceColor.PLATA && !estadoFinal.esJaqueMate && !estadoFinal.esTablas) {
                    viewModelScope.launch {
                        delay(500)
                        ejecutarTurnoIA()
                    }
                }
            } else {
                // Si toca una casilla distinta a las válidas, deselecciona
                _gameState.update { it.copy(selectedPosition = null, validMoves = emptyList()) }
            }
        }
    }

    fun validarJugadaPuzzle(origen: Position, destino: Position) {
        val state = _gameState.value

        // 1. Verificamos si el movimiento es válido
        val movimiento = state.validMoves.find { it.to == destino } ?: return

        val piezasDespues = realizarDesplazamiento(state.pieces, origen, destino)
        val nuevoStep = state.puzzleStepIndex + 1

        // 2. Evaluamos el estado tras el movimiento
        evaluarEstadoFinal(piezasDespues, PieceColor.PLATA)
        val estadoPostMovimiento = _gameState.value

        // Caso A: El usuario logró el Jaque Mate
        if (estadoPostMovimiento.esJaqueMate) {
            _gameState.update { it.copy(
                pieces = piezasDespues,
                puzzleResuelto = true,
                mensajeFinal = "¡Nivel ${state.nivelActualInt} Superado!",
                mensajeError = null // Limpiamos cualquier error previo
            )}
        }
        // Caso B: El usuario falló el Mate en 2 (agotó los pasos o movimiento incorrecto)
        else if (nuevoStep >= 2) {
            _gameState.update { it.copy(
                esJuegoBloqueado = true,
                selectedPosition = null,
                validMoves = emptyList(),
                // Activamos el aviso de error para que la UI lo muestre
                mensajeError = "¡Intenta Mate en 2!"
            )}

            viewModelScope.launch {
                delay(1500) // Tiempo para que el usuario lea el mensaje
                // Limpiamos el error y recargamos el nivel
                _gameState.update { it.copy(mensajeError = null) }
                cargarPartida(state.nivelActualInt)
            }
        }
        // Caso C: Continuar puzzle (primera jugada realizada, esperando respuesta IA)
        else {
            _gameState.update { it.copy(
                pieces = piezasDespues,
                puzzleStepIndex = nuevoStep,
                selectedPosition = null,
                validMoves = emptyList()
            )}

            viewModelScope.launch {
                delay(500)
                if (state.currentPuzzle != null) ejecutarRespuestaIA(state.currentPuzzle)
                else ejecutarTurnoIA()
            }
        }
    }

    fun reiniciarPartidaLibre() {
        val sizeActual = _gameState.value.boardSize

        // FORZAMOS EL SETUP ESTÁNDAR SI ES 8X8
        val piezasNuevas = if (sizeActual == 8) {
            generarSetupEstandar8x8()
        } else {
            NivelRepository.generarSetupPorDefecto(sizeActual)
        }

        _gameState.update {
            it.copy(
                pieces = piezasNuevas,
                historialTableros = listOf(piezasNuevas),
                esJaqueMate = false,
                esTablas = false,
                esAhogado = false,
                esJaque = false,
                victoriaMostrada = false, // <--- AÑADIDO: Resetear bandera de victoria
                currentTurn = PieceColor.ORO,
                selectedPosition = null,
                validMoves = emptyList(),
                piezasComidasOro = emptyList(),
                piezasComidasPlata = emptyList(),
                esJuegoBloqueado = false,
                ganador = null,
                casillaPista = null,
                puzzleStepIndex = 0
            )
        }
        Log.d("JASC_REINICIO", "Reinicio forzado a estándar 8x8 completado")
    }

    fun reiniciarSegunModo() {
        if (_gameState.value.modoJuego == GameMode.LIBRE) {
            reiniciarPartidaLibre()
        } else {
            reiniciarPartida()
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
        // 1. Intentamos obtener la config del repositorio
        val config = NivelRepository.obtenerNivel(n)

        // 2. Validación defensiva: Si no existe, no hacemos un return silencioso.
        // Esto evita que el tablero se quede en blanco.
        if (config == null) {
            Log.e("JASC_ERROR", "Intento de cargar nivel $n que no existe en repositorio.")
            return
        }

        // 3. Actualización de estado garantizada
        _gameState.update { currentState ->
            currentState.copy(
                nivelActualInt = n,
                boardSize = config.size,
                // Usamos la lógica centralizada de piezas
                pieces = generarPiezasParaModo(config.size, n),
                modoJuego = GameMode.PUZZLE,
                currentTurn = config.turnoInicial,
                esJaque = false,
                esJaqueMate = false,
                estaCargandoNivel = true,
                esJuegoBloqueado = false,
                puzzleStepIndex = 0,
                selectedPosition = null,
                validMoves = emptyList() // Sin <Move> (inferencia de tipos)
            )
        }

        // 4. Desbloqueo tras carga con manejo de Scope
        viewModelScope.launch {
            delay(500) // Tiempo para que la UI procese el cambio
            _gameState.update { it.copy(estaCargandoNivel = false) }
            Log.d("JASC_DEBUG", "Nivel $n cargado y desbloqueado exitosamente.")
        }
    }

    private fun generarPiezasParaModo(size: Int, nivel: Int? = null): List<ChessPiece> {
        // 1. PRIORIDAD: Si hay un nivel, búscarlo en el repositorio.
        if (nivel != null) {
            val config = NivelRepository.obtenerNivel(nivel)
            if (config != null) {
                Log.d("JASC_CARGA", "Nivel $nivel cargado desde Repositorio.")
                return config.piezas
            } else {
                Log.e("JASC_CARGA", "Nivel $nivel NO ENCONTRADO en Repositorio.")
            }
        }

        // 2. FALLBACK: Si no hay nivel o no existe en el repositorio, generamos dinámico.
        Log.d("JASC_CARGA", "Generando setup dinámico para tamaño $size.")
        return NivelRepository.generarSetupPorDefecto(size)
    }

    fun avanzarAlSiguienteNivel(context: Context) {
        val estadoActual = _gameState.value
        val nivelActual = estadoActual.nivelActualInt
        val siguienteNivel = nivelActual + 1
        val totalNiveles = NivelRepository.totalNiveles.size

        // 1. Verificamos si existe el siguiente nivel
        if (NivelRepository.totalNiveles.containsKey(siguienteNivel)) {

            // 2. Lógica de mensajes motivadores según el rango
            val mensaje = when (siguienteNivel) {
                in 11..20 -> "¡Nivel $nivelActual superado! Pasaste a la parte media. ¡Bien hecho!"
                in 21..30 -> "¡Impresionante! De $nivelActual a $siguienteNivel. Ya tienes una lógica excelente."
                in 31..40 -> "¡Eres un crack del ajedrez! Dominando el nivel $siguienteNivel."
                else -> "¡Nivel $nivelActual completado! Vamos por el $siguienteNivel."
            }

            // 3. Guardamos progreso y avanzamos
            PreferencesManager.guardarNivelMaximo(siguienteNivel, context)

            // Actualizamos estado con el mensaje motivador antes de cambiar
            _gameState.update { it.copy(mensajeFinal = mensaje) }

            // Cargamos el nuevo nivel automáticamente como querías
            viewModelScope.launch {
                delay(1500) // Un pequeño respiro para leer el mensaje
                cambiarNivel(siguienteNivel)
            }
        } else {
            // 4. Llegamos al final (Coronación)
            _gameState.update {
                it.copy(
                    puzzleResuelto = true,
                    mensajeFinal = "¡FELICIDADES! Has completado los $totalNiveles niveles. Eres un Maestro del Ajedrez."
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
                SoundManager.play(R.raw.capture)
                piezas.remove(piezaCapturada)
                if (piezaCapturada.color == PieceColor.ORO) nuevasComidasOro.add(piezaCapturada)
                else nuevasComidasPlata.add(piezaCapturada)
            } else {
                // --- LÓGICA DE SONIDO ESPECIAL ---
                if (piezaMoviendose.type == PieceType.CABALLO) {
                    SoundManager.play(R.raw.knight) // Sonido especial para el caballo
                } else {
                    SoundManager.play(R.raw.move)   // Sonido normal para el resto
                }
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
                pieces = piezas,
                piezasComidasOro = nuevasComidasOro,
                piezasComidasPlata = nuevasComidasPlata,
                esJaque = enJaque,
                esJaqueMate = esMate,
                esAhogado = esAhogado,
                esTablas = (esAhogado || verificarTablasPorMaterial(piezas)) && !esMate,
                selectedPosition = null,
                validMoves = emptyList(),
                currentTurn = turnoSiguiente,
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

        // 1. Validamos que tengamos al menos un estado previo al actual
        if (historial.size >= 2) {
            // Obtenemos el historial sin el estado actual (el penúltimo es el que queremos)
            val nuevoHistorial = historial.dropLast(1)
            val tableroAnterior = nuevoHistorial.last()

            // 2. Validación mejorada:
            // No borres la partida solo porque el tamaño sea diferente.
            // Si el historial existe, confiamos en él.
            _gameState.update { state ->
                state.copy(
                    pieces = tableroAnterior,
                    historialTableros = nuevoHistorial,
                    currentTurn = if (state.currentTurn == PieceColor.ORO) PieceColor.PLATA else PieceColor.ORO,

                    // Reseteamos estados críticos de fin de juego
                    esJaque = false,
                    esJaqueMate = false,
                    esTablas = false,
                    esAhogado = false,
                    ganador = null,

                    // Limpieza de selección para evitar errores de redibujado
                    selectedPosition = null,
                    validMoves = emptyList(),

                    // IMPORTANTE: Si tu juego mueve piezas al cementerio,
                    // aquí deberías restaurar también el estado previo de las piezas comidas
                    // Asegúrate de que tu 'historialTableros' guarde también el estado del cementerio.
                )
            }
            Log.d("JASC_DEBUG", "Jugada deshecha correctamente. Turno: ${_gameState.value.currentTurn}")
        } else {
            Log.d("JASC_DEBUG", "No hay más movimientos para deshacer.")
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

    // Asegúrate de que esta función sea llamada donde haces el movimiento de piezas
// 1. Función limpia, sin dependencia de Context
    private fun evaluarEstadoFinal(piezas: List<ChessPiece>, colorTurno: PieceColor) {
        val currentState = _gameState.value
        if (currentState.estaCargandoNivel) return

        val size = currentState.boardSize
        val enJaque = estaElReyEnJaque(colorTurno, piezas, size)
        val esMate = enJaque && verificarSiEsJaqueMate(colorTurno, piezas, size)
        val esAhogado = !enJaque && esAhogado(colorTurno, piezas, size)
        val esTablasMaterial = verificarTablasPorMaterial(piezas)

        val reyDerrotado = if (esMate) piezas.find { it.type == PieceType.REY && it.color == colorTurno } else null

        _gameState.update { state ->
            state.copy(
                pieces = if (esMate) piezas.map { if (it == reyDerrotado) it.copy(isFallen = true) else it } else piezas,
                esJaque = enJaque,
                esJaqueMate = esMate,
                esJuegoBloqueado = false,
                posicionReyDerrotado = reyDerrotado?.position,
                esAhogado = esAhogado,
                esTablas = esAhogado || esTablasMaterial,
                ganador = if (esMate) (if (colorTurno == PieceColor.ORO) PieceColor.PLATA else PieceColor.ORO) else null
            )
        }

        // --- INTEGRACIÓN DE PUNTOS Y VICTORIA ---
        if (esMate) {
            SoundManager.play(R.raw.victoria) // Asegúrate de tener sonido de victoria

            // Aquí sumamos puntos y verificamos evento de video
            // IMPORTANTE: Debes pasar el 'context' desde tu vista,
            // o si prefieres, usa un método que no requiera contexto si ya guardas en memoria
            procesarFinDeNivel(currentState.nivelActualInt)
        } else if (enJaque) {
            SoundManager.play(R.raw.check)
        }
    }

// En BoardViewModel.kt

    // Añade esta variable para acumular puntos en memoria durante la partida
    private var puntosAcumuladosEnSesion = 0

    // Dentro de BoardViewModel
    private var _appContext: Context? = null // <-- AGREGA ESTA VARIABLE

    fun setContext(context: Context) {
        _appContext = context.applicationContext
    }
    private fun procesarFinDeNivel(nivel: Int) {
        val context = _appContext ?: return

        val puntosPorNivel = 100
        val nuevosPuntos = _gameState.value.puntosTotales + puntosPorNivel

        // 1. Guardar y actualizar puntos
        PreferencesManager.guardarPuntos(puntosPorNivel, context)
        _gameState.update { it.copy(puntosTotales = nuevosPuntos) }

        // 2. Lógica de Video (Prioridad alta)
        if (nivel % 10 == 0) {
            val videoRes = when (nivel) {
                10 -> R.raw.video_felicitacion_10
                20 -> R.raw.video_felicitacion_20
                else -> R.raw.video_felicitacion_default
            }
            _gameState.update { it.copy(videoEventoPendiente = videoRes) }
        }
        // 3. Lógica de Recompensa (Solo si no hubo video, para no saturar la pantalla)
        else {
            val mensaje = obtenerMensajeDeRecompensa(nuevosPuntos)
            if (mensaje != null) {
                _gameState.update { it.copy(mensajeError = mensaje) }

                // Limpiar el mensaje automáticamente después de 3 segundos
                viewModelScope.launch {
                    delay(3000)
                    _gameState.update { it.copy(mensajeError = null) }
                }
            }
        }
    }

    // NUEVA FUNCIÓN PARA PERSISTIR (Llamada desde la UI al cerrar la partida)
    fun guardarProgresoFinal(context: Context) {
        PreferencesManager.guardarPuntos(puntosAcumuladosEnSesion, context)
        puntosAcumuladosEnSesion = 0 // Reiniciamos el contador de sesión
    }

    // En BoardViewModel.kt
    fun limpiarVideoEvento() {
        _gameState.update { it.copy(videoEventoPendiente = null) }
    }
    private fun ejecutarTurnoIA() {
        val estadoActual = _gameState.value
        val piezasIA = estadoActual.pieces.filter { it.color == PieceColor.PLATA }

        val todosLosMovimientosLegales = mutableListOf<Move>()
        for (pieza in piezasIA) {
            val destinos = MoveValidator.obtenerMovimientosValidos(pieza, estadoActual.pieces, estadoActual.boardSize)
            for (destino in destinos) {
                todosLosMovimientosLegales.add(Move(from = pieza.position, to = destino))
            }
        }

        val mejorMovimiento = AIEngine.calcularMejorMovimiento(estadoActual.pieces, estadoActual.nivelActual, estadoActual.boardSize)

        val movimientoElegido = if (mejorMovimiento != null) {
            Move(from = mejorMovimiento.first.position, to = mejorMovimiento.second)
        } else {
            todosLosMovimientosLegales.firstOrNull()
        }

        if (movimientoElegido != null) {
            val piezasActualizadas = realizarDesplazamiento(estadoActual.pieces, movimientoElegido.from, movimientoElegido.to)
            _gameState.update { it.copy(pieces = piezasActualizadas, currentTurn = PieceColor.ORO) }
            evaluarEstadoFinal(piezasActualizadas, PieceColor.ORO)
        } else {
            Log.d("JASC_IA", "No hay movimientos legales, fin de partida.")
            evaluarEstadoFinal(estadoActual.pieces, PieceColor.ORO)
        }
    }

    fun reiniciarTodoElProgreso() {
        _gameState.update {
            it.copy(
                puzzleResuelto = false,
                esJaqueMate = false,
                mensajeFinal = null,
                esJuegoBloqueado = false,
                selectedPosition = null,
                validMoves = emptyList()
            )
        }
        cambiarNivel(1)
    }

    private fun estaElReyEnJaque(colorDelRey: PieceColor, piezasTablero: List<ChessPiece>, size: Int): Boolean {
        val rey = piezasTablero.find { it.type == PieceType.REY && it.color == colorDelRey } ?: return false
        val colorOponente = if (colorDelRey == PieceColor.ORO) PieceColor.PLATA else PieceColor.ORO
        return MoveValidator.esCasillaAmenazadaPorGeometria(rey.position, colorOponente, piezasTablero, size)
    }

    private fun verificarSiEsJaqueMate(colorTurno: PieceColor, piezasTablero: List<ChessPiece>, size: Int): Boolean {
        // 1. Verificación rápida de estado
        if (!estaElReyEnJaque(colorTurno, piezasTablero, size)) return false

        // 2. Obtener todas las piezas aliadas
        val piezasAliadas = piezasTablero.filter { it.color == colorTurno }

        // 3. Verificar si existe AL MENOS un movimiento legal que salve al rey
        for (pieza in piezasAliadas) {
            val movimientosPosibles = MoveValidator.obtenerMovimientosValidos(pieza, piezasTablero, size)

            for (destino in movimientosPosibles) {
                // Creamos una simulación limpia eliminando la pieza en su posición original
                // y colocando la nueva (manejando capturas implícitamente por el filterNot)
                val simulacion = piezasTablero
                    .filterNot { it.position == pieza.position || it.position == destino }
                    .plus(pieza.copy(position = destino))

                // Si después de este movimiento el rey NO está en jaque, NO es mate
                if (!estaElReyEnJaque(colorTurno, simulacion, size)) {
                    return false
                }
            }
        }

        // Si recorrimos todo y no encontramos escape, es Jaque Mate
        return true
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

    fun cargarModo(size: Int, modo: GameMode, puzzle: ChessPuzzle?, nivel: Int = 0) { // nivel por defecto 0
        // Si es modo LIBRE, ignoramos cualquier nivel guardado
        val piezasNuevas = if (modo == GameMode.LIBRE) {
            if (size == 8) generarSetupEstandar8x8() else NivelRepository.generarSetupPorDefecto(size)
        } else {
            generarPiezasParaModo(size, nivel)
        }

        _gameState.update { currentState ->
            currentState.copy(
                pieces = piezasNuevas,
                boardSize = size,
                modoJuego = modo,
                currentPuzzle = puzzle,
                nivelActualInt = nivel,
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

    fun finalizarYGuardarNivel() {
        // 1. Generar el código en el Logcat (para que lo tengas de respaldo)
        generarCodigoDeNivel()

        // 2. Crear configuración
        val currentState = _gameState.value
        val nuevoId = currentState.nivelActualInt + 1

        val nuevoNivelConfig = NivelConfig(
            id = nuevoId,
            size = currentState.boardSize,
            piezas = currentState.pieces,
            turnoInicial = PieceColor.ORO
        )

        // 3. Guardar en el Repositorio
        NivelRepository.guardarNivel(nuevoNivelConfig)
        Log.d("JASC_DEBUG", "Nivel $nuevoId guardado en memoria.")

        // 4. Limpieza segura
        _gameState.update { it.copy(isEditingMode = false) }
        // modoEdicion = false // Asegúrate de que esto sea una variable observada si es necesario

        // 5. Salto seguro: Solo intentamos cambiar si sabemos que existe
        val configVerificada = NivelRepository.obtenerNivel(nuevoId)
        if (configVerificada != null) {
            cambiarNivel(nuevoId)
        } else {
            Log.e("JASC_DEBUG", "Error: El nivel $nuevoId no se pudo cargar tras guardarlo.")
            // Si falla, volvemos al nivel anterior para no dejar el tablero en blanco
            cambiarNivel(currentState.nivelActualInt)
        }
    }

    // Asegúrate de que esta función tenga Log.e para que resalte en la consola
    fun generarCodigoDeNivel() {
        val piezas = _gameState.value.pieces
        val sb = StringBuilder()
        var oroCount = 0
        var plataCount = 0

        sb.append("NivelConfig(piezas = listOf(\n")
        piezas.forEach { p ->
            val colorSufijo = if (p.color == PieceColor.ORO) "oro" else "plata"
            val tipoSufijo = p.type.name.lowercase()
            val count = if (p.color == PieceColor.ORO) ++oroCount else ++plataCount
            val idLimpio = "n${count}_${tipoSufijo}_${colorSufijo}"
            sb.append("    ChessPiece(\"$idLimpio\", PieceType.${p.type}, PieceColor.${p.color}, Position(${p.position.row}, ${p.position.col})),\n")
        }
        sb.append("), turnoInicial = PieceColor.ORO)")

        // USAMOS Log.e para que aparezca en ROJO en la consola y no lo pierdas
        Log.e("JASC_GENERADOR", sb.toString())
    }

}