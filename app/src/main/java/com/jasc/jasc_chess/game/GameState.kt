package com.jasc.jasc_chess.game

import com.jasc.jasc_chess.model.*

data class GameState(
    // --- MODO Y NIVEL ---
    val modoJuego: GameMode = GameMode.LIBRE,
    val nivelActualInt: Int = 1,
    val nivelActual: NivelDificultad = NivelDificultad.PRINCIPIANTE,
    val esModoProgresivo: Boolean = false,
    val isEditingMode: Boolean = false,
    val estaCargandoNivel: Boolean = false,

    // --- TABLERO Y PIEZAS ---
    val boardSize: Int = 8,
    val pieces: List<ChessPiece> = emptyList(),
    val currentTurn: PieceColor = PieceColor.ORO,
    val isPlayerTurn: Boolean = true,
    val selectedPosition: Position? = null,
    val validMoves: List<Move> = emptyList(),
    val casillaPista: Position? = null,
    val fichaInspeccionada: Position? = null,
    val posicionReyDerrotado: Position? = null,
    val historialTableros: List<List<ChessPiece>> = emptyList(),

    // --- ESTADO DE JUEGO (REGLAS Y MOVIMIENTOS) ---
    val partidaIniciada: Boolean = false,
    val haMovidoRey: Boolean = false,
    val torreOroIzqMovida: Boolean = false,
    val torreOroDerMovida: Boolean = false,
    val torrePlataIzqMovida: Boolean = false,
    val torrePlataDerMovida: Boolean = false,
    val puzzleStepIndex: Int = 0,
    val currentPuzzle: ChessPuzzle? = null,

    // --- ESTADO DE VICTORIA, DERROTA Y RESULTADOS ---
    val esJaque: Boolean = false,
    val esJaqueMate: Boolean = false,
    val esAhogado: Boolean = false,
    val esTablas: Boolean = false,
    val ganador: PieceColor? = null,
    val victoriaMostrada: Boolean = false,
    val puzzleResuelto: Boolean = false,
    val mensajeFinal: String? = null,
    val esJuegoBloqueado: Boolean = false,
    val piezasComidasOro: List<ChessPiece> = emptyList(),
    val piezasComidasPlata: List<ChessPiece> = emptyList(),

    // --- PISTA, ACERTIJOS Y EDICIÓN (CAMPOS REQUERIDOS POR NIVELCONFIG) ---
    val pistaBloqueada: Boolean = false,
    val acertijoActual: String = "", // Cambiado a String vacío por defecto
    val respuestaActual: String = "", // Campo necesario para NivelConfig
    val maxPasosConfigurado: Int = 4, // Campo necesario para NivelConfig
    val solucionTemporal: List<MovimientoSolucion> = emptyList(), // Campo necesario para NivelConfig

    // --- TIEMPO Y PROGRESO ---
    val modoTiempoActivado: Boolean = false,
    val oroTimeMillis: Long = 600000L,
    val plataTimeMillis: Long = 600000L,
    val puntosTotales: Int = 0,
    val lastUpdate: Long = 0L,

    // --- UI, TEMAS Y EVENTOS ---
    val temaActual: Int = 0,
    val estiloSeleccionado: EstiloFichas = EstiloFichas.TRADICIONAL,
    val videoEventoPendiente: Int? = null,
    val mensajeError: String? = null,
    val dialogoAcertijoVisible: Boolean = false,
    val codigoGeneradoVisible: String? = null
)