package com.jasc.jasc_chess.data.local

import com.jasc.jasc_chess.model.*

object NivelRepository {
val totalNiveles: Map<Int, List<ChessPiece>> = mapOf(
1 to listOf(
ChessPiece("n1_q", PieceType.REINA, PieceColor.PLATA, Position(1, 0)),
ChessPiece("n1_a", PieceType.ALFIL, PieceColor.PLATA, Position(1, 2)),
ChessPiece("n1_k", PieceType.REY, PieceColor.PLATA, Position(0, 2)),
ChessPiece("n1_p", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
ChessPiece("n1_t_oro", PieceType.TORRE, PieceColor.ORO, Position(1, 3)),
ChessPiece("n1_a_oro", PieceType.ALFIL, PieceColor.ORO, Position(3, 0)),
ChessPiece("n1_t_oro", PieceType.TORRE, PieceColor.ORO, Position(3, 3))
),
2 to listOf(
ChessPiece("n1_t1", PieceType.TORRE, PieceColor.PLATA, Position(0, 0)),
ChessPiece("n1_t2", PieceType.TORRE, PieceColor.PLATA, Position(0, 1)),
ChessPiece("n1_k", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
ChessPiece("n1_p", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
ChessPiece("n1_a", PieceType.ALFIL, PieceColor.PLATA, Position(2, 1)),
ChessPiece("n1_q_oro", PieceType.REINA, PieceColor.ORO, Position(2, 0)),
ChessPiece("n1_c_oro", PieceType.CABALLO, PieceColor.ORO, Position(3, 0)),
ChessPiece("n1_t_oro", PieceType.TORRE, PieceColor.ORO, Position(3, 2))
),
3 to listOf(
ChessPiece("n2_t_oro", PieceType.TORRE, PieceColor.ORO, Position(3, 1)),
ChessPiece("n1_q_oro", PieceType.REINA, PieceColor.ORO, Position(2, 0)),
ChessPiece("n2_k", PieceType.REY, PieceColor.PLATA, Position(0, 0)),
ChessPiece("n2_t", PieceType.TORRE, PieceColor.PLATA, Position(0, 1)),
ChessPiece("n1_p", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
ChessPiece("n2_t", PieceType.REINA, PieceColor.PLATA, Position(2, 2))
),
4 to listOf(
ChessPiece("n3_k", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
ChessPiece("n3_q_oro", PieceType.REINA, PieceColor.ORO, Position(3, 1)),
ChessPiece("n3_a_oro", PieceType.ALFIL, PieceColor.ORO, Position(2, 2))
)
)
}