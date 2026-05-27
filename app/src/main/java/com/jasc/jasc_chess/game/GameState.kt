package com.jasc.jasc_chess.game

// Asegúrate de importar tus modelos correctamente
import com.jasc.jasc_chess.model.* data class GameState(
    // --- ESTADO DEL PUZZLE Y MODO ---
    val currentPuzzle: ChessPuzzle? = null,
    val modoJuego: GameMode = GameMode.LIBRE,
    val boardSize: Int = 8, // Campo necesario para lógica 4x4
    val movimientosRestantes: Int = 0,

    // --- ESTADO VISUAL ---
    val pieces: List<ChessPiece> = emptyList(),
    val selectedPosition: Position? = null,
    val fichaInspeccionada: Position? = null,
    val validMoves: List<Position> = emptyList(),
    val temaActual: Int = 0,
    val estiloSeleccionado: EstiloFichas = EstiloFichas.TRADICIONAL,
    val casillaPista: Position? = null,

    // --- ESTADO DE LA PARTIDA ---
    val currentTurn: PieceColor = PieceColor.ORO,
    val partidaIniciada: Boolean = false,
    val haMovidoRey: Boolean = false,
    val historialTableros: List<List<ChessPiece>> = emptyList(),

    // --- TIEMPOS ---
    val oroTimeMillis: Long = 600000L,
    val plataTimeMillis: Long = 600000L,

    // --- ESTADO DE VICTORIA/EMPATE ---
    val piezasComidasOro: List<ChessPiece> = emptyList(),
    val piezasComidasPlata: List<ChessPiece> = emptyList(),
    val esJaque: Boolean = false,
    val esJaqueMate: Boolean = false,
    val esTablas: Boolean = false,
    val esAhogado: Boolean = false,
    val ganador: PieceColor? = null,

    // --- CONFIGURACIÓN ---
    val nivelActual: NivelDificultad = NivelDificultad.PRINCIPIANTE
)