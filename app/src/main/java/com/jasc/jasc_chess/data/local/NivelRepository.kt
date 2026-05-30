package com.jasc.jasc_chess.data.local

import com.jasc.jasc_chess.model.*

// Clase mejorada para incluir el límite de jugadas sin perder el turno inicial
data class NivelConfig(
    val piezas: List<ChessPiece>,
    val turnoInicial: PieceColor,
    val jugadasMaximas: Int = 2 // <--- Límite estricto para la lógica de juego
)

object NivelRepository {
    val totalNiveles: Map<Int, NivelConfig> = mapOf(
        1 to NivelConfig(
            piezas = listOf(
                ChessPiece("n1_k", PieceType.REY, PieceColor.PLATA, Position(0, 0)),
                ChessPiece("n1_a", PieceType.ALFIL, PieceColor.PLATA, Position(0, 1)),
                ChessPiece("n1_p", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
                ChessPiece("n1_p", PieceType.PEON, PieceColor.PLATA, Position(3, 0)),

                ChessPiece("n1_q_oro", PieceType.REINA, PieceColor.ORO, Position(3, 1)),
                ChessPiece("n3_p_oro", PieceType.PEON, PieceColor.ORO, Position(2, 1)),
                ChessPiece("n3_a_oro", PieceType.ALFIL, PieceColor.ORO, Position(1, 3)),

            ),
            turnoInicial = PieceColor.ORO
        ),
        2 to NivelConfig(
            piezas = listOf(
                ChessPiece("n1_t1", PieceType.TORRE, PieceColor.PLATA, Position(0, 0)),
                ChessPiece("n1_t2", PieceType.TORRE, PieceColor.PLATA, Position(0, 1)),
                ChessPiece("n1_k", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
                ChessPiece("n1_p", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
                ChessPiece("n1_a", PieceType.ALFIL, PieceColor.PLATA, Position(2, 1)),
                ChessPiece("n1_q_oro", PieceType.REINA, PieceColor.ORO, Position(2, 0)),
                ChessPiece("n1_c_oro", PieceType.CABALLO, PieceColor.ORO, Position(3, 0)),
                ChessPiece("n1_t_oro", PieceType.TORRE, PieceColor.ORO, Position(3, 2))
            ),
            turnoInicial = PieceColor.ORO
        ),
        3 to NivelConfig(
            piezas = listOf(
                ChessPiece("n2_t_oro", PieceType.TORRE, PieceColor.ORO, Position(3, 1)),
                ChessPiece("n1_q_oro", PieceType.REINA, PieceColor.ORO, Position(2, 0)),
                ChessPiece("n2_k", PieceType.REY, PieceColor.PLATA, Position(0, 0)),
                ChessPiece("n2_t", PieceType.TORRE, PieceColor.PLATA, Position(0, 1)),
                ChessPiece("n1_p", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
                ChessPiece("n2_t", PieceType.REINA, PieceColor.PLATA, Position(2, 2))
            ),
            turnoInicial = PieceColor.ORO
        ),
        4 to NivelConfig(
            piezas = listOf(
                ChessPiece("n3_k", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
                ChessPiece("n3_q_oro", PieceType.REINA, PieceColor.ORO, Position(3, 1)),
                ChessPiece("n3_a_oro", PieceType.ALFIL, PieceColor.ORO, Position(2, 2))
            ),
            turnoInicial = PieceColor.ORO
        ),
        5 to NivelConfig(
            piezas = listOf(
                ChessPiece("n1_q", PieceType.REINA, PieceColor.PLATA, Position(1, 0)),
                ChessPiece("n1_a", PieceType.ALFIL, PieceColor.PLATA, Position(1, 2)),
                ChessPiece("n1_k", PieceType.REY, PieceColor.PLATA, Position(0, 2)),
                ChessPiece("n1_p", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
                ChessPiece("n1_t_oro", PieceType.TORRE, PieceColor.ORO, Position(1, 3)),
                ChessPiece("n1_a_oro", PieceType.ALFIL, PieceColor.ORO, Position(3, 0)),
                ChessPiece("n1_t_oro", PieceType.TORRE, PieceColor.ORO, Position(3, 3))
            ),
            turnoInicial = PieceColor.ORO
        ),
        6 to NivelConfig(
            piezas = listOf(
                ChessPiece("n1_k", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
                ChessPiece("n1_c", PieceType.CABALLO, PieceColor.PLATA, Position(2, 1)),
                ChessPiece("n1_q_oro", PieceType.REINA, PieceColor.ORO, Position(3, 0)),
                ChessPiece("n1_c_oro", PieceType.CABALLO, PieceColor.ORO, Position(3, 3))
            ),
            turnoInicial = PieceColor.ORO
        ),
        7 to NivelConfig(
            piezas = listOf(
                ChessPiece("n1_k", PieceType.REY, PieceColor.PLATA, Position(0, 0)),
                ChessPiece("n1_t", PieceType.TORRE, PieceColor.PLATA, Position(0, 2)),
                ChessPiece("n1_P", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
                ChessPiece("n1_P", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),

                ChessPiece("n1_c_oro", PieceType.CABALLO, PieceColor.ORO, Position(2, 0)),
                ChessPiece("n1_q_oro", PieceType.REINA, PieceColor.ORO, Position(2, 3)),
            ),
            turnoInicial = PieceColor.ORO
        ),
        8 to NivelConfig(
            piezas = listOf(
                ChessPiece("n1_k", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
                ChessPiece("n1_t", PieceType.TORRE, PieceColor.PLATA, Position(0, 1)),
                ChessPiece("n1_a", PieceType.ALFIL, PieceColor.PLATA, Position(0, 0)),
                ChessPiece("n1_t", PieceType.TORRE, PieceColor.PLATA, Position(1, 0)),
                ChessPiece("n1_p", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),

                ChessPiece("n1_p_oro", PieceType.PEON, PieceColor.ORO, Position(2, 3)),
                ChessPiece("n3_a_oro", PieceType.ALFIL, PieceColor.ORO, Position(3, 1)),
                ChessPiece("n3_a_oro", PieceType.ALFIL, PieceColor.ORO, Position(3, 2)),
            ),
            turnoInicial = PieceColor.ORO
        ),
        9 to NivelConfig(
            piezas = listOf(
                ChessPiece("n1_k", PieceType.REY, PieceColor.PLATA, Position(0, 1)),
                ChessPiece("n1_a", PieceType.ALFIL, PieceColor.PLATA, Position(1, 2)),
                ChessPiece("n1_q", PieceType.REINA, PieceColor.PLATA, Position(2, 0)),
                ChessPiece("n1_p", PieceType.PEON, PieceColor.PLATA, Position(3, 1)),
                ChessPiece("n1_p", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),

                ChessPiece("n1_q_oro", PieceType.REINA, PieceColor.ORO, Position(2, 2)),
                ChessPiece("n3_c_oro", PieceType.CABALLO, PieceColor.ORO, Position(3, 2)),

                ),
            turnoInicial = PieceColor.ORO
        ),
    )
}