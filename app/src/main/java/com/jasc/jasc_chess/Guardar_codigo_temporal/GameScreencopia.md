    private fun generarPiezasParaModo(size: Int): List<ChessPiece> {
        val piezas = mutableListOf<ChessPiece>()
        if (size == 4) {
            // --- BLANCAS (ORO) ---
            // Torre en 0,0
            piezas.add(ChessPiece("t_oro_1", PieceType.TORRE, PieceColor.PLATA, Position(0, 0)))
            // Torre en 0,1
            piezas.add(ChessPiece("t_oro_2", PieceType.TORRE, PieceColor.PLATA, Position(0, 1)))
            // Rey en 0,3
            piezas.add(ChessPiece("k_oro", PieceType.REY, PieceColor.PLATA, Position(0, 3)))
            // Peón en 1,3
            piezas.add(ChessPiece("p_oro_1", PieceType.PEON, PieceColor.PLATA, Position(1, 3)))
            // Alfil en 2,1
            piezas.add(ChessPiece("a_oro_1", PieceType.ALFIL, PieceColor.PLATA, Position(2, 1)))

            // --- NEGRAS (PLATA) ---
            // Reina en 2,0
            piezas.add(ChessPiece("q_plata", PieceType.REINA, PieceColor.ORO, Position(2, 0)))
            // Caballo en 3,0
            piezas.add(ChessPiece("c_plata_1", PieceType.CABALLO, PieceColor.ORO, Position(3, 0)))
            // Torre en 3,2
            piezas.add(ChessPiece("t_plata_1", PieceType.TORRE, PieceColor.ORO, Position(3, 2)))

        } else {
            return setupInitialBoard()
        }
        return piezas
    }




package com.jasc.jasc_chess.data.local

import com.jasc.jasc_chess.model.*

object PuzzleRepository {
val levels = listOf(
// NIVEL 1: Inicio 4x4
ChessPuzzle(
id = 1,
description = "Inicio 4x4",
is4x4 = true,
piezas = listOf(
ChessPiece("t_oro_1", PieceType.TORRE, PieceColor.PLATA, Position(0, 0)),
ChessPiece("t_oro_2", PieceType.TORRE, PieceColor.PLATA, Position(0, 1)),
ChessPiece("k_oro", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
ChessPiece("p_oro_1", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
ChessPiece("a_oro_1", PieceType.ALFIL, PieceColor.PLATA, Position(2, 1)),
ChessPiece("q_plata", PieceType.REINA, PieceColor.ORO, Position(2, 0)),
ChessPiece("c_plata_1", PieceType.CABALLO, PieceColor.ORO, Position(3, 0)),
ChessPiece("t_plata_1", PieceType.TORRE, PieceColor.ORO, Position(3, 2))
)
),
// NIVEL 2: 4x4 Desafío 1
ChessPuzzle(
id = 2,
description = "Desafío 4x4 - Nivel 2",
is4x4 = true,
piezas = listOf(
ChessPiece("k_oro", PieceType.REY, PieceColor.ORO, Position(0, 0)),
ChessPiece("q_oro", PieceType.REINA, PieceColor.ORO, Position(0, 3)),
ChessPiece("k_plata", PieceType.REY, PieceColor.PLATA, Position(3, 3)),
ChessPiece("t_plata_1", PieceType.TORRE, PieceColor.PLATA, Position(3, 0))
)
),
// NIVEL 3: 4x4 Desafío 2
ChessPuzzle(
id = 3,
description = "Desafío 4x4 - Nivel 3",
is4x4 = true,
piezas = listOf(
ChessPiece("t_oro_1", PieceType.TORRE, PieceColor.ORO, Position(0, 0)),
ChessPiece("t_oro_2", PieceType.TORRE, PieceColor.ORO, Position(0, 3)),
ChessPiece("k_plata", PieceType.REY, PieceColor.PLATA, Position(3, 1))
)
),
// NIVEL 4: 8x8 Inicio
ChessPuzzle(
id = 4,
description = "Inicio 8x8",
is4x4 = false,
piezas = listOf(
ChessPiece("t_oro_1", PieceType.TORRE, PieceColor.ORO, Position(0, 0)),
ChessPiece("k_oro", PieceType.REY, PieceColor.ORO, Position(0, 4)),
ChessPiece("t_plata_1", PieceType.TORRE, PieceColor.PLATA, Position(7, 0)),
ChessPiece("k_plata", PieceType.REY, PieceColor.PLATA, Position(7, 4))
)
),
// NIVEL 5: 8x8 Jaque Mate avanzado
ChessPuzzle(
id = 5,
description = "Final de Partida 8x8",
is4x4 = false,
piezas = listOf(
ChessPiece("k_oro", PieceType.REY, PieceColor.ORO, Position(0, 7)),
ChessPiece("k_plata", PieceType.REY, PieceColor.PLATA, Position(2, 7)),
ChessPiece("q_oro", PieceType.REINA, PieceColor.ORO, Position(0, 5))
)
)
)
}