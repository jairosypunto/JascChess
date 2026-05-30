package com.jasc.jasc_chess.model


data class ChessPuzzle(
    val id: Int,
    val fen: String,
    val solution: List<Move>, // Lista de movimientos del JUGADOR
    val enemyMoves: List<Move>, // Lista de movimientos de la IA
    val description: String,
    val is4x4: Boolean
)