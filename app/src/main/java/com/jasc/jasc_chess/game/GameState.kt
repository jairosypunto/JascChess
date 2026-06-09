package com.jasc.jasc_chess.game

import com.jasc.jasc_chess.model.*

data class GameState(
    // --- NUEVO ESTADO DE EDICIÓN ---
    val isEditingMode: Boolean = false, // <--- AÑADE ESTA LÍNEA AQUÍ
    // --- ESTADO DEL PUZZLE Y MODO ---
    val lastUpdate: Long = 0L, // Añade esta línea
    val currentPuzzle: ChessPuzzle? = null,
    val modoJuego: GameMode = GameMode.LIBRE,
    val boardSize: Int = 8,
    val puzzleStepIndex: Int = 0, // <--- AÑADE ESTA LÍNEA
    val nivelActualInt: Int = 1, // Esto es vital

    // --- ESTADO DE JUEGO ---
    val pieces: List<ChessPiece> = emptyList(),
    val currentTurn: PieceColor = PieceColor.ORO,
    val isPlayerTurn: Boolean = true, // Controla si es turno del usuario o de la IA
    val partidaIniciada: Boolean = false,
    val haMovidoRey: Boolean = false,
    val torreOroIzqMovida: Boolean = false,
    val torreOroDerMovida: Boolean = false,
    val torrePlataIzqMovida: Boolean = false,
    val torrePlataDerMovida: Boolean = false,
    val posicionReyDerrotado: Position? = null,

    // --- ESTADO VISUAL E INTERACCIÓN ---
    val selectedPosition: Position? = null,
    val fichaInspeccionada: Position? = null,
// Asegúrate de que esto sea List<Move> y no otra cosa
// Asegúrate de que esta línea esté así:
    val validMoves: List<Move> = emptyList<Move>(),
    val casillaPista: Position? = null,
    val temaActual: Int = 0,
    val estiloSeleccionado: EstiloFichas = EstiloFichas.TRADICIONAL,

    // --- TIEMPOS Y HISTORIAL ---
    val modoTiempoActivado: Boolean = false,
    val oroTimeMillis: Long = 600000L,
    val plataTimeMillis: Long = 600000L,
    val historialTableros: List<List<ChessPiece>> = emptyList(),

    // --- ESTADO DE VICTORIA, DERROTA Y PUZZLES ---
    val piezasComidasOro: List<ChessPiece> = emptyList(),
    val piezasComidasPlata: List<ChessPiece> = emptyList(),
    val esJaque: Boolean = false,
    val esJaqueMate: Boolean = false,
    val esTablas: Boolean = false,
    val esAhogado: Boolean = false,
    val ganador: PieceColor? = null,
    val puzzleResuelto: Boolean = false, // Útil para mostrar la pantalla de "¡Bien hecho!"
    val mensajeFinal: String? = null,
    val esJuegoBloqueado: Boolean = false,
    // --- CONFIGURACIÓN ---
    val nivelActual: NivelDificultad = NivelDificultad.PRINCIPIANTE
)