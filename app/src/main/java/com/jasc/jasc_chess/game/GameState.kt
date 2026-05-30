package com.jasc.jasc_chess.game

import com.jasc.jasc_chess.model.*

data class GameState(
    // --- ESTADO DEL PUZZLE Y MODO ---
    val currentPuzzle: ChessPuzzle? = null,
    val modoJuego: GameMode = GameMode.LIBRE,
    val boardSize: Int = 8,
    val puzzleStepIndex: Int = 0, // <--- AÑADE ESTA LÍNEA

    // --- ESTADO DE JUEGO ---
    val pieces: List<ChessPiece> = emptyList(),
    val currentTurn: PieceColor = PieceColor.ORO,
    val isPlayerTurn: Boolean = true, // Controla si es turno del usuario o de la IA
    val partidaIniciada: Boolean = false,
    val haMovidoRey: Boolean = false,

    // --- ESTADO VISUAL E INTERACCIÓN ---
    val selectedPosition: Position? = null,
    val fichaInspeccionada: Position? = null,
    val validMoves: List<Position> = emptyList(),
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

    // --- CONFIGURACIÓN ---
    val nivelActual: NivelDificultad = NivelDificultad.PRINCIPIANTE
)