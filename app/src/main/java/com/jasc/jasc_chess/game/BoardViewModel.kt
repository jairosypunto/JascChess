package com.jasc.jasc_chess.game

import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jasc.jasc_chess.R
import com.jasc.jasc_chess.audio.SoundManager
import com.jasc.jasc_chess.data.engine.*
import com.jasc.jasc_chess.data.local.*
import com.jasc.jasc_chess.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
class BoardViewModel : ViewModel() {
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    // --- CORRECCIÓN EN EL INICIALIZADOR ---
    init {
        // Solo configuramos el 8x8 si el estado aún no tiene piezas (es decir, está vacío al iniciar)
        if (_gameState.value.pieces.isEmpty()) {
            configurarPartida(8, GameMode.LIBRE)
        }
        iniciarTemporizadorReloj()
    }

    // 1. Esta función es la que llamas desde el SelectorNivelesScreen
    fun cargarPartida(nivelId: Int) {
        indicePistaActual = 0
        val config = NivelRepository.totalNiveles[nivelId]

        if (config != null) {
            configurarPartida(config.size, GameMode.PUZZLE)

            _gameState.update { it.copy(
                pieces = config.piezas,
                nivelActualInt = nivelId,
                currentTurn = config.turnoInicial,
                boardSize = config.size,

                // --- AQUÍ ESTÁ LA CORRECCIÓN ---
                // Leemos los pasos reales del repositorio o ponemos 4 si es nulo
                maxPasosConfigurado = config.maxPasos,

                mensajeError = null,
                esJuegoBloqueado = false,
                puzzleStepIndex = 0
            )}
        }
    }

    fun activarModoEdicion() {
        vaciarTablero()
        // Activamos modo edición dentro del flujo de estado
        _gameState.update { it.copy(isEditingMode = true) }
    }
    private var indicePistaActual = 0

    fun cerrarDialogoAcertijo() {
        _gameState.update {
            it.copy(
                dialogoAcertijoVisible = false, // Solo ocultamos la ventana
                mensajeError = null
            )
        }
    }

    fun obtenerPistaAyuda() {
        val nivelActual = NivelRepository.obtenerNivel(_gameState.value.nivelActualInt) ?: return

        // 1. Si ya hay bloqueo activo, solo mostramos el diálogo
        if (_gameState.value.pistaBloqueada) {
            _gameState.update { it.copy(dialogoAcertijoVisible = true) }
            return
        }

        // 2. Si NO hay bloqueo, verificamos si aún hay pistas disponibles
        if (indicePistaActual < nivelActual.secuenciaSolucion.size) {

            // CORRECCIÓN AQUÍ:
            // No pintamos la pista todavía. Primero activamos el bloqueo y el diálogo.
            // El usuario DEBE validar el acertijo para que la pista se pinte.
            _gameState.update {
                it.copy(
                    pistaBloqueada = true,
                    dialogoAcertijoVisible = true,
                    acertijoActual = nivelActual.acertijo
                )
            }
        }
    }

