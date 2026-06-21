package com.jasc.jasc_chess.data.local

import com.jasc.jasc_chess.model.*

import com.jasc.jasc_chess.model.ChessPiece
import com.jasc.jasc_chess.model.PieceColor
import com.jasc.jasc_chess.model.MovimientoSolucion // IMPORTA SOLO ESTA

data class MovimientoSolucion(val desde: Position, val hacia: Position)

data class NivelConfig(
    val id: Int,
    val size: Int,
    val piezas: List<ChessPiece>,
    val turnoInicial: PieceColor,
    val secuenciaSolucion: List<MovimientoSolucion>, // Aquí ya no debe haber error
    val acertijo: String,
    val respuestaAcertijo: String,
    val maxPasos: Int,
    val mateEnJugadas: Int = 1 // <--- El "= 1" hace que sea opcional
)

object NivelRepository {

    val totalNiveles = mutableMapOf<Int, NivelConfig>(
        1 to NivelConfig(9, 4, listOf(
            ChessPiece("n1_k", PieceType.REY, PieceColor.PLATA, Position(0, 1)),
            ChessPiece("n1_a", PieceType.ALFIL, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("n1_q", PieceType.REINA, PieceColor.PLATA, Position(2, 0)),
            ChessPiece("n1_p", PieceType.PEON, PieceColor.PLATA, Position(3, 1)),
            ChessPiece("n1_p", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("n1_q_oro", PieceType.REINA, PieceColor.ORO, Position(2, 2)),
            ChessPiece("n3_c_oro", PieceType.CABALLO, PieceColor.ORO, Position(3, 2))
        ), PieceColor.ORO,
            listOf(MovimientoSolucion(Position(3, 2), Position(2, 0))),
            "¿Cómo se llama la pieza que salta?", "caballo", 2),

        2 to NivelConfig(2, 4, listOf(
            ChessPiece("rey_plata_0_3", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("peon_plata_1_1", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
            ChessPiece("peon_plata_1_3", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
            ChessPiece("caballo_oro_3_0", PieceType.CABALLO, PieceColor.ORO, Position(3, 0)),
            ChessPiece("reina_oro_3_2", PieceType.REINA, PieceColor.ORO, Position(3, 2)),
            ChessPiece("reina_plata_0_0", PieceType.REINA, PieceColor.PLATA, Position(0, 0)),
            ChessPiece("caballo_oro_2_3", PieceType.CABALLO, PieceColor.ORO, Position(2, 3))
        ), PieceColor.ORO,
            listOf(MovimientoSolucion(Position(3, 0), Position(1, 1))),
            "¿Cuántas casillas tiene un tablero?", "64", 2),

        3 to NivelConfig(3, 4, listOf(
            ChessPiece("p_1_3_1780877008581", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
            ChessPiece("p_0_3_1780877020212", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("p_3_0_1780877095084", PieceType.CABALLO, PieceColor.ORO, Position(3, 0)),
            ChessPiece("p_3_2_1780877102870", PieceType.REINA, PieceColor.ORO, Position(3, 2)),
            ChessPiece("p_2_0_1780877455710", PieceType.ALFIL, PieceColor.ORO, Position(2, 0)),
            ChessPiece("p_2_3_1780877481867", PieceType.PEON, PieceColor.ORO, Position(2, 3)),
            ChessPiece("p_1_0_1780877491323", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("p_0_0_1780877549511", PieceType.TORRE, PieceColor.PLATA, Position(0, 0))
        ), PieceColor.ORO,
            listOf(MovimientoSolucion(Position(3, 2), Position(0, 2))),
            "¿Cuántas casillas tiene un tablero?", "64", 2),

        4 to NivelConfig(4, 4, listOf(
            ChessPiece("n3_k", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("n3_P", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("n3_P", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
            ChessPiece("n1_t", PieceType.TORRE, PieceColor.PLATA, Position(0, 1)),
            ChessPiece("n3_q_oro", PieceType.REINA, PieceColor.ORO, Position(2, 0)),
            ChessPiece("n3_C_oro", PieceType.CABALLO, PieceColor.ORO, Position(3, 2)),
            ChessPiece("n3_t_oro", PieceType.TORRE, PieceColor.ORO, Position(2, 2))
        ), PieceColor.ORO,
            listOf(MovimientoSolucion(Position(2, 0), Position(0, 2))),
            "¿Cuánto es 9x9 ?", "81", 2),

        5 to NivelConfig(5, 4, listOf(
            ChessPiece("n1_q", PieceType.REINA, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("n1_a", PieceType.ALFIL, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("n1_k", PieceType.REY, PieceColor.PLATA, Position(0, 2)),
            ChessPiece("n1_p", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
            ChessPiece("n1_t_oro", PieceType.TORRE, PieceColor.ORO, Position(1, 3)),
            ChessPiece("n1_a_oro", PieceType.ALFIL, PieceColor.ORO, Position(3, 0)),
            ChessPiece("n1_t_oro", PieceType.TORRE, PieceColor.ORO, Position(3, 3))
        ), PieceColor.ORO,
            listOf(MovimientoSolucion(Position(1, 3), Position(0, 3))),
            "¿Cuánto es 9x8 ?", "72", 2),

        6 to NivelConfig(6, 4, listOf(
            ChessPiece("n1_k", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("n1_c", PieceType.CABALLO, PieceColor.PLATA, Position(2, 1)),
            ChessPiece("n1_q_oro", PieceType.REINA, PieceColor.ORO, Position(3, 0)),
            ChessPiece("n1_c_oro", PieceType.CABALLO, PieceColor.ORO, Position(3, 3))
        ), PieceColor.ORO,
            listOf(MovimientoSolucion(Position(3, 0), Position(2, 1))),
            "¿Cómo se llama la pieza que salta?", "caballo", 2),

        7 to NivelConfig(7, 4, listOf(
            ChessPiece("n1_k", PieceType.REY, PieceColor.PLATA, Position(0, 0)),
            ChessPiece("n1_t", PieceType.TORRE, PieceColor.PLATA, Position(0, 2)),
            ChessPiece("n1_P", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("n1_P", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
            ChessPiece("n1_c_oro", PieceType.CABALLO, PieceColor.ORO, Position(2, 0)),
            ChessPiece("n1_q_oro", PieceType.REINA, PieceColor.ORO, Position(2, 3))
        ), PieceColor.ORO,
            listOf(MovimientoSolucion(Position(2, 3), Position(0, 1))),
            "¿Cuánto es 5 x 9 ?", "45", 2),

        8 to NivelConfig(8, 4, listOf(
            ChessPiece("n1_k", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("n1_t", PieceType.TORRE, PieceColor.PLATA, Position(0, 1)),
            ChessPiece("n1_a", PieceType.ALFIL, PieceColor.PLATA, Position(0, 0)),
            ChessPiece("n1_t", PieceType.TORRE, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("n1_p", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
            ChessPiece("n1_p_oro", PieceType.PEON, PieceColor.ORO, Position(2, 3)),
            ChessPiece("n3_a_oro", PieceType.ALFIL, PieceColor.ORO, Position(3, 1)),
            ChessPiece("n3_a_oro", PieceType.ALFIL, PieceColor.ORO, Position(3, 2))
        ), PieceColor.ORO,
            listOf(MovimientoSolucion(Position(3, 2), Position(2, 1))),
            "¿Cuánto es 9 x 3 ?", "27", 2),

        9 to NivelConfig(9, 4, listOf(
            ChessPiece("n1_k", PieceType.REY, PieceColor.PLATA, Position(0, 1)),
            ChessPiece("n1_a", PieceType.ALFIL, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("n1_q", PieceType.REINA, PieceColor.PLATA, Position(2, 0)),
            ChessPiece("n1_p", PieceType.PEON, PieceColor.PLATA, Position(3, 1)),
            ChessPiece("n1_p", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("n1_q_oro", PieceType.REINA, PieceColor.ORO, Position(2, 2)),
            ChessPiece("n3_c_oro", PieceType.CABALLO, PieceColor.ORO, Position(3, 2))
        ), PieceColor.ORO,
            listOf(MovimientoSolucion(Position(3, 2), Position(2, 0))),
            "¿Cuánto es 5 x 5 ?", "25", 2),

        10 to NivelConfig(10, 4, listOf(
            ChessPiece("n1_k", PieceType.REY, PieceColor.PLATA, Position(0, 0)),
            ChessPiece("n1_a", PieceType.ALFIL, PieceColor.PLATA, Position(0, 1)),
            ChessPiece("n1_p", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
            ChessPiece("n1_p", PieceType.PEON, PieceColor.PLATA, Position(3, 0)),
            ChessPiece("n1_q_oro", PieceType.REINA, PieceColor.ORO, Position(3, 1)),
            ChessPiece("n3_p_oro", PieceType.PEON, PieceColor.ORO, Position(2, 1)),
            ChessPiece("n3_a_oro", PieceType.ALFIL, PieceColor.ORO, Position(1, 3))
        ), PieceColor.ORO,
            listOf(MovimientoSolucion(Position(3, 1), Position(2, 0))),
            "¿Cuánto es 4 x 8  ?", "32", 2),

        11 to NivelConfig(11, 4, listOf(
            ChessPiece("n1_t1", PieceType.TORRE, PieceColor.PLATA, Position(0, 0)),
            ChessPiece("n1_t2", PieceType.TORRE, PieceColor.PLATA, Position(0, 1)),
            ChessPiece("n1_k", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("n1_p", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
            ChessPiece("n1_a", PieceType.ALFIL, PieceColor.PLATA, Position(2, 1)),
            ChessPiece("n1_q_oro", PieceType.REINA, PieceColor.ORO, Position(2, 0)),
            ChessPiece("n1_c_oro", PieceType.CABALLO, PieceColor.ORO, Position(3, 0)),
            ChessPiece("n1_t_oro", PieceType.TORRE, PieceColor.ORO, Position(3, 2))
        ), PieceColor.ORO,
            listOf(MovimientoSolucion(Position(2, 0), Position(0, 2))),
            "¿Cuánto es 6 x 7 ?", "42", 2),

        12 to NivelConfig(12, 4, listOf(
            ChessPiece("n2_t_oro", PieceType.TORRE, PieceColor.ORO, Position(3, 1)),
            ChessPiece("n1_q_oro", PieceType.REINA, PieceColor.ORO, Position(2, 0)),
            ChessPiece("n2_k", PieceType.REY, PieceColor.PLATA, Position(0, 0)),
            ChessPiece("n2_t", PieceType.TORRE, PieceColor.PLATA, Position(0, 1)),
            ChessPiece("n1_p", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("n2_t", PieceType.REINA, PieceColor.PLATA, Position(2, 2))
        ), PieceColor.ORO,
            listOf(MovimientoSolucion(Position(2, 0), Position(0, 2))),
            "¿Cuánto es 7 x 9 ?",
            "63", 2),

        13 to NivelConfig(13, 5, listOf(
            ChessPiece("n2_k", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("n1_a", PieceType.ALFIL, PieceColor.PLATA, Position(2, 2)),
            ChessPiece("n1_p1", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
            ChessPiece("n2_t2", PieceType.TORRE, PieceColor.PLATA, Position(0, 4)),
            ChessPiece("n2_q", PieceType.REINA, PieceColor.ORO, Position(4, 3)),
            ChessPiece("n1_c_oro", PieceType.CABALLO, PieceColor.ORO, Position(3, 3))
        ), PieceColor.ORO,
            listOf(MovimientoSolucion(Position(4, 3), Position(2, 1))),
            "¿Cuanto es 5 x 8 ?", "40", 2),

        14 to NivelConfig(14, 6, listOf(
            ChessPiece("n2_k", PieceType.REY, PieceColor.PLATA, Position(0, 5)),
            ChessPiece("n2_t1", PieceType.TORRE, PieceColor.PLATA, Position(0, 0)),
            ChessPiece("n2_t2", PieceType.TORRE, PieceColor.PLATA, Position(0, 2)),
            ChessPiece("n1_p1", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("n1_p2", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
            ChessPiece("n1_q_oro", PieceType.REINA, PieceColor.ORO, Position(5, 2)),
            ChessPiece("n2_t_oro", PieceType.TORRE, PieceColor.ORO, Position(5, 4))
        ), PieceColor.ORO,
            listOf(MovimientoSolucion(Position(5, 2), Position(2, 5))),
            "¿Cómo se llama la pieza más importante que se mueve por todo el tablero?",
            "reina", 2),

        15 to NivelConfig(15, 6, listOf(
            ChessPiece("n2_k", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("n1_a", PieceType.ALFIL, PieceColor.PLATA, Position(0, 4)),
            ChessPiece("n2_t1", PieceType.TORRE, PieceColor.PLATA, Position(0, 0)),
            ChessPiece("n2_t2", PieceType.TORRE, PieceColor.PLATA, Position(0, 2)),
            ChessPiece("n1_p1", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("n1_p2", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
            ChessPiece("n1_c_oro", PieceType.CABALLO, PieceColor.ORO, Position(3, 0)),
            ChessPiece("n1_q_oro", PieceType.REINA, PieceColor.ORO, Position(2, 0)),
            ChessPiece("n2_t_oro", PieceType.TORRE, PieceColor.ORO, Position(5, 4))
        ), PieceColor.ORO,
            listOf(MovimientoSolucion(Position(2, 0), Position(0, 2))),
            "¿Cómo se llama la pieza que salta?", "caballo", 2),

        16 to NivelConfig(16, 5, listOf(
            ChessPiece("p_0_1_1780980688004", PieceType.REY, PieceColor.PLATA, Position(0, 1)),
            ChessPiece("p_0_2_1780980691412", PieceType.REINA, PieceColor.PLATA, Position(0, 2)),
            ChessPiece("p_1_0_1780980694431", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("p_1_1_1780980694685", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
            ChessPiece("p_1_2_1780980694917", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("p_3_0_1780980702060", PieceType.REINA, PieceColor.ORO, Position(3, 0)),
            ChessPiece("p_3_3_1780980708475", PieceType.TORRE, PieceColor.ORO, Position(3, 3)),
            ChessPiece("p_3_4_1780980711305", PieceType.ALFIL, PieceColor.ORO, Position(3, 4))
        ), PieceColor.ORO,
            listOf(MovimientoSolucion(Position(3, 0), Position(1, 2))),
            "¿Cuánto es 9 x 5 ?", "45", 2),

        17 to NivelConfig(17, 4, listOf(
            ChessPiece("p_1_2_1780876623237", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("p_2_1_1780876625924", PieceType.ALFIL, PieceColor.PLATA, Position(2, 1)),
            ChessPiece("p_3_2_1780876633828", PieceType.CABALLO, PieceColor.ORO, Position(3, 2)),
            ChessPiece("p_3_1_1780876641967", PieceType.REINA, PieceColor.ORO, Position(3, 1)),
            ChessPiece("p_0_3_1780876660724", PieceType.TORRE, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("p_0_2_1780876668438", PieceType.REY, PieceColor.PLATA, Position(0, 2))
        ), PieceColor.ORO,
            listOf(MovimientoSolucion(Position(3, 1), Position(2, 0))),
            "¿Cuánto es 6 x 6?", "36", 2),

        18 to NivelConfig(id = 18, size = 6, piezas = listOf(
            ChessPiece("alfil_plata_0_0", PieceType.ALFIL, PieceColor.PLATA, Position(0, 0)),
            ChessPiece("alfil_plata_0_3", PieceType.ALFIL, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("rey_plata_0_4", PieceType.REY, PieceColor.PLATA, Position(0, 4)),
            ChessPiece("torre_plata_0_5", PieceType.TORRE, PieceColor.PLATA, Position(0, 5)),
            ChessPiece("peon_plata_1_0", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("peon_plata_2_1", PieceType.PEON, PieceColor.PLATA, Position(2, 1)),
            ChessPiece("peon_plata_1_5", PieceType.PEON, PieceColor.PLATA, Position(1, 5)),
            ChessPiece("caballo_plata_2_4", PieceType.CABALLO, PieceColor.PLATA, Position(2, 4)),
            ChessPiece("torre_oro_2_3", PieceType.TORRE, PieceColor.ORO, Position(2, 3)),
            ChessPiece("alfil_oro_2_5", PieceType.ALFIL, PieceColor.ORO, Position(2, 5)),
            ChessPiece("reina_oro_3_5", PieceType.REINA, PieceColor.ORO, Position(3, 5))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(3, 5), Position(3, 1))),
            acertijo = "¿Cuánto es 3 x 8 ?", respuestaAcertijo = "24", maxPasos = 2),

        19 to NivelConfig(id = 19, size = 6, piezas = listOf(
            ChessPiece("rey_plata_0_1", PieceType.REY, PieceColor.PLATA, Position(0, 1)),
            ChessPiece("peon_plata_1_0", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("peon_plata_1_2", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("reina_plata_0_3", PieceType.REINA, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("peon_oro_2_2", PieceType.PEON, PieceColor.ORO, Position(2, 2)),
            ChessPiece("peon_oro_3_4", PieceType.PEON, PieceColor.ORO, Position(3, 4)),
            ChessPiece("reina_oro_2_4", PieceType.REINA, PieceColor.ORO, Position(2, 4))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(2, 4), Position(2, 1))),
            acertijo = "¿Cuánto es 7 x 5 ?", respuestaAcertijo = "35", maxPasos = 2),

        20 to NivelConfig(id = 20, size = 4, piezas = listOf(
            ChessPiece("alfil_plata_0_1", PieceType.ALFIL, PieceColor.PLATA, Position(0, 1)),
            ChessPiece("rey_plata_0_2", PieceType.REY, PieceColor.PLATA, Position(0, 2)),
            ChessPiece("torre_plata_0_3", PieceType.TORRE, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("torre_plata_1_0", PieceType.TORRE, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("peon_plata_1_2", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("peon_plata_1_3", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
            ChessPiece("peon_oro_2_0", PieceType.PEON, PieceColor.ORO, Position(2, 0)),
            ChessPiece("caballo_oro_3_0", PieceType.CABALLO, PieceColor.ORO, Position(3, 0)),
            ChessPiece("reina_oro_3_2", PieceType.REINA, PieceColor.ORO, Position(3, 2)),
            ChessPiece("alfil_oro_2_2", PieceType.ALFIL, PieceColor.ORO, Position(2, 2))
        ), PieceColor.ORO,
            listOf(MovimientoSolucion(Position(2, 2), Position(1, 1))),
            "¿Cuántas casillas tiene un tablero?", "64", 2),

        21 to NivelConfig(id =21, size = 5, piezas = listOf(
            ChessPiece("peon_plata_1_0", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("peon_plata_1_1", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
            ChessPiece("peon_plata_1_2", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("rey_plata_0_0", PieceType.REY, PieceColor.PLATA, Position(0, 0)),
            ChessPiece("torre_plata_0_2", PieceType.TORRE, PieceColor.PLATA, Position(0, 2)),
            ChessPiece("reina_plata_4_4", PieceType.REINA, PieceColor.PLATA, Position(4, 4)),
            ChessPiece("torre_oro_3_3", PieceType.TORRE, PieceColor.ORO, Position(3, 3)),
            ChessPiece("reina_oro_4_3", PieceType.REINA, PieceColor.ORO, Position(4, 3)),
            ChessPiece("caballo_oro_1_3", PieceType.CABALLO, PieceColor.ORO, Position(1, 3))
        ), turnoInicial = PieceColor.ORO,
            secuenciaSolucion = listOf(MovimientoSolucion(Position(4, 3), Position(1, 0))),
            acertijo = "cuantas piezas negras trae un ajedrez", respuestaAcertijo = "16", maxPasos = 2),

        22 to NivelConfig(id = 22, size = 5, piezas = listOf(
            ChessPiece("rey_plata_0_2", PieceType.REY, PieceColor.PLATA, Position(0, 2)),
            ChessPiece("torre_plata_1_1", PieceType.TORRE, PieceColor.PLATA, Position(1, 1)),
            ChessPiece("caballo_plata_1_3", PieceType.CABALLO, PieceColor.PLATA, Position(1, 3)),
            ChessPiece("peon_plata_2_1", PieceType.PEON, PieceColor.PLATA, Position(2, 1)),
            ChessPiece("peon_plata_3_0", PieceType.PEON, PieceColor.PLATA, Position(3, 0)),
            ChessPiece("caballo_oro_3_1", PieceType.CABALLO, PieceColor.ORO, Position(3, 1)),
            ChessPiece("caballo_oro_2_2", PieceType.CABALLO, PieceColor.ORO, Position(2, 2)),
            ChessPiece("reina_oro_2_3", PieceType.REINA, PieceColor.ORO, Position(2, 3))
        ), turnoInicial = PieceColor.ORO,
            secuenciaSolucion = listOf(MovimientoSolucion(Position(2, 3), Position(1, 4))),
            acertijo = "cuantas piezas trae un tablero de ajedrez", respuestaAcertijo = "32", maxPasos = 2),

        23 to NivelConfig(id = 23, size = 5, piezas = listOf(
            ChessPiece("peon_plata_1_2", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("peon_plata_2_1", PieceType.PEON, PieceColor.PLATA, Position(2, 1)),
            ChessPiece("peon_plata_2_2", PieceType.PEON, PieceColor.PLATA, Position(2, 2)),
            ChessPiece("peon_plata_2_4", PieceType.PEON, PieceColor.PLATA, Position(2, 4)),
            ChessPiece("alfil_plata_1_4", PieceType.ALFIL, PieceColor.PLATA, Position(1, 4)),
            ChessPiece("reina_plata_1_0", PieceType.REINA, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("caballo_plata_0_1", PieceType.CABALLO, PieceColor.PLATA, Position(0, 1)),
            ChessPiece("rey_plata_0_4", PieceType.REY, PieceColor.PLATA, Position(0, 4)),
            ChessPiece("alfil_oro_4_0", PieceType.ALFIL, PieceColor.ORO, Position(4, 0)),
            ChessPiece("reina_oro_3_1", PieceType.REINA, PieceColor.ORO, Position(3, 1)),
            ChessPiece("torre_oro_4_3", PieceType.TORRE, PieceColor.ORO, Position(4, 3)),
            ChessPiece("peon_oro_3_4", PieceType.PEON, PieceColor.ORO, Position(3, 4))
        ), turnoInicial = PieceColor.ORO,
            secuenciaSolucion = listOf(MovimientoSolucion(Position(3, 1), Position(2, 3))),
            acertijo = "¿Cuánto es 7x4", respuestaAcertijo = "28", maxPasos = 2),

        24 to NivelConfig(id = 24, size = 5, piezas = listOf(
            ChessPiece("torre_oro_0_1", PieceType.TORRE, PieceColor.ORO, Position(0, 2)),
            ChessPiece("torre_oro_4_3", PieceType.TORRE, PieceColor.ORO, Position(4, 3)),
            ChessPiece("caballo_oro_2_1", PieceType.CABALLO, PieceColor.ORO, Position(2, 2)),
            ChessPiece("alfil_oro_2_3", PieceType.ALFIL, PieceColor.ORO, Position(2, 4)),
            ChessPiece("rey_plata_0_3", PieceType.REY, PieceColor.PLATA, Position(0, 4)),
            ChessPiece("caballo_plata_0_2", PieceType.CABALLO, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("torre_plata_0_0", PieceType.TORRE, PieceColor.PLATA, Position(0, 1))
        ), turnoInicial = PieceColor.ORO,
            secuenciaSolucion = listOf(MovimientoSolucion(desde = Position(0, 2), Position(0,3))),
            acertijo = "¿Cuanto es 9 x 9 - 3", respuestaAcertijo = "78", maxPasos = 2),

        25 to NivelConfig(id = 25, size = 4, piezas = listOf(
            ChessPiece("torre_plata_0_0", PieceType.TORRE, PieceColor.PLATA, Position(0, 0)),
            ChessPiece("peon_plata_1_1", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
            ChessPiece("peon_plata_2_2", PieceType.PEON, PieceColor.PLATA, Position(2, 2)),
            ChessPiece("alfil_plata_1_0", PieceType.ALFIL, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("caballo_oro_0_1", PieceType.CABALLO, PieceColor.ORO, Position(0, 1)),
            ChessPiece("rey_plata_0_2", PieceType.REY, PieceColor.PLATA, Position(0, 2)),
            ChessPiece("reina_oro_3_3", PieceType.REINA, PieceColor.ORO, Position(3, 3))
        ), turnoInicial = PieceColor.ORO,
            secuenciaSolucion = listOf(MovimientoSolucion(desde = Position(3,3), Position(1,3))),
            acertijo = "cuantas es 8 x 9 + 3", respuestaAcertijo = "75", maxPasos = 2),

        26 to NivelConfig(id = 26, size = 5, piezas = listOf(
            ChessPiece("reina_plata_4_4", PieceType.REINA, PieceColor.ORO, Position(4, 4)),
            ChessPiece("alfil_plata_3_4", PieceType.ALFIL, PieceColor.ORO, Position(3, 4)),
            ChessPiece("peon_plata_2_0", PieceType.PEON, PieceColor.ORO, Position(2, 0)),
            ChessPiece("peon_oro_1_0", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("peon_oro_2_3", PieceType.PEON, PieceColor.PLATA, Position(2, 3)),
            ChessPiece("reina_oro_1_3", PieceType.REINA, PieceColor.PLATA, Position(1, 3)),
            ChessPiece("torre_oro_0_3", PieceType.TORRE, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("rey_oro_0_1", PieceType.REY, PieceColor.PLATA, Position(0, 1))
        ), turnoInicial = PieceColor.ORO,
            secuenciaSolucion = listOf(MovimientoSolucion(desde = Position(3,4), Position(2,3))),
            acertijo = "¿Cuánto es 8 x 9 + 4", respuestaAcertijo = "76", maxPasos = 2),

        27 to NivelConfig(id = 27, size = 6, piezas = listOf(
            ChessPiece("peon_oro_2_0", PieceType.PEON, PieceColor.ORO, Position(2, 0)),
            ChessPiece("peon_oro_3_2", PieceType.PEON, PieceColor.ORO, Position(3, 2)),
            ChessPiece("caballo_oro_3_4", PieceType.CABALLO, PieceColor.ORO, Position(3, 4)),
            ChessPiece("peon_plata_1_4", PieceType.PEON, PieceColor.PLATA, Position(1, 4)),
            ChessPiece("peon_plata_1_3", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
            ChessPiece("rey_plata_1_2", PieceType.REY, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("alfil_plata_1_0", PieceType.ALFIL, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("alfil_plata_0_2", PieceType.ALFIL, PieceColor.PLATA, Position(0, 2)),
            ChessPiece("torre_plata_0_3", PieceType.TORRE, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("torre_oro_5_1", PieceType.TORRE, PieceColor.ORO, Position(5, 1)),
            ChessPiece("alfil_oro_5_0", PieceType.ALFIL, PieceColor.ORO, Position(5, 0)),
            ChessPiece("reina_oro_5_3", PieceType.REINA, PieceColor.ORO, Position(5, 3))
        ), turnoInicial = PieceColor.ORO,
            secuenciaSolucion = listOf(MovimientoSolucion(desde = Position(5,3), Position(2,3))),
            acertijo = "¿Cuánto es 8 x 8 + 10 ?", respuestaAcertijo = "74", maxPasos = 2),

        28 to NivelConfig(28, 6, listOf(
            ChessPiece("n1_caballo_plata", PieceType.CABALLO, PieceColor.PLATA, Position(0, 0)),
            ChessPiece("n2_alfil_plata", PieceType.ALFIL, PieceColor.PLATA, Position(0, 1)),
            ChessPiece("n3_peon_plata", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("n4_peon_plata", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
            ChessPiece("n5_rey_plata", PieceType.REY, PieceColor.PLATA, Position(2, 2)),
            ChessPiece("n6_peon_plata", PieceType.PEON, PieceColor.PLATA, Position(3, 3)),
            ChessPiece("n1_peon_oro", PieceType.PEON, PieceColor.ORO, Position(3, 2)),
            ChessPiece("n2_alfil_oro", PieceType.ALFIL, PieceColor.ORO, Position(3, 0)),
            ChessPiece("n3_caballo_oro", PieceType.CABALLO, PieceColor.ORO, Position(5, 1)),
            ChessPiece("n4_alfil_oro", PieceType.ALFIL, PieceColor.ORO, Position(4, 4)),
            ChessPiece("n5_reina_oro", PieceType.REINA, PieceColor.ORO, Position(3, 5))
        ), PieceColor.ORO,
            listOf(MovimientoSolucion(Position(5, 1), Position(4, 3))),
            "¿Cuánto es 8x8 ?", "64", 2),

        29 to NivelConfig(id = 29, size = 5, piezas = listOf(
            ChessPiece("caballo_oro_1_1", PieceType.CABALLO, PieceColor.ORO, Position(1, 1)),
            ChessPiece("alfil_oro_4_1", PieceType.ALFIL, PieceColor.ORO, Position(4, 1)),
            ChessPiece("torre_plata_3_2", PieceType.TORRE, PieceColor.PLATA, Position(3, 2)),
            ChessPiece("peon_plata_2_4", PieceType.PEON, PieceColor.PLATA, Position(2, 4)),
            ChessPiece("rey_plata_1_4", PieceType.REY, PieceColor.PLATA, Position(1, 4)),
            ChessPiece("alfil_plata_1_3", PieceType.ALFIL, PieceColor.PLATA, Position(1, 3)),
            ChessPiece("torre_plata_0_3", PieceType.TORRE, PieceColor.PLATA, Position(0, 3))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(4, 1), Position(3, 2))),
            acertijo = " ¿Cuánto es 4 x 9 - 2 ", respuestaAcertijo = "34", maxPasos = 4),

        30 to NivelConfig(id = 30, size = 5, piezas = listOf(
            ChessPiece("torre_oro_3_2", PieceType.TORRE, PieceColor.ORO, Position(3, 2)),
            ChessPiece("reina_oro_1_3", PieceType.REINA, PieceColor.ORO, Position(1, 3)),
            ChessPiece("caballo_oro_3_1", PieceType.CABALLO, PieceColor.ORO, Position(3, 1)),
            ChessPiece("reina_plata_4_1", PieceType.REINA, PieceColor.PLATA, Position(4, 1)),
            ChessPiece("alfil_plata_1_1", PieceType.ALFIL, PieceColor.PLATA, Position(1, 1)),
            ChessPiece("rey_plata_0_1", PieceType.REY, PieceColor.PLATA, Position(0, 1)),
            ChessPiece("caballo_plata_2_0", PieceType.CABALLO, PieceColor.PLATA, Position(2, 0)),
            ChessPiece("caballo_plata_0_4", PieceType.CABALLO, PieceColor.PLATA, Position(0, 4))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(3, 2), Position(0, 2))),
            acertijo = "Qué ciudad es nombrada la eterna primavera?", respuestaAcertijo = "Medellín", maxPasos = 2),

        31 to NivelConfig(id = 31, size = 5, piezas = listOf(
            ChessPiece("peon_plata_1_0", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("peon_plata_1_1", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
            ChessPiece("peon_plata_1_2", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("rey_plata_0_0", PieceType.REY, PieceColor.PLATA, Position(0, 0)),
            ChessPiece("torre_plata_0_2", PieceType.TORRE, PieceColor.PLATA, Position(0, 2)),
            ChessPiece("reina_plata_4_4", PieceType.REINA, PieceColor.PLATA, Position(4, 4)),
            ChessPiece("reina_oro_4_3", PieceType.REINA, PieceColor.ORO, Position(4, 3)),
            ChessPiece("torre_oro_3_3", PieceType.TORRE, PieceColor.ORO, Position(3, 3)),
            ChessPiece("caballo_oro_1_3", PieceType.CABALLO, PieceColor.ORO, Position(1, 3))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(4, 3), Position(1, 0))),
            acertijo = "ajedrez en ingles", respuestaAcertijo = "chess", maxPasos = 2),
        32 to NivelConfig(id = 32, size = 4, piezas = listOf(
            ChessPiece("caballo_oro_3_2", PieceType.CABALLO, PieceColor.ORO, Position(3, 2)),
            ChessPiece("reina_oro_2_2", PieceType.REINA, PieceColor.ORO, Position(2, 2)),
            ChessPiece("peon_plata_3_1", PieceType.PEON, PieceColor.PLATA, Position(3, 1)),
            ChessPiece("peon_plata_1_0", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("reina_plata_2_0", PieceType.REINA, PieceColor.PLATA, Position(2, 0)),
            ChessPiece("alfil_plata_1_2", PieceType.ALFIL, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("rey_plata_0_1", PieceType.REY, PieceColor.PLATA, Position(0, 1))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(3, 2), Position(2, 0))),
            acertijo = "¿Cuánto es 7 x 7 ?", respuestaAcertijo = "49", maxPasos = 2),
        33 to NivelConfig(id = 33, size = 6, piezas = listOf(
            ChessPiece("alfil_oro_4_0", PieceType.ALFIL, PieceColor.ORO, Position(4, 0)),
            ChessPiece("reina_oro_3_4", PieceType.REINA, PieceColor.ORO, Position(3, 4)),
            ChessPiece("peon_plata_1_0", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("peon_plata_1_1", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
            ChessPiece("peon_plata_1_5", PieceType.PEON, PieceColor.PLATA, Position(1, 5)),
            ChessPiece("alfil_plata_0_0", PieceType.ALFIL, PieceColor.PLATA, Position(0, 0)),
            ChessPiece("alfil_plata_0_3", PieceType.ALFIL, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("reina_plata_0_1", PieceType.REINA, PieceColor.PLATA, Position(0, 1)),
            ChessPiece("rey_plata_0_2", PieceType.REY, PieceColor.PLATA, Position(0, 2)),
            ChessPiece("caballo_plata_0_4", PieceType.CABALLO, PieceColor.PLATA, Position(0, 4)),
            ChessPiece("torre_plata_0_5", PieceType.TORRE, PieceColor.PLATA, Position(0, 5))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(3, 4), Position(3, 2))),
            acertijo = "¿Cuánto es 7 x 8 ?", respuestaAcertijo = "56", maxPasos = 2),
        34 to NivelConfig(id = 34, size = 5, piezas = listOf(
            ChessPiece("caballo_plata_0_4", PieceType.CABALLO, PieceColor.PLATA, Position(0, 4)),
            ChessPiece("caballo_plata_2_0", PieceType.CABALLO, PieceColor.PLATA, Position(2, 0)),
            ChessPiece("rey_plata_0_1", PieceType.REY, PieceColor.PLATA, Position(0, 1)),
            ChessPiece("alfil_plata_1_1", PieceType.ALFIL, PieceColor.PLATA, Position(1, 1)),
            ChessPiece("reina_plata_4_1", PieceType.REINA, PieceColor.PLATA, Position(4, 1)),
            ChessPiece("torre_oro_3_2", PieceType.TORRE, PieceColor.ORO, Position(3, 2)),
            ChessPiece("caballo_oro_3_1", PieceType.CABALLO, PieceColor.ORO, Position(3, 1)),
            ChessPiece("reina_oro_1_3", PieceType.REINA, PieceColor.ORO, Position(1, 3))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(3, 2), Position(0, 2))),
            acertijo = "¿Cuánto es 7 x 3 ?", respuestaAcertijo = "21", maxPasos = 2),

        35 to NivelConfig(id = 35, size = 5, piezas = listOf(
            ChessPiece("alfil_oro_4_0", PieceType.ALFIL, PieceColor.ORO, Position(4, 0)),
            ChessPiece("alfil_oro_3_4", PieceType.ALFIL, PieceColor.ORO, Position(3, 4)),
            ChessPiece("peon_oro_3_3", PieceType.PEON, PieceColor.ORO, Position(3, 3)),
            ChessPiece("torre_oro_4_2", PieceType.TORRE, PieceColor.ORO, Position(4, 2)),
            ChessPiece("caballo_oro_4_3", PieceType.CABALLO, PieceColor.ORO, Position(4, 3)),
            ChessPiece("torre_plata_0_1", PieceType.TORRE, PieceColor.PLATA, Position(0, 1)),
            ChessPiece("torre_plata_1_1", PieceType.TORRE, PieceColor.PLATA, Position(1, 1)),
            ChessPiece("peon_plata_1_2", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("caballo_plata_0_3", PieceType.CABALLO, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("rey_plata_1_4", PieceType.REY, PieceColor.PLATA, Position(1, 4))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(4, 2), Position(1, 2))),
            acertijo = "¿Cuánto es 3 x 8 ?", respuestaAcertijo = "24", maxPasos = 2),

        36 to NivelConfig(id = 36, size = 6, piezas = listOf(
            ChessPiece("alfil_oro_5_0", PieceType.ALFIL, PieceColor.ORO, Position(5, 0)),
            ChessPiece("torre_oro_5_1", PieceType.TORRE, PieceColor.ORO, Position(5, 1)),
            ChessPiece("peon_oro_4_3", PieceType.PEON, PieceColor.ORO, Position(4, 3)),
            ChessPiece("caballo_oro_3_2", PieceType.CABALLO, PieceColor.ORO, Position(3, 2)),
            ChessPiece("alfil_oro_1_1", PieceType.ALFIL, PieceColor.ORO, Position(1, 1)),
            ChessPiece("torre_plata_0_3", PieceType.TORRE, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("peon_plata_1_3", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
            ChessPiece("alfil_plata_1_0", PieceType.ALFIL, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("alfil_plata_4_2", PieceType.ALFIL, PieceColor.PLATA, Position(4, 2)),
            ChessPiece("rey_plata_0_1", PieceType.REY, PieceColor.PLATA, Position(0, 1))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(3, 2), Position(2, 0))),
            acertijo = "¿Cuánto es 3 x 4 ?", respuestaAcertijo = "12", maxPasos = 2),

        37 to NivelConfig(id = 37, size = 6, piezas = listOf(
            ChessPiece("rey_plata_0_4", PieceType.REY, PieceColor.PLATA, Position(0, 4)),
            ChessPiece("torre_plata_0_3", PieceType.TORRE, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("peon_plata_1_3", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
            ChessPiece("peon_plata_1_4", PieceType.PEON, PieceColor.PLATA, Position(1, 4)),
            ChessPiece("torre_oro_3_2", PieceType.TORRE, PieceColor.ORO, Position(3, 2)),
            ChessPiece("caballo_oro_3_1", PieceType.CABALLO, PieceColor.ORO, Position(3, 1))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(3, 1), Position(1, 2))),
            acertijo = "¿Cuánto es 4 x 4 ?", respuestaAcertijo = "16", maxPasos = 2),
        38 to NivelConfig(id = 38, size = 6, piezas = listOf(
            ChessPiece("peon_oro_4_3", PieceType.PEON, PieceColor.ORO, Position(4, 3)),
            ChessPiece("peon_oro_4_5", PieceType.PEON, PieceColor.ORO, Position(4, 5)),
            ChessPiece("alfil_oro_5_3", PieceType.ALFIL, PieceColor.ORO, Position(5, 3)),
            ChessPiece("caballo_oro_2_1", PieceType.CABALLO, PieceColor.ORO, Position(2, 1)),
            ChessPiece("torre_oro_0_3", PieceType.TORRE, PieceColor.ORO, Position(0, 3)),
            ChessPiece("caballo_plata_1_2", PieceType.CABALLO, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("rey_plata_2_4", PieceType.REY, PieceColor.PLATA, Position(2, 4)),
            ChessPiece("peon_plata_1_4", PieceType.PEON, PieceColor.PLATA, Position(1, 4)),
            ChessPiece("peon_plata_1_5", PieceType.PEON, PieceColor.PLATA, Position(1, 5)),
            ChessPiece("peon_plata_2_2", PieceType.PEON, PieceColor.PLATA, Position(2, 2))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(4, 5), Position(3, 5))),
            acertijo = "¿Cuánto es 2 x 4 ", respuestaAcertijo = "8", maxPasos = 2),
        39 to NivelConfig(id = 39, size = 6, piezas = listOf(
            ChessPiece("rey_plata_0_5", PieceType.REY, PieceColor.PLATA, Position(0, 5)),
            ChessPiece("caballo_plata_0_2", PieceType.CABALLO, PieceColor.PLATA, Position(0, 2)),
            ChessPiece("reina_plata_1_1", PieceType.REINA, PieceColor.PLATA, Position(1, 1)),
            ChessPiece("peon_plata_1_3", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
            ChessPiece("peon_plata_2_3", PieceType.PEON, PieceColor.PLATA, Position(2, 3)),
            ChessPiece("peon_plata_2_2", PieceType.PEON, PieceColor.PLATA, Position(2, 2)),
            ChessPiece("peon_plata_2_5", PieceType.PEON, PieceColor.PLATA, Position(2, 5)),
            ChessPiece("alfil_plata_1_5", PieceType.ALFIL, PieceColor.PLATA, Position(1, 5)),
            ChessPiece("peon_oro_3_5", PieceType.PEON, PieceColor.ORO, Position(3, 5)),
            ChessPiece("torre_oro_4_4", PieceType.TORRE, PieceColor.ORO, Position(4, 4)),
            ChessPiece("alfil_oro_4_1", PieceType.ALFIL, PieceColor.ORO, Position(4, 1)),
            ChessPiece("reina_oro_3_2", PieceType.REINA, PieceColor.ORO, Position(3, 2))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(3, 2), Position(2, 3))),
            acertijo = "¿Cuánto es 2 x 9 ", respuestaAcertijo = "18", maxPasos = 2),

        40 to NivelConfig(id = 40, size = 5, piezas = listOf(
            ChessPiece("alfil_oro_4_3", PieceType.ALFIL, PieceColor.ORO, Position(4, 3)),
            ChessPiece("reina_oro_2_3", PieceType.REINA, PieceColor.ORO, Position(2, 3)),
            ChessPiece("rey_plata_1_0", PieceType.REY, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("alfil_plata_1_1", PieceType.ALFIL, PieceColor.PLATA, Position(1, 1)),
            ChessPiece("peon_plata_3_0", PieceType.PEON, PieceColor.PLATA, Position(3, 0)),
            ChessPiece("peon_plata_2_1", PieceType.PEON, PieceColor.PLATA, Position(2, 1))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(4, 3), Position(2, 1))),
            acertijo = "¿Cuánto es 6 x 9 ", respuestaAcertijo = "54", maxPasos = 4),

        41 to NivelConfig(id = 41, size = 6, piezas = listOf(
            ChessPiece("peon_plata_1_4", PieceType.PEON, PieceColor.PLATA, Position(1, 4)),
            ChessPiece("peon_plata_2_3", PieceType.PEON, PieceColor.PLATA, Position(2, 3)),
            ChessPiece("peon_plata_3_4", PieceType.PEON, PieceColor.PLATA, Position(3, 4)),
            ChessPiece("caballo_plata_1_5", PieceType.CABALLO, PieceColor.PLATA, Position(1, 5)),
            ChessPiece("torre_plata_0_3", PieceType.TORRE, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("rey_plata_0_4", PieceType.REY, PieceColor.PLATA, Position(0, 4)),
            ChessPiece("reina_plata_0_1", PieceType.REINA, PieceColor.PLATA, Position(0, 1)),
            ChessPiece("caballo_oro_3_1", PieceType.CABALLO, PieceColor.ORO, Position(3, 1)),
            ChessPiece("alfil_oro_4_0", PieceType.ALFIL, PieceColor.ORO, Position(4, 0))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(3, 1), Position(1, 2))),
            acertijo = "¿Cuánto es 7 x 9 ", respuestaAcertijo = "63", maxPasos = 4),

        42 to NivelConfig(id = 42, size = 5, piezas = listOf(
            ChessPiece("torre_oro_4_4", PieceType.TORRE, PieceColor.ORO, Position(4, 4)),
            ChessPiece("caballo_oro_3_3", PieceType.CABALLO, PieceColor.ORO, Position(3, 3)),
            ChessPiece("reina_oro_2_3", PieceType.REINA, PieceColor.ORO, Position(2, 3)),
            ChessPiece("reina_plata_2_2", PieceType.REINA, PieceColor.PLATA, Position(2, 2)),
            ChessPiece("torre_plata_0_1", PieceType.TORRE, PieceColor.PLATA, Position(0, 1)),
            ChessPiece("rey_plata_0_3", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("alfil_plata_1_3", PieceType.ALFIL, PieceColor.PLATA, Position(1, 3))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(4, 4), Position(1, 4))),
            acertijo = "¿Cuánto es 7 x 2 ", respuestaAcertijo = "14", maxPasos = 4),
        43 to NivelConfig(id = 43, size = 6, piezas = listOf(
            ChessPiece("torre_plata_0_1", PieceType.TORRE, PieceColor.PLATA, Position(0, 1)),
            ChessPiece("torre_plata_0_3", PieceType.TORRE, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("peon_plata_0_2", PieceType.PEON, PieceColor.PLATA, Position(0, 2)),
            ChessPiece("peon_plata_1_5", PieceType.PEON, PieceColor.PLATA, Position(1, 5)),
            ChessPiece("peon_plata_2_3", PieceType.PEON, PieceColor.PLATA, Position(2, 3)),
            ChessPiece("rey_plata_2_4", PieceType.REY, PieceColor.PLATA, Position(2, 4)),
            ChessPiece("peon_plata_4_3", PieceType.PEON, PieceColor.PLATA, Position(4, 3)),
            ChessPiece("alfil_plata_0_4", PieceType.ALFIL, PieceColor.PLATA, Position(0, 4)),
            ChessPiece("caballo_oro_3_3", PieceType.CABALLO, PieceColor.ORO, Position(3, 3)),
            ChessPiece("peon_oro_4_2", PieceType.PEON, PieceColor.ORO, Position(4, 2)),
            ChessPiece("reina_oro_5_3", PieceType.REINA, PieceColor.ORO, Position(5, 3))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(5, 3), Position(4, 4))),
            acertijo = "¿Cuánto es 6 x 2 ", respuestaAcertijo = "12", maxPasos = 3),
        44 to NivelConfig(id = 44, size = 5, piezas = listOf(
            ChessPiece("peon_plata_1_0", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("torre_plata_0_3", PieceType.TORRE, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("rey_plata_1_1", PieceType.REY, PieceColor.PLATA, Position(1, 1)),
            ChessPiece("peon_plata_1_3", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
            ChessPiece("peon_oro_1_2", PieceType.PEON, PieceColor.ORO, Position(1, 2)),
            ChessPiece("peon_oro_2_3", PieceType.PEON, PieceColor.ORO, Position(2, 3)),
            ChessPiece("peon_oro_3_1", PieceType.PEON, PieceColor.ORO, Position(3, 1)),
            ChessPiece("reina_oro_3_0", PieceType.REINA, PieceColor.ORO, Position(3, 0)),
            ChessPiece("caballo_oro_4_2", PieceType.CABALLO, PieceColor.ORO, Position(4, 2))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(3, 0), Position(2, 0))),
            acertijo = "¿Cuánto es 6 x 5 ", respuestaAcertijo = "30", maxPasos = 3),

        45 to NivelConfig(id = 45, size = 6, piezas = listOf(
            ChessPiece("torre_oro_5_5", PieceType.TORRE, PieceColor.ORO, Position(5, 5)),
            ChessPiece("alfil_oro_3_4", PieceType.ALFIL, PieceColor.ORO, Position(3, 4)),
            ChessPiece("peon_oro_2_4", PieceType.PEON, PieceColor.ORO, Position(2, 4)),
            ChessPiece("peon_plata_2_5", PieceType.PEON, PieceColor.PLATA, Position(2, 5)),
            ChessPiece("peon_plata_1_4", PieceType.PEON, PieceColor.PLATA, Position(1, 4)),
            ChessPiece("peon_plata_1_3", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
            ChessPiece("caballo_oro_1_2", PieceType.CABALLO, PieceColor.ORO, Position(1, 2)),
            ChessPiece("torre_plata_0_3", PieceType.TORRE, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("rey_plata_0_5", PieceType.REY, PieceColor.PLATA, Position(0, 5))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(5, 5), Position(2, 5))),
            acertijo = "¿Cuánto es 6 x 6 ", respuestaAcertijo = "36", maxPasos = 3),
        46 to NivelConfig(id = 46, size = 5, piezas = listOf(
            ChessPiece("caballo_oro_3_2", PieceType.CABALLO, PieceColor.ORO, Position(3, 2)),
            ChessPiece("caballo_oro_2_3", PieceType.CABALLO, PieceColor.ORO, Position(2, 3)),
            ChessPiece("reina_oro_2_4", PieceType.REINA, PieceColor.ORO, Position(2, 4)),
            ChessPiece("caballo_plata_1_4", PieceType.CABALLO, PieceColor.PLATA, Position(1, 4)),
            ChessPiece("rey_plata_0_3", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("torre_plata_1_2", PieceType.TORRE, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("peon_plata_2_2", PieceType.PEON, PieceColor.PLATA, Position(2, 2)),
            ChessPiece("peon_plata_3_1", PieceType.PEON, PieceColor.PLATA, Position(3, 1))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(2, 4), Position(1, 3))),
            acertijo = "¿Cuánto es 6 x 6 ", respuestaAcertijo = "36", maxPasos = 3),
        47 to NivelConfig(id = 47, size = 6, piezas = listOf(
            ChessPiece("peon_oro_4_1", PieceType.PEON, PieceColor.ORO, Position(4, 1)),
            ChessPiece("caballo_oro_4_3", PieceType.CABALLO, PieceColor.ORO, Position(4, 3)),
            ChessPiece("peon_oro_4_2", PieceType.PEON, PieceColor.ORO, Position(4, 2)),
            ChessPiece("peon_oro_4_4", PieceType.PEON, PieceColor.ORO, Position(4, 4)),
            ChessPiece("alfil_oro_4_5", PieceType.ALFIL, PieceColor.ORO, Position(4, 5)),
            ChessPiece("torre_plata_1_5", PieceType.TORRE, PieceColor.ORO, Position(1, 5)),

            ChessPiece("caballo_plata_3_4", PieceType.CABALLO, PieceColor.PLATA, Position(3, 4)),
            ChessPiece("rey_plata_2_3", PieceType.REY, PieceColor.PLATA, Position(2, 3)),
            ChessPiece("peon_plata_1_3", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
            ChessPiece("peon_plata_2_2", PieceType.PEON, PieceColor.PLATA, Position(2, 2)),
            ChessPiece("peon_plata_1_1", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
            ChessPiece("caballo_plata_0_1", PieceType.CABALLO, PieceColor.PLATA, Position(0, 1)),
            ChessPiece("alfil_plata_0_2", PieceType.ALFIL, PieceColor.PLATA, Position(0, 2))

        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(4, 2), Position(3, 2))),
            acertijo = "¿Cuánto es 6 x 7 ", respuestaAcertijo = "42", maxPasos = 3),

        48 to NivelConfig(id = 48, size = 4, piezas = listOf(
            ChessPiece("reina_oro_3_2", PieceType.REINA, PieceColor.ORO, Position(3, 2)),
            ChessPiece("torre_oro_0_0", PieceType.TORRE, PieceColor.ORO, Position(0, 0)),
            ChessPiece("peon_plata_1_2", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("peon_plata_1_3", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
            ChessPiece("caballo_plata_0_2", PieceType.CABALLO, PieceColor.PLATA, Position(0, 2)),
            ChessPiece("rey_plata_0_3", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("torre_oro_3_1", PieceType.TORRE, PieceColor.ORO, Position(3, 1))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(3, 1), Position(2, 1))),
            acertijo = "¿Cuánto es 7 x 7 ", respuestaAcertijo = "49", maxPasos = 3),
        49 to NivelConfig(id = 49, size = 5, piezas = listOf(
            ChessPiece("peon_oro_4_3", PieceType.PEON, PieceColor.ORO, Position(4, 3)),
            ChessPiece("peon_oro_4_4", PieceType.PEON, PieceColor.ORO, Position(4, 4)),
            ChessPiece("caballo_oro_3_3", PieceType.CABALLO, PieceColor.ORO, Position(3, 3)),
            ChessPiece("torre_oro_1_0", PieceType.TORRE, PieceColor.ORO, Position(1, 0)),
            ChessPiece("alfil_plata_3_1", PieceType.ALFIL, PieceColor.PLATA, Position(3, 1)),
            ChessPiece("peon_plata_2_3", PieceType.PEON, PieceColor.PLATA, Position(2, 3)),
            ChessPiece("torre_plata_0_4", PieceType.TORRE, PieceColor.PLATA, Position(0, 4)),
            ChessPiece("rey_plata_2_4", PieceType.REY, PieceColor.PLATA, Position(2, 4)),
            ChessPiece("reina_oro_1_2", PieceType.REINA, PieceColor.ORO, Position(1, 2))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(1, 2), Position(1, 4))),
            acertijo = "¿Cuánto es 7 x 2 ", respuestaAcertijo = "14", maxPasos = 2),

        50 to NivelConfig(id = 50, size = 5, piezas = listOf(
            ChessPiece("peon_oro_2_3", PieceType.PEON, PieceColor.ORO, Position(2, 3)),
            ChessPiece("torre_oro_4_1", PieceType.TORRE, PieceColor.ORO, Position(4, 1)),
            ChessPiece("alfil_oro_4_0", PieceType.ALFIL, PieceColor.ORO, Position(4, 0)),
            ChessPiece("reina_oro_2_1", PieceType.REINA, PieceColor.ORO, Position(2, 1)),
            ChessPiece("peon_plata_2_2", PieceType.PEON, PieceColor.PLATA, Position(2, 2)),
            ChessPiece("caballo_plata_2_4", PieceType.CABALLO, PieceColor.PLATA, Position(2, 4)),
            ChessPiece("torre_plata_0_4", PieceType.TORRE, PieceColor.PLATA, Position(0, 4)),
            ChessPiece("torre_plata_0_0", PieceType.TORRE, PieceColor.PLATA, Position(0, 0)),
            ChessPiece("rey_plata_0_2", PieceType.REY, PieceColor.PLATA, Position(0, 2))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(2, 1), Position(2, 2))),
            acertijo = "¿Cuánto es 4 x 3 ", respuestaAcertijo = "12", maxPasos = 2),
        51 to NivelConfig(id = 51, size = 5, piezas = listOf(
            ChessPiece("reina_oro_3_0", PieceType.REINA, PieceColor.ORO, Position(3, 0)),
            ChessPiece("alfil_oro_3_2", PieceType.ALFIL, PieceColor.ORO, Position(3, 2)),
            ChessPiece("peon_plata_3_1", PieceType.PEON, PieceColor.PLATA, Position(3, 1)),
            ChessPiece("peon_plata_2_2", PieceType.PEON, PieceColor.PLATA, Position(2, 2)),
            ChessPiece("peon_plata_1_1", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
            ChessPiece("rey_plata_0_1", PieceType.REY, PieceColor.PLATA, Position(0, 1)),
            ChessPiece("torre_plata_0_3", PieceType.TORRE, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("reina_plata_0_4", PieceType.REINA, PieceColor.PLATA, Position(0, 4)),
            ChessPiece("caballo_plata_1_3", PieceType.CABALLO, PieceColor.PLATA, Position(1, 3))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(3, 2), Position(2, 3))),
            acertijo = "¿Cuánto es 9 x 3 ", respuestaAcertijo = "27", maxPasos = 2),
        52 to NivelConfig(id = 52, size = 5, piezas = listOf(
            ChessPiece("torre_oro_4_4", PieceType.TORRE, PieceColor.ORO, Position(4, 4)),
            ChessPiece("alfil_oro_3_0", PieceType.ALFIL, PieceColor.ORO, Position(3, 0)),
            ChessPiece("reina_oro_1_2", PieceType.REINA, PieceColor.ORO, Position(1, 2)),
            ChessPiece("peon_plata_1_3", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
            ChessPiece("caballo_plata_1_4", PieceType.CABALLO, PieceColor.PLATA, Position(1, 4)),
            ChessPiece("rey_plata_0_4", PieceType.REY, PieceColor.PLATA, Position(0, 4)),
            ChessPiece("torre_plata_0_1", PieceType.TORRE, PieceColor.PLATA, Position(0, 1)),
            ChessPiece("reina_plata_0_0", PieceType.REINA, PieceColor.PLATA, Position(0, 0))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(4, 4), Position(1, 4))),
            acertijo = "¿Cuánto es 2 x 9 ", respuestaAcertijo = "18", maxPasos = 2),
        53 to NivelConfig(id = 53, size = 5, piezas = listOf(
            ChessPiece("reina_oro_3_0", PieceType.REINA, PieceColor.ORO, Position(3, 0)),
            ChessPiece("alfil_oro_3_2", PieceType.ALFIL, PieceColor.ORO, Position(3, 2)),
            ChessPiece("peon_plata_3_1", PieceType.PEON, PieceColor.PLATA, Position(3, 1)),
            ChessPiece("peon_plata_2_2", PieceType.PEON, PieceColor.PLATA, Position(2, 2)),
            ChessPiece("peon_plata_1_1", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
            ChessPiece("torre_plata_0_4", PieceType.TORRE, PieceColor.PLATA, Position(0, 4)),
            ChessPiece("torre_plata_0_3", PieceType.TORRE, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("caballo_plata_1_3", PieceType.CABALLO, PieceColor.PLATA, Position(1, 3)),
            ChessPiece("rey_plata_0_1", PieceType.REY, PieceColor.PLATA, Position(0, 1))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(3, 2), Position(2, 3))),
            acertijo = "¿Cuánto es 9 x 9 ", respuestaAcertijo = "81", maxPasos = 2),
        54 to NivelConfig(id = 54, size = 5, piezas = listOf(
            ChessPiece("peon_oro_3_1", PieceType.PEON, PieceColor.ORO, Position(3, 1)),
            ChessPiece("peon_oro_2_0", PieceType.PEON, PieceColor.ORO, Position(2, 0)),
            ChessPiece("peon_oro_2_3", PieceType.PEON, PieceColor.ORO, Position(2, 3)),
            ChessPiece("reina_oro_0_4", PieceType.REINA, PieceColor.ORO, Position(0, 4)),
            ChessPiece("torre_plata_0_3", PieceType.TORRE, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("rey_plata_0_2", PieceType.REY, PieceColor.PLATA, Position(0, 2)),
            ChessPiece("peon_plata_2_1", PieceType.PEON, PieceColor.PLATA, Position(2, 1)),
            ChessPiece("peon_plata_1_0", PieceType.PEON, PieceColor.PLATA, Position(1, 0))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(0, 4), Position(2, 2))),
            acertijo = "¿Cuánto es 9 x 9 + 4 ", respuestaAcertijo = "85", maxPasos = 2),
        55 to NivelConfig(id = 55, size = 5, piezas = listOf(
            ChessPiece("torre_plata_0_2", PieceType.TORRE, PieceColor.PLATA, Position(0, 2)),
            ChessPiece("torre_plata_1_2", PieceType.TORRE, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("rey_plata_0_4", PieceType.REY, PieceColor.PLATA, Position(0, 4)),
            ChessPiece("peon_plata_2_4", PieceType.PEON, PieceColor.PLATA, Position(2, 4)),
            ChessPiece("caballo_oro_2_2", PieceType.CABALLO, PieceColor.ORO, Position(2, 2)),
            ChessPiece("reina_oro_2_3", PieceType.REINA, PieceColor.ORO, Position(2, 3))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(2, 3), Position(1, 4))),
            acertijo = "¿Cuánto es 9 x 3 + 4 ", respuestaAcertijo = "31", maxPasos = 2),
        56 to NivelConfig(id = 56, size = 5, piezas = listOf(
            ChessPiece("alfil_oro_3_0", PieceType.ALFIL, PieceColor.ORO, Position(3, 0)),
            ChessPiece("alfil_oro_3_3", PieceType.ALFIL, PieceColor.ORO, Position(3, 3)),
            ChessPiece("reina_oro_3_4", PieceType.REINA, PieceColor.ORO, Position(3, 4)),
            ChessPiece("peon_plata_1_3", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
            ChessPiece("torre_plata_1_2", PieceType.TORRE, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("rey_plata_0_3", PieceType.REY, PieceColor.PLATA, Position(0, 3))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(3, 4), Position(1, 2))),
            acertijo = "¿Cuánto es 9 x 7 + 7 ", respuestaAcertijo = "70", maxPasos = 2),
        57 to NivelConfig(id = 57, size = 5, piezas = listOf(
            ChessPiece("peon_oro_2_0", PieceType.PEON, PieceColor.ORO, Position(2, 0)),
            ChessPiece("alfil_oro_2_2", PieceType.ALFIL, PieceColor.ORO, Position(2, 2)),
            ChessPiece("caballo_oro_3_0", PieceType.CABALLO, PieceColor.ORO, Position(3, 0)),
            ChessPiece("reina_oro_3_2", PieceType.REINA, PieceColor.ORO, Position(3, 2)),
            ChessPiece("peon_plata_1_2", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("peon_plata_1_3", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
            ChessPiece("torre_plata_1_0", PieceType.TORRE, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("torre_plata_0_3", PieceType.TORRE, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("alfil_plata_0_1", PieceType.ALFIL, PieceColor.PLATA, Position(0, 1)),
            ChessPiece("rey_plata_0_2", PieceType.REY, PieceColor.PLATA, Position(0, 2))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(2, 2), Position(1, 1))),
            acertijo = "¿Cuánto es 9 x 6 + 6 ", respuestaAcertijo = "60", maxPasos = 2),

        58 to NivelConfig(id = 58, size = 6, piezas = listOf(
            ChessPiece("reina_oro_3_5", PieceType.REINA, PieceColor.ORO, Position(3, 5)),
            ChessPiece("alfil_oro_2_5", PieceType.ALFIL, PieceColor.ORO, Position(2, 5)),
            ChessPiece("torre_oro_2_3", PieceType.TORRE, PieceColor.ORO, Position(2, 3)),
            ChessPiece("caballo_plata_2_4", PieceType.CABALLO, PieceColor.PLATA, Position(2, 4)),
            ChessPiece("peon_plata_1_5", PieceType.PEON, PieceColor.PLATA, Position(1, 5)),
            ChessPiece("peon_plata_1_0", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("peon_plata_2_1", PieceType.PEON, PieceColor.PLATA, Position(2, 1)),
            ChessPiece("torre_plata_0_5", PieceType.TORRE, PieceColor.PLATA, Position(0, 5)),
            ChessPiece("alfil_plata_0_3", PieceType.ALFIL, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("alfil_plata_0_0", PieceType.ALFIL, PieceColor.PLATA, Position(0, 0)),
            ChessPiece("rey_plata_0_4", PieceType.REY, PieceColor.PLATA, Position(0, 4))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(3, 5), Position(3, 1))),
            acertijo = "¿Cuánto es 9 x 5 + 5 ", respuestaAcertijo = "50", maxPasos = 2),
        59 to NivelConfig(id = 59, size = 6, piezas = listOf(
            ChessPiece("peon_oro_3_4", PieceType.PEON, PieceColor.ORO, Position(3, 4)),
            ChessPiece("reina_oro_2_4", PieceType.REINA, PieceColor.ORO, Position(2, 4)),
            ChessPiece("peon_oro_2_2", PieceType.PEON, PieceColor.ORO, Position(2, 2)),
            ChessPiece("peon_plata_1_2", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("peon_plata_1_0", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("reina_oro_0_3", PieceType.REINA, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("rey_plata_0_1", PieceType.REY, PieceColor.PLATA, Position(0, 1))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(2, 4), Position(5, 2))),
            acertijo = "¿Cuánto es 9 x 4 + 4 ", respuestaAcertijo = "40", maxPasos = 2),

        60 to NivelConfig(id = 60, size = 6, piezas = listOf(
            ChessPiece("rey_plata_0_1", PieceType.REY, PieceColor.PLATA, Position(0, 1)),
            ChessPiece("peon_plata_1_2", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("peon_plata_2_3", PieceType.PEON, PieceColor.PLATA, Position(2, 3)),
            ChessPiece("torre_plata_0_4", PieceType.TORRE, PieceColor.PLATA, Position(0, 4)),
            ChessPiece("reina_plata_2_4", PieceType.REINA, PieceColor.PLATA, Position(2, 4)),
            ChessPiece("alfil_oro_3_2", PieceType.ALFIL, PieceColor.ORO, Position(3, 2)),
            ChessPiece("reina_oro_2_0", PieceType.REINA, PieceColor.ORO, Position(2, 0))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(3, 2), Position(1, 0))),
            acertijo = "¿Cuánto es 9 x 3 + 3 ", respuestaAcertijo = "30", maxPasos = 4),

        61 to NivelConfig(id = 61, size = 5, piezas = listOf(
            ChessPiece("peon_plata_3_3", PieceType.PEON, PieceColor.ORO, Position(3, 3)),
            ChessPiece("caballo_plata_2_2", PieceType.CABALLO, PieceColor.ORO, Position(2, 2)),
            ChessPiece("caballo_plata_4_4", PieceType.CABALLO, PieceColor.ORO, Position(4, 4)),
            ChessPiece("torre_plata_4_0", PieceType.TORRE, PieceColor.ORO, Position(4, 0)),
            ChessPiece("peon_oro_3_1", PieceType.PEON, PieceColor.PLATA, Position(3, 1)),
            ChessPiece("peon_oro_3_2", PieceType.PEON, PieceColor.PLATA, Position(3, 2)),
            ChessPiece("peon_oro_1_4", PieceType.PEON, PieceColor.PLATA, Position(1, 4)),
            ChessPiece("peon_oro_1_3", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
            ChessPiece("alfil_oro_0_2", PieceType.ALFIL, PieceColor.PLATA, Position(0, 2)),
            ChessPiece("alfil_oro_2_1", PieceType.ALFIL, PieceColor.PLATA, Position(2, 1)),
            ChessPiece("rey_oro_0_4", PieceType.REY, PieceColor.PLATA, Position(0, 4))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(4, 4), Position(2, 3))),
            acertijo = "¿Cuánto es 8 x 3 + 3 ", respuestaAcertijo = "27", maxPasos = 2),

        62 to NivelConfig(id = 62, size = 5, piezas = listOf(
            ChessPiece("torre_oro_1_0", PieceType.TORRE, PieceColor.ORO, Position(1, 0)),
            ChessPiece("caballo_oro_3_0", PieceType.CABALLO, PieceColor.ORO, Position(3, 0)),
            ChessPiece("alfil_oro_3_1", PieceType.ALFIL, PieceColor.ORO, Position(3, 1)),
            ChessPiece("peon_plata_1_4", PieceType.PEON, PieceColor.PLATA, Position(1, 4)),
            ChessPiece("peon_plata_2_3", PieceType.PEON, PieceColor.PLATA, Position(2, 3)),
            ChessPiece("alfil_plata_3_2", PieceType.ALFIL, PieceColor.PLATA, Position(3, 2)),
            ChessPiece("caballo_plata_2_4", PieceType.CABALLO, PieceColor.PLATA, Position(2, 4)),
            ChessPiece("reina_plata_3_4", PieceType.REINA, PieceColor.PLATA, Position(3, 4)),
            ChessPiece("rey_plata_0_3", PieceType.REY, PieceColor.PLATA, Position(0, 3))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(3, 0), Position(2, 2))),
            acertijo = "¿Cuánto es 8 x 4 + 3 ", respuestaAcertijo = "35", maxPasos = 2),

        63 to NivelConfig(id = 63, size = 6, piezas = listOf(
            ChessPiece("peon_oro_5_5", PieceType.PEON, PieceColor.ORO, Position(5, 5)),
            ChessPiece("peon_oro_5_4", PieceType.PEON, PieceColor.ORO, Position(5, 4)),
            ChessPiece("alfil_oro_5_3", PieceType.ALFIL, PieceColor.ORO, Position(5, 3)),
            ChessPiece("reina_oro_1_2", PieceType.REINA, PieceColor.ORO, Position(1, 2)),
            ChessPiece("peon_plata_1_5", PieceType.PEON, PieceColor.PLATA, Position(1, 5)),
            ChessPiece("peon_plata_2_4", PieceType.PEON, PieceColor.PLATA, Position(2, 4)),
            ChessPiece("peon_plata_3_2", PieceType.PEON, PieceColor.PLATA, Position(3, 2)),
            ChessPiece("alfil_plata_4_1", PieceType.ALFIL, PieceColor.PLATA, Position(4, 1)),
            ChessPiece("rey_plata_2_5", PieceType.REY, PieceColor.PLATA, Position(2, 5))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(1, 2), Position(0, 3))),
            acertijo = "¿Cuánto es 4 x 3 + 3 ", respuestaAcertijo = "15", maxPasos = 2),

        64 to NivelConfig(id = 64, size = 4, piezas = listOf(
            ChessPiece("rey_plata_0_0", PieceType.REY, PieceColor.PLATA, Position(0, 0)),
            ChessPiece("peon_oro_3_1", PieceType.PEON, PieceColor.ORO, Position(3, 1)),
            ChessPiece("peon_oro_2_2", PieceType.PEON, PieceColor.ORO, Position(2, 2)),
            ChessPiece("alfil_oro_2_0", PieceType.ALFIL, PieceColor.ORO, Position(2, 0)),
            ChessPiece("alfil_oro_1_2", PieceType.ALFIL, PieceColor.ORO, Position(1, 2))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(2, 0), Position(1, 1))),
            acertijo = "¿Cuánto es 6 x 3 + 8 ", respuestaAcertijo = "26", maxPasos = 2),
        65 to NivelConfig(id = 65, size = 5, piezas = listOf(
            ChessPiece("torre_oro_0_4", PieceType.TORRE, PieceColor.ORO, Position(0, 4)),
            ChessPiece("torre_oro_1_4", PieceType.TORRE, PieceColor.ORO, Position(1, 4)),
            ChessPiece("peon_oro_2_2", PieceType.PEON, PieceColor.ORO, Position(2, 2)),
            ChessPiece("peon_plata_1_0", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("peon_plata_1_2", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("peon_plata_2_1", PieceType.PEON, PieceColor.PLATA, Position(2, 1)),
            ChessPiece("peon_plata_2_3", PieceType.PEON, PieceColor.PLATA, Position(2, 3)),
            ChessPiece("alfil_plata_4_4", PieceType.ALFIL, PieceColor.PLATA, Position(4, 4)),
            ChessPiece("reina_plata_3_3", PieceType.REINA, PieceColor.PLATA, Position(3, 3)),
            ChessPiece("rey_plata_0_0", PieceType.REY, PieceColor.PLATA, Position(0, 0)),
            ChessPiece("torre_plata_0_1", PieceType.TORRE, PieceColor.PLATA, Position(0, 1))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(0, 4), Position(0, 1))),
            acertijo = "¿Cuánto es 6 x 6 + 9 ", respuestaAcertijo = "45", maxPasos = 2),
        66 to NivelConfig(id = 66, size = 5, piezas = listOf(
            ChessPiece("reina_plata_4_1", PieceType.REINA, PieceColor.PLATA, Position(4, 1)),
            ChessPiece("peon_plata_3_4", PieceType.PEON, PieceColor.PLATA, Position(3, 4)),
            ChessPiece("peon_plata_2_3", PieceType.PEON, PieceColor.PLATA, Position(2, 3)),
            ChessPiece("peon_plata_1_2", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("rey_plata_1_3", PieceType.REY, PieceColor.PLATA, Position(1, 3)),
            ChessPiece("alfil_oro_3_3", PieceType.ALFIL, PieceColor.ORO, Position(3, 3)),
            ChessPiece("reina_oro_0_0", PieceType.REINA, PieceColor.ORO, Position(0, 0))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(3, 3), Position(2, 2))),
            acertijo = "¿Cuánto es 7 x 6 - 7 ", respuestaAcertijo = "35", maxPasos = 2),

        67 to NivelConfig(id = 67, size = 6, piezas = listOf(
            ChessPiece("caballo_plata_0_0", PieceType.CABALLO, PieceColor.PLATA, Position(0, 0)),
            ChessPiece("alfil_plata_0_1", PieceType.ALFIL, PieceColor.PLATA, Position(0, 1)),
            ChessPiece("peon_plata_1_0", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("peon_plata_1_1", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
            ChessPiece("peon_plata_3_3", PieceType.PEON, PieceColor.PLATA, Position(3, 3)),
            ChessPiece("rey_plata_2_2", PieceType.REY, PieceColor.PLATA, Position(2, 2)),
            ChessPiece("peon_oro_3_2", PieceType.PEON, PieceColor.ORO, Position(3, 2)),
            ChessPiece("alfil_oro_3_0", PieceType.ALFIL, PieceColor.ORO, Position(3, 0)),
            ChessPiece("alfil_oro_4_4", PieceType.ALFIL, PieceColor.ORO, Position(4, 4)),
            ChessPiece("caballo_oro_5_1", PieceType.CABALLO, PieceColor.ORO, Position(5, 1)),
            ChessPiece("reina_oro_3_5", PieceType.REINA, PieceColor.ORO, Position(3, 5))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(3, 3), Position(2, 2))),
            acertijo = "¿Cuánto es 6 x 6 - 7 ", respuestaAcertijo = "43", maxPasos = 2),

        68 to NivelConfig(id = 68, size = 6, piezas = listOf(
            ChessPiece("reina_oro_5_3", PieceType.REINA, PieceColor.ORO, Position(5, 3)),
            ChessPiece("peon_oro_2_1", PieceType.PEON, PieceColor.ORO, Position(2, 1)),
            ChessPiece("alfil_oro_1_3", PieceType.ALFIL, PieceColor.ORO, Position(1, 3)),
            ChessPiece("peon_plata_1_1", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
            ChessPiece("peon_plata_3_0", PieceType.PEON, PieceColor.PLATA, Position(3, 0)),
            ChessPiece("rey_plata_0_0", PieceType.REY, PieceColor.PLATA, Position(0, 0)),
            ChessPiece("alfil_plata_0_1", PieceType.ALFIL, PieceColor.PLATA, Position(0, 1))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(5, 3), Position(2, 0))),
            acertijo = "¿Cuánto es 6 x 9 - 7 ", respuestaAcertijo = "47", maxPasos = 2),

        69 to NivelConfig(id = 69, size = 4, piezas = listOf(
            ChessPiece("rey_plata_0_0", PieceType.REY, PieceColor.PLATA, Position(0, 0)),
            ChessPiece("peon_plata_1_1", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
            ChessPiece("torre_plata_1_2", PieceType.TORRE, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("caballo_plata_2_2", PieceType.CABALLO, PieceColor.PLATA, Position(2, 2)),
            ChessPiece("caballo_oro_0_2", PieceType.CABALLO, PieceColor.ORO, Position(0, 2)),
            ChessPiece("caballo_oro_1_3", PieceType.CABALLO, PieceColor.ORO, Position(1, 3)),
            ChessPiece("reina_oro_3_2", PieceType.REINA, PieceColor.ORO, Position(3, 2))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(5, 3), Position(2, 0))),
            acertijo = "¿Cuánto es 8 x 9 - 5 ", respuestaAcertijo = "67", maxPasos = 2),

        70 to NivelConfig(id = 70, size = 4, piezas = listOf(
            ChessPiece("reina_oro_2_0", PieceType.REINA, PieceColor.ORO, Position(2, 0)),
            ChessPiece("caballo_oro_1_1", PieceType.CABALLO, PieceColor.ORO, Position(1, 1)),
            ChessPiece("caballo_oro_3_2", PieceType.CABALLO, PieceColor.ORO, Position(3, 2)),
            ChessPiece("caballo_plata_2_1", PieceType.CABALLO, PieceColor.PLATA, Position(2, 1)),
            ChessPiece("alfil_plata_1_2", PieceType.ALFIL, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("rey_plata_0_2", PieceType.REY, PieceColor.PLATA, Position(0, 2))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(1, 1), Position(2, 3))),
            acertijo = "¿Cuánto es 9 x 9 - 6 ", respuestaAcertijo = "75", maxPasos = 2),

        71 to NivelConfig(id = 71, size = 5, piezas = listOf(
            ChessPiece("reina_oro_4_0", PieceType.REINA, PieceColor.ORO, Position(4, 0)),
            ChessPiece("alfil_oro_4_2", PieceType.ALFIL, PieceColor.ORO, Position(4, 2)),
            ChessPiece("alfil_oro_4_3", PieceType.ALFIL, PieceColor.ORO, Position(4, 3)),
            ChessPiece("peon_plata_2_2", PieceType.PEON, PieceColor.PLATA, Position(2, 2)),
            ChessPiece("peon_plata_2_3", PieceType.PEON, PieceColor.PLATA, Position(2, 3)),
            ChessPiece("peon_plata_1_2", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("torre_plata_0_3", PieceType.TORRE, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("rey_plata_0_1", PieceType.REY, PieceColor.PLATA, Position(0, 1))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(4, 0), Position(2, 2))),
            acertijo = "¿Cuánto es 9 x 9 - 6 ", respuestaAcertijo = "75", maxPasos = 3),

        72 to NivelConfig(id = 72, size = 6, piezas = listOf(
            ChessPiece("torre_oro_5_4", PieceType.TORRE, PieceColor.ORO, Position(5, 4)),
            ChessPiece("torre_oro_5_2", PieceType.TORRE, PieceColor.ORO, Position(5, 2)),
            ChessPiece("caballo_oro_3_0", PieceType.CABALLO, PieceColor.ORO, Position(3, 0)),
            ChessPiece("reina_oro_2_2", PieceType.REINA, PieceColor.ORO, Position(2, 2)),
            ChessPiece("peon_plata_2_0", PieceType.PEON, PieceColor.PLATA, Position(2, 0)),
            ChessPiece("peon_plata_2_1", PieceType.PEON, PieceColor.PLATA, Position(2, 1)),
            ChessPiece("peon_plata_1_2", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("peon_plata_3_3", PieceType.PEON, PieceColor.PLATA, Position(3, 3)),
            ChessPiece("torre_plata_0_1", PieceType.TORRE, PieceColor.PLATA, Position(0, 1)),
            ChessPiece("torre_plata_1_3", PieceType.TORRE, PieceColor.PLATA, Position(1, 3)),
            ChessPiece("alfil_plata_3_2", PieceType.ALFIL, PieceColor.PLATA, Position(3, 2)),
            ChessPiece("rey_plata_1_0", PieceType.REY, PieceColor.PLATA, Position(1, 0))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(2, 2), Position(1, 1))),
            acertijo = "¿Cuánto es 9 x 6 - 8 ", respuestaAcertijo = "46", maxPasos = 5),
        73 to NivelConfig(id = 73, size = 6, piezas = listOf(
            ChessPiece("torre_oro_5_1", PieceType.TORRE, PieceColor.ORO, Position(5, 1)),
            ChessPiece("torre_oro_4_3", PieceType.TORRE, PieceColor.ORO, Position(4, 3)),
            ChessPiece("reina_oro_4_0", PieceType.REINA, PieceColor.ORO, Position(4, 0)),
            ChessPiece("rey_plata_1_0", PieceType.REY, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("alfil_plata_2_0", PieceType.ALFIL, PieceColor.PLATA, Position(2, 0)),
            ChessPiece("peon_plata_2_3", PieceType.PEON, PieceColor.PLATA, Position(2, 3)),
            ChessPiece("peon_plata_1_2", PieceType.PEON, PieceColor.PLATA, Position(1, 2))
        ), turnoInicial = PieceColor.ORO,
            listOf(MovimientoSolucion(Position(4, 0), Position(2, 0))),
            acertijo = "¿Cuánto es 7 x 9 - 7 ", respuestaAcertijo = "56", maxPasos = 2),

    )

fun generarSetupPorDefecto(size: Int): List<ChessPiece> {
    return if (size == 8) {
        val pieces = mutableListOf<ChessPiece>()
        // Fila 0 y 1 (Plata)
        pieces.add(ChessPiece("t_plata_1", PieceType.TORRE, PieceColor.PLATA, Position(0, 0)))
        pieces.add(ChessPiece("c_plata_1", PieceType.CABALLO, PieceColor.PLATA, Position(0, 1)))
        // ... (tu lógica de setup clásico)
        pieces
    } else {
        generarSetupProfesional(size)
    }
}

fun generarSetupProfesional(size: Int): List<ChessPiece> {
    val piezas = mutableListOf<ChessPiece>()
    val mitad = size / 2
    listOf(PieceColor.PLATA to 0, PieceColor.ORO to size - 1).forEach { (color, fila) ->
        piezas.add(ChessPiece("k_${color}_0", PieceType.REY, color, Position(fila, mitad)))
        piezas.add(ChessPiece("q_${color}_0", PieceType.REINA, color, Position(fila, mitad - 1)))
        piezas.add(ChessPiece("t_${color}_0", PieceType.TORRE, color, Position(fila, 0)))
        piezas.add(ChessPiece("t_${color}_1", PieceType.TORRE, color, Position(fila, size - 1)))
    }
    for (col in 0 until size) {
        piezas.add(ChessPiece("p_plata_$col", PieceType.PEON, PieceColor.PLATA, Position(1, col)))
        piezas.add(ChessPiece("p_oro_$col", PieceType.PEON, PieceColor.ORO, Position(size - 2, col)))
    }
    return piezas
}

fun guardarNivel(config: NivelConfig) {
    totalNiveles[config.id] = config
}

fun obtenerNivel(id: Int): NivelConfig? {
    return totalNiveles[id]
}
}
