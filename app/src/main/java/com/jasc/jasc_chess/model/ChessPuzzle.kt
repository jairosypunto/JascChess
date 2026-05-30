package com.jasc.jasc_chess.model

data class ChessPuzzle(
    val id: Int,
    val fen: String,
    val requiredMoves: List<Move>,
    val solution: List<Move>,
    val enemyMoves: List<Move>,
    val description: String,
    val is4x4: Boolean
)