    fun verificarRespuestaAcertijo(respuesta: String) {
        val nivelActual = NivelRepository.obtenerNivel(_gameState.value.nivelActualInt) ?: return

        if (respuesta.trim().equals(nivelActual.respuestaAcertijo, ignoreCase = true)) {
            // Obtenemos la pista que estaba pendiente
            val movimiento = nivelActual.secuenciaSolucion[indicePistaActual]
            indicePistaActual++ // Avanzamos el índice aquí, al acertar

            _gameState.update {
                it.copy(
                    pistaBloqueada = false,
                    dialogoAcertijoVisible = false,
                    casillaPista = movimiento.desde, // PINTA LA PISTA AQUÍ
                    mensajeError = null
                )
            }
        } else {
            _gameState.update { it.copy(mensajeError = "Incorrecto, intenta de nuevo.") }
        }
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
        // Generamos un ID legible: tipo_color_fila_columna
        // Ejemplo: "peon_oro_2_0"
        val tipo = seleccion.first.name.lowercase()
        val color = seleccion.second.name.lowercase()
        val idLimpio = "${tipo}_${color}_${pos.row}_${pos.col}"

        return ChessPiece(
            id = idLimpio, // Usamos el ID limpio sin currentTimeMillis()
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

    // --- En tu BoardViewModel ---
    fun reiniciarPartida() {
        val state = _gameState.value
        val config = NivelRepository.obtenerNivel(state.nivelActualInt)

        _gameState.update { it.copy(
            pieces = config?.piezas ?: emptyList(),
            puzzleStepIndex = 0,
            esJuegoBloqueado = false,
            mensajeError = null,

            // --- ESTO ES LO QUE TE FALTA ---
            // Al poner esto en false, la condición del 'if' en la UI falla
            // y el AlertDialog se destruye automáticamente.
            esAhogado = false,
            esTablas = false,
            esJaqueMate = false,
            // -------------------------------

            currentTurn = config?.turnoInicial ?: PieceColor.ORO,
            selectedPosition = null,
            validMoves = emptyList()
        )}
        Log.d("JASC_REINICIO", "Nivel ${state.nivelActualInt} reiniciado correctamente.")
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
        val config = NivelRepository.obtenerNivel(n)

        if (config == null) {
            Log.e("JASC_ERROR", "Intento de cargar nivel $n que no existe en el repositorio.")
            return
        }

        _gameState.update { currentState ->
            currentState.copy(
                nivelActualInt = n,
                boardSize = config.size,
                pieces = generarPiezasParaModo(config.size, n),
                modoJuego = GameMode.PUZZLE,
                currentTurn = config.turnoInicial,
                esJaque = false,
                esJaqueMate = false,
                estaCargandoNivel = true,
                esJuegoBloqueado = false,
                puzzleStepIndex = 0,
                selectedPosition = null,
                validMoves = emptyList()
            )
        }

        viewModelScope.launch {
            delay(500)
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

        // SI ESTÁS EN MODO LIBRE: No avances niveles, solo muestra la victoria
        if (estadoActual.modoJuego == GameMode.LIBRE) {
            _gameState.update { it.copy(
                puzzleResuelto = true,
                mensajeFinal = "¡Jaque Mate! ¡Excelente jugada!"
            )}
            return // Salimos aquí para que no intente avanzar niveles
        }

        // SI ESTÁS EN MODO PUZZLE:
        val nivelActual = estadoActual.nivelActualInt
        val siguienteNivel = nivelActual + 1
        val totalNiveles = NivelRepository.totalNiveles.size

        if (NivelRepository.totalNiveles.containsKey(siguienteNivel)) {
            val mensaje = when (siguienteNivel) {
                in 1..10 -> "¡Nivel $nivelActual completado! Vamos por el $siguienteNivel."
                in 11..20 -> "¡Nivel $nivelActual superado! ¡Bien hecho!"
                in 21..30 -> "¡Impresionante! Ya tienes una lógica excelente."
                in 31..40 -> "¡Brillante! Tu estrategia está alcanzando nivel maestro."
                in 41..50 -> "¡Asombroso! Tus jugadas son dignas de un campeón."
                in 51..60 -> "¡Épico! Has dominado cada desafío con precisión y mente aguda."
                else -> "🏆 ¡FELICIDADES! Has completado todos los niveles. Eres un Gran Maestro del ajedrez."
            }


            PreferencesManager.guardarNivelMaximo(siguienteNivel, context)
            _gameState.update { it.copy(mensajeFinal = mensaje) }

            viewModelScope.launch {
                delay(1500)
                cambiarNivel(siguienteNivel)
            }
        } else {
            _gameState.update {
                it.copy(
                    puzzleResuelto = true,
                    mensajeFinal = "🏆 ¡Has completado todos los niveles! Eres un Gran Maestro del ajedrez.",
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

// --- BLOQUE CORREGIDO: VALIDACIÓN Y DESPLAZAMIENTO PROFESIONAL ---

    fun validarJugadaPuzzle(origen: Position, destino: Position) {
        val state = _gameState.value
        val piezaMoviendose = state.pieces.find { it.position == origen } ?: return
        val nivelActual = NivelRepository.obtenerNivel(state.nivelActualInt) ?: return

        // 1. VALIDACIÓN SILENCIOSA
        if (!MoveValidator.esMovimientoValido(piezaMoviendose, destino, state.pieces, state.boardSize)) {
            _gameState.update { it.copy(selectedPosition = null, validMoves = emptyList()) }
            return
        }

        // 2. EJECUCIÓN SEGURA
        val piezasDespues = realizarDesplazamiento(state.pieces, origen, destino)
        val nuevoStep = state.puzzleStepIndex + 1

        _gameState.update {
            it.copy(
                pieces = piezasDespues,
                puzzleStepIndex = nuevoStep,
                currentTurn = PieceColor.PLATA,
                selectedPosition = null,
                validMoves = emptyList(),
                mensajeError = null
            )
        }

        // 3. EVALUACIÓN DE ESTADO
        evaluarEstadoFinal(piezasDespues, PieceColor.PLATA)
        val estadoPost = _gameState.value

        if (estadoPost.esJaqueMate) {
            _gameState.update { it.copy(puzzleResuelto = true, mensajeFinal = "¡Nivel ${state.nivelActualInt} Superado!") }
        } else if (nuevoStep >= nivelActual.maxPasos) {
            // --- AQUÍ ESTÁ EL CAMBIO ---
            // 1. Primero actualizamos el mensaje para que la UI lo pinte
            _gameState.update { it.copy(mensajeError = "¡Agotaste los ${nivelActual.maxPasos} intentos! Reiniciando...") }

            // 2. Luego lanzamos el reinicio con espera
            viewModelScope.launch {
                delay(2000) // 2 segundos para que el jugador vea el mensaje
                reiniciarPartida()
            }
        } else {
            viewModelScope.launch {
                delay(600)
                if (state.currentPuzzle != null) ejecutarRespuestaIA(state.currentPuzzle)
                else ejecutarTurnoIA()
            }
        }
    }

    fun realizarDesplazamiento(piezas: List<ChessPiece>, origen: Position, destino: Position): List<ChessPiece> {
        val nuevasPiezas = piezas.toMutableList()
        val piezaMovida = nuevasPiezas.find { it.position == origen } ?: return piezas
        val piezaCapturada = nuevasPiezas.find { it.position == destino }

        // 4. BLINDAJE: Impedir captura del Rey en cualquier circunstancia
        if (piezaCapturada?.type == PieceType.REY) {
            Log.e("SEGURIDAD", "Captura de REY detectada y bloqueada.")
            return piezas
        }

        // 5. Gestión de capturas y cementerio
        if (piezaCapturada != null) {
            _gameState.update { state ->
                if (piezaCapturada.color == PieceColor.ORO)
                    state.copy(piezasComidasOro = state.piezasComidasOro + piezaCapturada)
                else
                    state.copy(piezasComidasPlata = state.piezasComidasPlata + piezaCapturada)
            }
            nuevasPiezas.remove(piezaCapturada)
        }

        // 6. Mover pieza
        val idx = nuevasPiezas.indexOf(piezaMovida)
        if (idx != -1) nuevasPiezas[idx] = piezaMovida.copy(position = destino)

        _gameState.update { it.copy(casillaPista = null) }
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

    // 1. EJECUCIÓN DE RESPUESTA IA (ESPECÍFICA PARA PUZZLES)
    private fun ejecutarRespuestaIA(puzzle: ChessPuzzle) {
        viewModelScope.launch {
            delay(600)

            // Obtenemos el índice actual.
            // Si el usuario acaba de mover y el step subió, el turno de la IA corresponde al index actual.
            val stepActual = _gameState.value.puzzleStepIndex
            val respuestaIA = puzzle.enemyMoves.getOrNull(stepActual)

            // Si el puzzle no tiene respuesta definida para este paso, la IA pasa el turno al usuario.
            if (respuestaIA == null) {
                _gameState.update { it.copy(currentTurn = PieceColor.ORO) }
                return@launch
            }

            val piezaIA = _gameState.value.pieces.find { it.position == respuestaIA.from }
            val esValido = piezaIA != null && MoveValidator.esMovimientoValido(
                piezaIA, respuestaIA.to, _gameState.value.pieces, _gameState.value.boardSize
            )

            if (esValido) {
                val piezasActualizadas = realizarDesplazamiento(_gameState.value.pieces, respuestaIA.from, respuestaIA.to)

                // Incrementamos el índice de paso aquí también para que el contador avance
                _gameState.update { it.copy(
                    pieces = piezasActualizadas,
                    puzzleStepIndex = it.puzzleStepIndex + 1,
                    currentTurn = PieceColor.ORO
                )}
                evaluarEstadoFinal(piezasActualizadas, PieceColor.ORO)
            } else {
                // Si la jugada de la IA definida en el puzzle falla, devolvemos el turno al usuario
                _gameState.update { it.copy(currentTurn = PieceColor.ORO) }
            }
        }
    }

    private fun evaluarEstadoFinal(piezas: List<ChessPiece>, colorTurno: PieceColor) {
        val currentState = _gameState.value
        if (currentState.estaCargandoNivel) return

        val size = currentState.boardSize

        // Cálculos necesarios antes del update
        val enJaque = estaElReyEnJaque(colorTurno, piezas, size)
        val esMate = enJaque && verificarSiEsJaqueMate(colorTurno, piezas, size)
        val esAhogado = !enJaque && esAhogado(colorTurno, piezas, size)
        val esTablasMaterial = verificarTablasPorMaterial(piezas)

        val reyDerrotado = if (esMate) piezas.find { it.type == PieceType.REY && it.color == colorTurno } else null

        // Actualización de estado
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
            SoundManager.play(R.raw.victoria)
            procesarFinDeNivel(currentState.nivelActualInt)

            // Avance automático
            if (currentState.modoJuego != GameMode.LIBRE) {
                viewModelScope.launch {
                    delay(2000)
                    // Se utiliza una referencia al contexto guardado o se pasa por parámetro
                    // Si este método no tiene acceso a context, debe llamarse desde la UI
                }
            }
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
        val puntosActuales = _gameState.value.puntosTotales
        val nuevosPuntos = puntosActuales + puntosPorNivel

        PreferencesManager.guardarPuntos(puntosPorNivel, context)

        // Lógica dinámica para seleccionar el video según el nivel
        val esNivelEspecial = (nivel % 10 == 0)

        // Asignamos el recurso dinámicamente usando un 'when'
        val videoAsignado = when (nivel) {
            10 -> R.raw.video_felicitacion_10
            20 -> R.raw.video_felicitacion_20
            30 -> R.raw.video_felicitacion_30 // <--- Aquí ya cargará el del 30
            40 -> R.raw.video_felicitacion_40
            50 -> R.raw.video_felicitacion_50
            60 -> R.raw.video_felicitacion_60
            else -> null
        }

        _gameState.update {
            it.copy(
                puntosTotales = nuevosPuntos,
                videoEventoPendiente = videoAsignado // Usamos la variable dinámica
            )
        }

        if (!esNivelEspecial) {
            val mensaje = obtenerMensajeDeRecompensa(nuevosPuntos)
            if (mensaje != null) {
                _gameState.update { it.copy(mensajeError = mensaje) }
                viewModelScope.launch {
                    delay(3000)
                    _gameState.update { it.copy(mensajeError = null) }
                }
            }
        }
    }


    fun guardarProgresoFinal(context: Context) {
        PreferencesManager.guardarPuntos(puntosAcumuladosEnSesion, context)
        puntosAcumuladosEnSesion = 0 // Reiniciamos el contador de sesión
    }

    // En BoardViewModel.kt
    fun limpiarVideoEvento() {
        _gameState.update { it.copy(videoEventoPendiente = null) }
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

    fun iniciarModoPractica(context: Context, esContinuo: Boolean = true) {
        val nivelGuardado = PreferencesManager.obtenerNivelMaximo(context)
        val nivelAIniciar = if (nivelGuardado > 0) nivelGuardado else 1

        val config = NivelRepository.obtenerNivel(nivelAIniciar)

        if (config != null) {
            Log.d("DEBUG_JASC", "Cargando Nivel: $nivelAIniciar, Tamaño: ${config.size}")

            _gameState.update { it.copy(
                boardSize = config.size, // <--- Esto fuerza el 4x4 o el tamaño del nivel
                pieces = config.piezas,
                modoJuego = GameMode.PUZZLE, // Asegúrate de que este sea el modo correcto
                nivelActualInt = nivelAIniciar,
                esModoProgresivo = esContinuo,
                // LIMPIEZA: Aseguramos que no queden rastros de modo libre
                piezasComidasOro = emptyList(),
                piezasComidasPlata = emptyList(),
                historialTableros = listOf(config.piezas)
            )}
        } else {
            Log.e("DEBUG_JASC", "El nivel $nivelAIniciar no existe en NivelRepository")
        }
    }
    fun cargarModo(size: Int, modo: GameMode, puzzle: ChessPuzzle?, nivel: Int = 0) {
        // CORRECCIÓN: Accedemos al mapa totalNiveles directamente
        val config = if (modo != GameMode.LIBRE) NivelRepository.totalNiveles[nivel] else null
        android.util.Log.d("DEBUG_JASC", "Nivel recibido: $nivel | Config encontrada: ${config != null} | maxPasos en repo: ${config?.maxPasos}")
        val piezasNuevas = if (modo == GameMode.LIBRE) {
            if (size == 8) generarSetupEstandar8x8() else NivelRepository.generarSetupPorDefecto(size)
        } else {
            // Si el nivel no existe en el mapa, usamos un fallback seguro
            config?.piezas ?: generarPiezasParaModo(size, nivel)
        }

        _gameState.update { currentState ->
            currentState.copy(
                pieces = piezasNuevas,
                boardSize = size,
                modoJuego = modo,
                currentPuzzle = puzzle,
                nivelActualInt = nivel,

                // ASIGNACIÓN CORRECTA
                maxPasosConfigurado = config?.maxPasos ?: 4,
                puzzleStepIndex = 0,

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

    // 1. Esta función ahora calcula el ID de forma independiente
    fun generarCodigoDeNivel(): NivelConfig {
        val currentState = _gameState.value
        val nuevoId = currentState.nivelActualInt + 1

        return NivelConfig(
            id = nuevoId,
            size = currentState.boardSize,
            piezas = currentState.pieces,
            turnoInicial = PieceColor.ORO,
            secuenciaSolucion = currentState.solucionTemporal.map { mov ->
                com.jasc.jasc_chess.model.MovimientoSolucion(mov.desde, mov.hacia)
            },
            acertijo = currentState.acertijoActual.ifBlank { "Sin acertijo" },
            respuestaAcertijo = currentState.respuestaActual.ifBlank { "N/A" },
            // Corregido: 'maxPasos' se define una sola vez aquí abajo
            maxPasos = currentState.maxPasosConfigurado.coerceAtLeast(1)
        )
    }
    fun limpiarCodigoGenerado() {
        _gameState.update { it.copy(codigoGeneradoVisible = null) }
    }
    fun finalizarYGuardarNivel() {
        val config = generarCodigoDeNivel()
        NivelRepository.guardarNivel(config)

        // ... (tu lógica de formateo que ya tienes) ...
        val piezasStr = config.piezas.joinToString(",\n    ") { pieza ->
            "ChessPiece(\"${pieza.id}\", PieceType.${pieza.type}, PieceColor.${pieza.color}, Position(${pieza.position.row}, ${pieza.position.col}))"
        }

        val solucionesStr = config.secuenciaSolucion.joinToString(", ") { mov ->
            "MovimientoSolucion(Position(${mov.desde.row}, ${mov.desde.col}), Position(${mov.hacia.row}, ${mov.hacia.col}))"
        }

        val codigoFormateado = """
        ${config.id} to NivelConfig(id = ${config.id}, size = ${config.size}, piezas = listOf(
            $piezasStr
        ), turnoInicial = PieceColor.${config.turnoInicial},
        secuenciaSolucion = listOf($solucionesStr),
        acertijo = "${config.acertijo}", respuestaAcertijo = "${config.respuestaAcertijo}", maxPasos = ${config.maxPasos})
    """.trimIndent()

        Log.d("JASC_DEBUG", "Nivel guardado. Código:\n$codigoFormateado")

        // ACTUALIZACIÓN AQUÍ:
        // 1. isEditingMode = false (cierra el panel)
        // 2. partidaIniciada = true (prepara el tablero para jugar)
        _gameState.update { currentState ->
            currentState.copy(
                isEditingMode = false,
                partidaIniciada = true,
                codigoGeneradoVisible = codigoFormateado
            )
        }
    }

    fun actualizarMaxPasos(nuevosPasos: Int) {
        _gameState.update { it.copy(maxPasosConfigurado = nuevosPasos) }
    }
}