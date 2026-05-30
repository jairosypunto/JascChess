package com.jasc.jasc_chess.data.local

import com.jasc.jasc_chess.model.ChessPuzzle
import com.jasc.jasc_chess.model.Move
import com.jasc.jasc_chess.model.Position

object PuzzleRepository {
    val levels = listOf(
        // --- PUZZLES 8x8 (IDs 1-20) ---
        ChessPuzzle(1, "6k1/5ppp/8/8/8/8/5PPP/R4RK1 w - - 0 1", listOf(Move(Position(7, 0), Position(0, 0))), emptyList(), "Doble Torre", false),
        ChessPuzzle(2, "8/8/8/8/3k4/5Q2/8/K7 w - - 0 1", listOf(Move(Position(5, 5), Position(4, 5))), emptyList(), "Dama y Rey", false),
        ChessPuzzle(3, "4k3/8/8/8/8/8/PPP5/4K3 w - - 0 1", listOf(Move(Position(6, 0), Position(4, 0))), emptyList(), "Avance Peón", false),
        ChessPuzzle(4, "r1bqkbnr/pppp1ppp/2n5/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R w - - 0 1", listOf(Move(Position(6, 4), Position(4, 4))), emptyList(), "Apertura Italiana", false),
        ChessPuzzle(5, "r1bqkb1r/pppp1ppp/2n2n2/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R w - - 0 1", listOf(Move(Position(5, 5), Position(2, 2))), emptyList(), "Defensa Berlinesa", false),
        ChessPuzzle(6, "r1bqkb1r/pppp1ppp/2n2n2/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R w - - 0 1", listOf(Move(Position(7, 6), Position(5, 5))), emptyList(), "Desarrollo Caballo", false),
        ChessPuzzle(7, "r1bqkb1r/pppp1ppp/2n2n2/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R w - - 0 1", listOf(Move(Position(6, 2), Position(4, 2))), emptyList(), "Centro Peón", false),
        ChessPuzzle(8, "r1bqkb1r/pppp1ppp/2n2n2/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R w - - 0 1", listOf(Move(Position(6, 3), Position(4, 3))), emptyList(), "Control Centro", false),
        ChessPuzzle(9, "r1bqkb1r/pppp1ppp/2n2n2/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R w - - 0 1", listOf(Move(Position(7, 1), Position(5, 2))), emptyList(), "Salida Caballo", false),
        ChessPuzzle(10, "r1bqkb1r/pppp1ppp/2n2n2/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R w - - 0 1", listOf(Move(Position(7, 5), Position(4, 2))), emptyList(), "Alfil Activo", false),
        ChessPuzzle(11, "r1bqkb1r/pppp1ppp/2n2n2/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R w - - 0 1", listOf(Move(Position(7, 4), Position(6, 6))), emptyList(), "Enroque Corto", false),
        ChessPuzzle(12, "8/8/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1", listOf(Move(Position(6, 4), Position(4, 4))), emptyList(), "Apertura Rey", false),
        ChessPuzzle(13, "8/8/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1", listOf(Move(Position(6, 3), Position(4, 3))), emptyList(), "Apertura Dama", false),
        ChessPuzzle(14, "8/8/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1", listOf(Move(Position(7, 6), Position(5, 5))), emptyList(), "Caballo F3", false),
        ChessPuzzle(15, "8/8/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1", listOf(Move(Position(6, 2), Position(4, 2))), emptyList(), "Peón C4", false),
        ChessPuzzle(16, "8/8/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1", listOf(Move(Position(7, 5), Position(4, 2))), emptyList(), "Alfil C4", false),
        ChessPuzzle(17, "8/8/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1", listOf(Move(Position(6, 6), Position(4, 6))), emptyList(), "Peón G3", false),
        ChessPuzzle(18, "8/8/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1", listOf(Move(Position(7, 1), Position(5, 2))), emptyList(), "Caballo C3", false),
        ChessPuzzle(19, "8/8/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1", listOf(Move(Position(6, 5), Position(4, 5))), emptyList(), "Peón F4", false),
        ChessPuzzle(20, "8/8/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1", listOf(Move(Position(6, 1), Position(4, 1))), emptyList(), "Peón B4", false),

        // --- PUZZLES 4x4 (IDs 101-120) ---
        ChessPuzzle(101, "k7/8/p7/KR6 w - - 0 1", listOf(Move(Position(3, 1), Position(3, 2))), listOf(Move(Position(2, 0), Position(2, 1))), "Mate 4x4 A", true),
        ChessPuzzle(102, "k7/8/p7/KR6 w - - 0 1", listOf(Move(Position(3, 1), Position(3, 2))), listOf(Move(Position(2, 0), Position(2, 1))), "Mate 4x4 B", true),
        ChessPuzzle(103, "k7/4K3/8/8 w - - 0 1", listOf(Move(Position(1, 4), Position(2, 4))), emptyList(), "Rey Cerca", true),
        ChessPuzzle(104, "k7/4K3/8/8 w - - 0 1", listOf(Move(Position(1, 4), Position(1, 3))), emptyList(), "Oposición", true),
        ChessPuzzle(105, "k7/4K3/8/8 w - - 0 1", listOf(Move(Position(1, 4), Position(0, 4))), emptyList(), "Final Básico", true),
        ChessPuzzle(106, "k7/4K3/8/8 w - - 0 1", listOf(Move(Position(1, 4), Position(2, 3))), emptyList(), "Cierre", true),
        ChessPuzzle(107, "k7/4K3/8/8 w - - 0 1", listOf(Move(Position(1, 4), Position(1, 5))), emptyList(), "Bloqueo", true),
        ChessPuzzle(108, "k7/4K3/8/8 w - - 0 1", listOf(Move(Position(1, 4), Position(0, 3))), emptyList(), "Estrategia", true),
        ChessPuzzle(109, "k7/4K3/8/8 w - - 0 1", listOf(Move(Position(1, 4), Position(0, 5))), emptyList(), "Final 4x4", true),
        ChessPuzzle(110, "k7/4K3/8/8 w - - 0 1", listOf(Move(Position(1, 4), Position(2, 5))), emptyList(), "Mate Rápido", true),
        ChessPuzzle(111, "k7/4K3/8/8 w - - 0 1", listOf(Move(Position(1, 4), Position(2, 4))), emptyList(), "Control 4x4", true),
        ChessPuzzle(112, "k7/4K3/8/8 w - - 0 1", listOf(Move(Position(1, 4), Position(1, 3))), emptyList(), "Rey Activo", true),
        ChessPuzzle(113, "k7/4K3/8/8 w - - 0 1", listOf(Move(Position(1, 4), Position(0, 4))), emptyList(), "Final 1", true),
        ChessPuzzle(114, "k7/4K3/8/8 w - - 0 1", listOf(Move(Position(1, 4), Position(2, 3))), emptyList(), "Final 2", true),
        ChessPuzzle(115, "k7/4K3/8/8 w - - 0 1", listOf(Move(Position(1, 4), Position(1, 5))), emptyList(), "Final 3", true),
        ChessPuzzle(116, "k7/4K3/8/8 w - - 0 1", listOf(Move(Position(1, 4), Position(0, 3))), emptyList(), "Final 4", true),
        ChessPuzzle(117, "k7/4K3/8/8 w - - 0 1", listOf(Move(Position(1, 4), Position(0, 5))), emptyList(), "Final 5", true),
        ChessPuzzle(118, "k7/4K3/8/8 w - - 0 1", listOf(Move(Position(1, 4), Position(2, 5))), emptyList(), "Final 6", true),
        ChessPuzzle(119, "k7/4K3/8/8 w - - 0 1", listOf(Move(Position(1, 4), Position(2, 4))), emptyList(), "Final 7", true),
        ChessPuzzle(120, "k7/4K3/8/8 w - - 0 1", listOf(Move(Position(1, 4), Position(1, 3))), emptyList(), "Final 8", true)
    )
}