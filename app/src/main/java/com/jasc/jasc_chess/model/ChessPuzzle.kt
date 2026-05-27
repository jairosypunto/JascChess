package com.jasc.jasc_chess.model

data class ChessPuzzle(
    val id: Int,
    val fen: String,
    val sequence: List<Move>, // El plan maestro que la IA seguirá
    val mateIn: Int,
    val description: String
)

object PuzzleRepository {
    val levels = listOf(
        // Nivel 1: Mate en 1 (Reina blanca ataca)
        ChessPuzzle(
            id = 1,
            fen = "8/8/8/5k2/5Q2/8/8/8 w - - 0 1",
            sequence = listOf(Move(Position(4, 5), Position(4, 7))),
            mateIn = 1,
            description = "Mate con Reina"
        ),
        // Nivel 2: Mate en 1 (Torre blanca en la esquina)
        ChessPuzzle(
            id = 2,
            fen = "7k/R7/8/8/8/8/8/8 w - - 0 1",
            sequence = listOf(Move(Position(1, 0), Position(7, 0))),
            mateIn = 1,
            description = "Mate de torre en última fila"
        ),
        // Nivel 43: Presión al centro (Tu ejemplo original)
        ChessPuzzle(
            id = 43,
            fen = "r3k2r/pppb1ppp/2nq1n2/4p3/4P3/2NQ1N2/PPPB1PPP/R3K2R w KQkq - 0 1",
            sequence = listOf(
                Move(Position(0, 3), Position(0, 2)),
                Move(Position(1, 2), Position(2, 2))
            ),
            mateIn = 3,
            description = "Presión al centro"
        )
    )
}