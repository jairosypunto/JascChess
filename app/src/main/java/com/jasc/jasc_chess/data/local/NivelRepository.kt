package com.jasc.jasc_chess.data.local

import com.jasc.jasc_chess.model.*

data class NivelConfig(
    val id: Int,
    val size: Int,
    val piezas: List<ChessPiece>,
    val turnoInicial: PieceColor,
    val secuenciaSolucion: List<MovimientoSolucion>,
    val acertijo: String,
    val respuestaAcertijo: String,
    val maxPasos: Int,
    val mateEnJugadas: Int = 1,
    val mensajeTactico: String = "Prepárate para la batalla"
)

object NivelRepository {

    val totalNiveles = mutableMapOf<Int, NivelConfig>(
            1 to NivelConfig(1, 4, listOf(
                ChessPiece("n1_k", PieceType.REY, PieceColor.PLATA, Position(0, 1)),
                ChessPiece("n1_a", PieceType.ALFIL, PieceColor.PLATA, Position(1, 2)),
                ChessPiece("n1_q", PieceType.REINA, PieceColor.PLATA, Position(2, 0)),
                ChessPiece("n1_p", PieceType.PEON, PieceColor.PLATA, Position(3, 1)),
                ChessPiece("n1_p", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
                ChessPiece("n1_q_oro", PieceType.REINA, PieceColor.ORO, Position(2, 2)),
                ChessPiece("n3_c_oro", PieceType.CABALLO, PieceColor.ORO, Position(3, 2))
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(3, 2), Position(2, 0))),
                "¿Cómo se llama la pieza que salta?", "caballo", 2, 1, "Busca el salto ganador"),

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
                "¿Cuántas casillas tiene un tablero?", "64", 2, 1, "Ataca con precisión"),

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
                "¿Cuántas casillas tiene un tablero?", "64", 2, 1, "Controla el centro"),

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
                "¿Cuánto es 9x9 ?", "81", 2, 1, "La estrategia es clave"),

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
                "¿Cuánto es 9x8 ?", "72", 2, 1, "Avanza hacia la victoria"),

            6 to NivelConfig(6, 4, listOf(
                ChessPiece("n1_k", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
                ChessPiece("n1_c", PieceType.CABALLO, PieceColor.PLATA, Position(2, 1)),
                ChessPiece("n1_q_oro", PieceType.REINA, PieceColor.ORO, Position(3, 0)),
                ChessPiece("n1_c_oro", PieceType.CABALLO, PieceColor.ORO, Position(3, 3))
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(3, 0), Position(2, 1))),
                "¿Cómo se llama la pieza que salta?", "caballo", 2, 1, "Sorprende con tu caballo"),

            7 to NivelConfig(7, 4, listOf(
                ChessPiece("n1_k", PieceType.REY, PieceColor.PLATA, Position(0, 0)),
                ChessPiece("n1_t", PieceType.TORRE, PieceColor.PLATA, Position(0, 2)),
                ChessPiece("n1_P", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
                ChessPiece("n1_P", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
                ChessPiece("n1_c_oro", PieceType.CABALLO, PieceColor.ORO, Position(2, 0)),
                ChessPiece("n1_q_oro", PieceType.REINA, PieceColor.ORO, Position(2, 3))
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(2, 3), Position(0, 1))),
                "¿Cuánto es 5 x 9 ?", "45", 2, 1, "Mantén la posición"),

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
                "¿Cuánto es 9 x 3 ?", "27", 2, 1, "Calcula cada paso"),

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
                "¿Cuánto es 5 x 5 ?", "25", 2, 1, "Ataque directo"),

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
                "¿Cuánto es 4 x 8  ?", "32", 2, 1, "Domina el tablero"),

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
                "¿Cuánto es 6 x 7 ?", "42", 2, 1, "Prepárate para la batalla"),

            12 to NivelConfig(12, 4, listOf(
                ChessPiece("n2_t_oro", PieceType.TORRE, PieceColor.ORO, Position(3, 1)),
                ChessPiece("n1_q_oro", PieceType.REINA, PieceColor.ORO, Position(2, 0)),
                ChessPiece("n2_k", PieceType.REY, PieceColor.PLATA, Position(0, 0)),
                ChessPiece("n2_t", PieceType.TORRE, PieceColor.PLATA, Position(0, 1)),
                ChessPiece("n1_p", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
                ChessPiece("n2_t", PieceType.REINA, PieceColor.PLATA, Position(2, 2))
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(2, 0), Position(2, 2))),
                "¿Cuánto es 7 x 9 ?", "63", 2, 1, "Ejecuta con firmeza"),

            13 to NivelConfig(13, 5, listOf(
                ChessPiece("n2_k", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
                ChessPiece("n1_a", PieceType.ALFIL, PieceColor.PLATA, Position(2, 2)),
                ChessPiece("n1_p1", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
                ChessPiece("n2_t2", PieceType.TORRE, PieceColor.PLATA, Position(0, 4)),
                ChessPiece("n2_q", PieceType.REINA, PieceColor.ORO, Position(4, 3)),
                ChessPiece("n1_c_oro", PieceType.CABALLO, PieceColor.ORO, Position(3, 3))
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(4, 3), Position(2, 1))),
                "¿Cuanto es 5 x 8 ?", "40", 2, 1, "Piensa el siguiente paso"),

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
                "¿Cómo se llama la pieza más importante que se mueve por todo el tablero?", "reina", 2, 1, "La reina domina hoy"),

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
                "¿Cómo se llama la pieza que salta?", "caballo", 2, 1, "El salto es decisivo"),

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
                "¿Cuánto es 9 x 5 ?", "45", 2, 1, "Calcula la posición"),

            17 to NivelConfig(17, 4, listOf(
                ChessPiece("p_1_2_1780876623237", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
                ChessPiece("p_2_1_1780876625924", PieceType.ALFIL, PieceColor.PLATA, Position(2, 1)),
                ChessPiece("p_3_2_1780876633828", PieceType.CABALLO, PieceColor.ORO, Position(3, 2)),
                ChessPiece("p_3_1_1780876641967", PieceType.REINA, PieceColor.ORO, Position(3, 1)),
                ChessPiece("p_0_3_1780876660724", PieceType.TORRE, PieceColor.PLATA, Position(0, 3)),
                ChessPiece("p_0_2_1780876668438", PieceType.REY, PieceColor.PLATA, Position(0, 2))
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(3, 1), Position(2, 0))),
                "¿Cuánto es 6 x 6?", "36", 2, 1, "Mantén la concentración"),

            18 to NivelConfig(18, 6, listOf(
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
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(3, 5), Position(3, 1))),
                "¿Cuánto es 3 x 8 ?", "24", 2, 1, "Despliega tus piezas"),

            19 to NivelConfig(19, 6, listOf(
                ChessPiece("rey_plata_0_1", PieceType.REY, PieceColor.PLATA, Position(0, 1)),
                ChessPiece("peon_plata_1_0", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
                ChessPiece("peon_plata_1_2", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
                ChessPiece("reina_plata_0_3", PieceType.REINA, PieceColor.PLATA, Position(0, 3)),
                ChessPiece("peon_oro_2_2", PieceType.PEON, PieceColor.ORO, Position(2, 2)),
                ChessPiece("peon_oro_3_4", PieceType.PEON, PieceColor.ORO, Position(3, 4)),
                ChessPiece("reina_oro_2_4", PieceType.REINA, PieceColor.ORO, Position(2, 4))
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(2, 4), Position(5, 1))),
                "¿Cuánto es 7 x 5 ?", "35", 2, 1, "Observa el ataque"),

            20 to NivelConfig(20, 4, listOf(
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
                "¿Cuántas casillas tiene un tablero?", "64", 2, 1, "La defensa es sólida"),

            21 to NivelConfig(21, 5, listOf(
                ChessPiece("peon_plata_1_0", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
                ChessPiece("peon_plata_1_1", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
                ChessPiece("peon_plata_1_2", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
                ChessPiece("rey_plata_0_0", PieceType.REY, PieceColor.PLATA, Position(0, 0)),
                ChessPiece("torre_plata_0_2", PieceType.TORRE, PieceColor.PLATA, Position(0, 2)),
                ChessPiece("reina_plata_4_4", PieceType.REINA, PieceColor.PLATA, Position(4, 4)),
                ChessPiece("torre_oro_3_3", PieceType.TORRE, PieceColor.ORO, Position(3, 3)),
                ChessPiece("reina_oro_4_3", PieceType.REINA, PieceColor.ORO, Position(4, 3)),
                ChessPiece("caballo_oro_1_3", PieceType.CABALLO, PieceColor.ORO, Position(1, 3))
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(4, 3), Position(1, 0))),
                "cuantas piezas negras trae un ajedrez", "16", 2, 1, "Presiona al rival"),

            22 to NivelConfig(22, 5, listOf(
                ChessPiece("rey_plata_0_2", PieceType.REY, PieceColor.PLATA, Position(0, 2)),
                ChessPiece("torre_plata_1_1", PieceType.TORRE, PieceColor.PLATA, Position(1, 1)),
                ChessPiece("caballo_plata_1_3", PieceType.CABALLO, PieceColor.PLATA, Position(1, 3)),
                ChessPiece("peon_plata_2_1", PieceType.PEON, PieceColor.PLATA, Position(2, 1)),
                ChessPiece("peon_plata_3_0", PieceType.PEON, PieceColor.PLATA, Position(3, 0)),
                ChessPiece("caballo_oro_3_1", PieceType.CABALLO, PieceColor.ORO, Position(3, 1)),
                ChessPiece("caballo_oro_2_2", PieceType.CABALLO, PieceColor.ORO, Position(2, 2)),
                ChessPiece("reina_oro_2_3", PieceType.REINA, PieceColor.ORO, Position(2, 3))
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(2, 3), Position(1, 4))),
                "cuantas piezas trae un tablero de ajedrez", "64", 2, 1, "Control total"),

            23 to NivelConfig(23, 5, listOf(
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
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(3, 1), Position(2, 2))),
                "¿Cuánto es 7x4", "28", 2, 1, "La mente en el objetivo"),

            24 to NivelConfig(24, 5, listOf(
                ChessPiece("torre_oro_0_1", PieceType.TORRE, PieceColor.ORO, Position(0, 2)),
                ChessPiece("torre_oro_4_3", PieceType.TORRE, PieceColor.ORO, Position(4, 3)),
                ChessPiece("caballo_oro_2_1", PieceType.CABALLO, PieceColor.ORO, Position(2, 2)),
                ChessPiece("alfil_oro_2_3", PieceType.ALFIL, PieceColor.ORO, Position(2, 4)),
                ChessPiece("rey_plata_0_3", PieceType.REY, PieceColor.PLATA, Position(0, 4)),
                ChessPiece("caballo_plata_0_2", PieceType.CABALLO, PieceColor.PLATA, Position(0, 3)),
                ChessPiece("torre_plata_0_0", PieceType.TORRE, PieceColor.PLATA, Position(0, 1))
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(0, 2), Position(0, 3))),
                "¿Cuanto es 9 x 9 - 3", "78", 2, 1, "Un movimiento inteligente"),

            25 to NivelConfig(25, 4, listOf(
                ChessPiece("torre_plata_0_0", PieceType.TORRE, PieceColor.PLATA, Position(0, 0)),
                ChessPiece("peon_plata_1_1", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
                ChessPiece("peon_plata_2_2", PieceType.PEON, PieceColor.PLATA, Position(2, 2)),
                ChessPiece("alfil_plata_1_0", PieceType.ALFIL, PieceColor.PLATA, Position(1, 0)),
                ChessPiece("caballo_oro_0_1", PieceType.CABALLO, PieceColor.ORO, Position(0, 1)),
                ChessPiece("rey_plata_0_2", PieceType.REY, PieceColor.PLATA, Position(0, 2)),
                ChessPiece("reina_oro_3_3", PieceType.REINA, PieceColor.ORO, Position(3, 3))
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(3, 3), Position(1, 3))),
                "cuantas es 8 x 9 + 3", "75", 2, 1, "La victoria está cerca"),

            26 to NivelConfig(26, 5, listOf(
                ChessPiece("reina_plata_4_4", PieceType.REINA, PieceColor.ORO, Position(4, 4)),
                ChessPiece("alfil_plata_3_4", PieceType.ALFIL, PieceColor.ORO, Position(3, 4)),
                ChessPiece("peon_plata_2_0", PieceType.PEON, PieceColor.ORO, Position(2, 0)),
                ChessPiece("peon_oro_1_0", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
                ChessPiece("peon_oro_2_3", PieceType.PEON, PieceColor.PLATA, Position(2, 3)),
                ChessPiece("reina_oro_1_3", PieceType.REINA, PieceColor.PLATA, Position(1, 3)),
                ChessPiece("torre_oro_0_3", PieceType.TORRE, PieceColor.PLATA, Position(0, 3)),
                ChessPiece("rey_oro_0_1", PieceType.REY, PieceColor.PLATA, Position(0, 1))
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(3, 4), Position(2, 3))),
                "¿Cuánto es 8 x 9 + 4", "76", 2, 1, "Ataque coordinado"),

            27 to NivelConfig(27, 6, listOf(
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
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(5, 3), Position(2, 3))),
                "¿Cuánto es 8 x 8 + 10 ?", "74", 2, 1, "La reina domina"),

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
                "¿Cuánto es 8x8 ?", "64", 2, 1, "Salto táctico"),

            29 to NivelConfig(29, 5, listOf(
                ChessPiece("caballo_oro_1_1", PieceType.CABALLO, PieceColor.ORO, Position(1, 1)),
                ChessPiece("alfil_oro_4_1", PieceType.ALFIL, PieceColor.ORO, Position(4, 1)),
                ChessPiece("torre_plata_3_2", PieceType.TORRE, PieceColor.PLATA, Position(3, 2)),
                ChessPiece("peon_plata_2_4", PieceType.PEON, PieceColor.PLATA, Position(2, 4)),
                ChessPiece("rey_plata_1_4", PieceType.REY, PieceColor.PLATA, Position(1, 4)),
                ChessPiece("alfil_plata_1_3", PieceType.ALFIL, PieceColor.PLATA, Position(1, 3)),
                ChessPiece("torre_plata_0_3", PieceType.TORRE, PieceColor.PLATA, Position(0, 3))
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(4, 1), Position(3, 2))),
                "¿Cuánto es 4 x 9 - 2 ", "34", 4, 1, "Ataque preciso"),

            30 to NivelConfig(30, 5, listOf(
                ChessPiece("torre_oro_3_2", PieceType.TORRE, PieceColor.ORO, Position(3, 2)),
                ChessPiece("reina_oro_1_3", PieceType.REINA, PieceColor.ORO, Position(1, 3)),
                ChessPiece("caballo_oro_3_1", PieceType.CABALLO, PieceColor.ORO, Position(3, 1)),
                ChessPiece("reina_plata_4_1", PieceType.REINA, PieceColor.PLATA, Position(4, 1)),
                ChessPiece("alfil_plata_1_1", PieceType.ALFIL, PieceColor.PLATA, Position(1, 1)),
                ChessPiece("rey_plata_0_1", PieceType.REY, PieceColor.PLATA, Position(0, 1)),
                ChessPiece("caballo_plata_2_0", PieceType.CABALLO, PieceColor.PLATA, Position(2, 0)),
                ChessPiece("caballo_plata_0_4", PieceType.CABALLO, PieceColor.PLATA, Position(0, 4))
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(3, 2), Position(0, 2))),
                "Qué ciudad es nombrada la eterna primavera?", "Medellín", 2, 1, "Movimiento certero"),

            31 to NivelConfig(31, 5, listOf(
                ChessPiece("peon_plata_1_0", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
                ChessPiece("peon_plata_1_1", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
                ChessPiece("peon_plata_1_2", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
                ChessPiece("rey_plata_0_0", PieceType.REY, PieceColor.PLATA, Position(0, 0)),
                ChessPiece("torre_plata_0_2", PieceType.TORRE, PieceColor.PLATA, Position(0, 2)),
                ChessPiece("reina_plata_4_4", PieceType.REINA, PieceColor.PLATA, Position(4, 4)),
                ChessPiece("reina_oro_4_3", PieceType.REINA, PieceColor.ORO, Position(4, 3)),
                ChessPiece("torre_oro_3_3", PieceType.TORRE, PieceColor.ORO, Position(3, 3)),
                ChessPiece("caballo_oro_1_3", PieceType.CABALLO, PieceColor.ORO, Position(1, 3))
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(4, 3), Position(1, 0))),
                "ajedrez en ingles", "chess", 2, 1, "Controla el espacio"),

            32 to NivelConfig(32, 4, listOf(
                ChessPiece("caballo_oro_3_2", PieceType.CABALLO, PieceColor.ORO, Position(3, 2)),
                ChessPiece("reina_oro_2_2", PieceType.REINA, PieceColor.ORO, Position(2, 2)),
                ChessPiece("peon_plata_3_1", PieceType.PEON, PieceColor.PLATA, Position(3, 1)),
                ChessPiece("peon_plata_1_0", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
                ChessPiece("reina_plata_2_0", PieceType.REINA, PieceColor.PLATA, Position(2, 0)),
                ChessPiece("alfil_plata_1_2", PieceType.ALFIL, PieceColor.PLATA, Position(1, 2)),
                ChessPiece("rey_plata_0_1", PieceType.REY, PieceColor.PLATA, Position(0, 1))
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(3, 2), Position(2, 0))),
                "¿Cuánto es 7 x 7 ?", "49", 2, 1, "Ataque frontal"),

            33 to NivelConfig(33, 6, listOf(
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
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(3, 4), Position(3, 5))),
                "¿Cuánto es 7 x 8 ?", "56", 2, 1, "Presión constante"),

            34 to NivelConfig(34, 5, listOf(
                ChessPiece("caballo_plata_0_4", PieceType.CABALLO, PieceColor.PLATA, Position(0, 4)),
                ChessPiece("caballo_plata_2_0", PieceType.CABALLO, PieceColor.PLATA, Position(2, 0)),
                ChessPiece("rey_plata_0_1", PieceType.REY, PieceColor.PLATA, Position(0, 1)),
                ChessPiece("alfil_plata_1_1", PieceType.ALFIL, PieceColor.PLATA, Position(1, 1)),
                ChessPiece("reina_plata_4_1", PieceType.REINA, PieceColor.PLATA, Position(4, 1)),
                ChessPiece("torre_oro_3_2", PieceType.TORRE, PieceColor.ORO, Position(3, 2)),
                ChessPiece("caballo_oro_3_1", PieceType.CABALLO, PieceColor.ORO, Position(3, 1)),
                ChessPiece("reina_oro_1_3", PieceType.REINA, PieceColor.ORO, Position(1, 3))
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(3, 2), Position(0, 2))),
                "¿Cuánto es 7 x 3 ?", "21", 2, 1, "Cierra el cerco"),

            35 to NivelConfig(35, 5, listOf(
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
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(4, 2), Position(1, 2))),
                "¿Cuánto es 3 x 8 ?", "24", 2, 1, "Avanza seguro"),

            36 to NivelConfig(36, 6, listOf(
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
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(3, 2), Position(2, 0))),
                "¿Cuánto es 3 x 4 ?", "12", 2, 1, "Despeja el camino"),

            37 to NivelConfig(37, 6, listOf(
                ChessPiece("rey_plata_0_4", PieceType.REY, PieceColor.PLATA, Position(0, 4)),
                ChessPiece("torre_plata_0_3", PieceType.TORRE, PieceColor.PLATA, Position(0, 3)),
                ChessPiece("peon_plata_1_3", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
                ChessPiece("peon_plata_1_4", PieceType.PEON, PieceColor.PLATA, Position(1, 4)),
                ChessPiece("torre_oro_3_2", PieceType.TORRE, PieceColor.ORO, Position(3, 2)),
                ChessPiece("caballo_oro_3_1", PieceType.CABALLO, PieceColor.ORO, Position(3, 1))
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(3, 1), Position(1, 2))),
                "¿Cuánto es 4 x 4 ?", "16", 2, 1, "Salto táctico"),

            38 to NivelConfig(38, 6, listOf(
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
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(4, 5), Position(3, 5))),
                "¿Cuánto es 2 x 4 ", "8", 2, 1, "Avanza con cautela"),

            39 to NivelConfig(39, 6, listOf(
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
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(3, 2), Position(2, 3))),
                "¿Cuánto es 2 x 9 ", "18", 2, 1, "Maniobra envolvente"),

            40 to NivelConfig(40, 5, listOf(
                ChessPiece("alfil_oro_4_3", PieceType.ALFIL, PieceColor.ORO, Position(4, 3)),
                ChessPiece("reina_oro_2_3", PieceType.REINA, PieceColor.ORO, Position(2, 3)),
                ChessPiece("rey_plata_1_0", PieceType.REY, PieceColor.PLATA, Position(1, 0)),
                ChessPiece("alfil_plata_1_1", PieceType.ALFIL, PieceColor.PLATA, Position(1, 1)),
                ChessPiece("peon_plata_3_0", PieceType.PEON, PieceColor.PLATA, Position(3, 0)),
                ChessPiece("peon_plata_2_1", PieceType.PEON, PieceColor.PLATA, Position(2, 1))
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(4, 3), Position(2, 1))),
                "¿Cuánto es 6 x 9 ", "54", 4, 1, "Ataque contundente"),

            41 to NivelConfig(41, 6, listOf(
                ChessPiece("peon_plata_1_4", PieceType.PEON, PieceColor.PLATA, Position(1, 4)),
                ChessPiece("peon_plata_2_3", PieceType.PEON, PieceColor.PLATA, Position(2, 3)),
                ChessPiece("peon_plata_3_4", PieceType.PEON, PieceColor.PLATA, Position(3, 4)),
                ChessPiece("caballo_plata_1_5", PieceType.CABALLO, PieceColor.PLATA, Position(1, 5)),
                ChessPiece("torre_plata_0_3", PieceType.TORRE, PieceColor.PLATA, Position(0, 3)),
                ChessPiece("rey_plata_0_4", PieceType.REY, PieceColor.PLATA, Position(0, 4)),
                ChessPiece("reina_plata_0_1", PieceType.REINA, PieceColor.PLATA, Position(0, 1)),
                ChessPiece("caballo_oro_3_1", PieceType.CABALLO, PieceColor.ORO, Position(3, 1)),
                ChessPiece("alfil_oro_4_0", PieceType.ALFIL, PieceColor.ORO, Position(4, 0))
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(3, 1), Position(1, 2))),
                "¿Cuánto es 7 x 9 ", "63", 4, 1, "Control total"),

            42 to NivelConfig(42, 5, listOf(
                ChessPiece("torre_oro_4_4", PieceType.TORRE, PieceColor.ORO, Position(4, 4)),
                ChessPiece("caballo_oro_3_3", PieceType.CABALLO, PieceColor.ORO, Position(3, 3)),
                ChessPiece("reina_oro_2_3", PieceType.REINA, PieceColor.ORO, Position(2, 3)),
                ChessPiece("reina_plata_2_2", PieceType.REINA, PieceColor.PLATA, Position(2, 2)),
                ChessPiece("torre_plata_0_1", PieceType.TORRE, PieceColor.PLATA, Position(0, 1)),
                ChessPiece("rey_plata_0_3", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
                ChessPiece("alfil_plata_1_3", PieceType.ALFIL, PieceColor.PLATA, Position(1, 3))
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(4, 4), Position(1, 4))),
                "¿Cuánto es 7 x 2 ", "14", 4, 1, "Ataque rápido"),

            43 to NivelConfig(43, 6, listOf(
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
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(5, 3), Position(4, 4))),
                "¿Cuánto es 6 x 2 ", "12", 3, 1, "El ataque es tuyo"),

            44 to NivelConfig(44, 5, listOf(
                ChessPiece("peon_plata_1_0", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
                ChessPiece("torre_plata_0_3", PieceType.TORRE, PieceColor.PLATA, Position(0, 3)),
                ChessPiece("rey_plata_1_1", PieceType.REY, PieceColor.PLATA, Position(1, 1)),
                ChessPiece("peon_plata_1_3", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
                ChessPiece("peon_oro_1_2", PieceType.PEON, PieceColor.ORO, Position(1, 2)),
                ChessPiece("peon_oro_2_3", PieceType.PEON, PieceColor.ORO, Position(2, 3)),
                ChessPiece("peon_oro_3_1", PieceType.PEON, PieceColor.ORO, Position(3, 1)),
                ChessPiece("reina_oro_3_0", PieceType.REINA, PieceColor.ORO, Position(3, 0)),
                ChessPiece("caballo_oro_4_2", PieceType.CABALLO, PieceColor.ORO, Position(4, 2))
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(3, 0), Position(2, 0))),
                "¿Cuánto es 6 x 5 ", "30", 3, 1, "Define la partida"),

            45 to NivelConfig(45, 6, listOf(
                ChessPiece("torre_oro_5_5", PieceType.TORRE, PieceColor.ORO, Position(5, 5)),
                ChessPiece("alfil_oro_3_4", PieceType.ALFIL, PieceColor.ORO, Position(3, 4)),
                ChessPiece("peon_oro_2_4", PieceType.PEON, PieceColor.ORO, Position(2, 4)),
                ChessPiece("peon_plata_2_5", PieceType.PEON, PieceColor.PLATA, Position(2, 5)),
                ChessPiece("peon_plata_1_4", PieceType.PEON, PieceColor.PLATA, Position(1, 4)),
                ChessPiece("peon_plata_1_3", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
                ChessPiece("caballo_oro_1_2", PieceType.CABALLO, PieceColor.ORO, Position(1, 2)),
                ChessPiece("torre_plata_0_3", PieceType.TORRE, PieceColor.PLATA, Position(0, 3)),
                ChessPiece("rey_plata_0_5", PieceType.REY, PieceColor.PLATA, Position(0, 5))
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(5, 5), Position(2, 5))),
                "¿Cuánto es 6 x 6 ", "36", 3, 1, "Victoria inminente"),

            46 to NivelConfig(46, 5, listOf(
                ChessPiece("caballo_oro_3_2", PieceType.CABALLO, PieceColor.ORO, Position(3, 2)),
                ChessPiece("caballo_oro_2_3", PieceType.CABALLO, PieceColor.ORO, Position(2, 3)),
                ChessPiece("reina_oro_2_4", PieceType.REINA, PieceColor.ORO, Position(2, 4)),
                ChessPiece("caballo_plata_1_4", PieceType.CABALLO, PieceColor.PLATA, Position(1, 4)),
                ChessPiece("rey_plata_0_3", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
                ChessPiece("torre_plata_1_2", PieceType.TORRE, PieceColor.PLATA, Position(1, 2)),
                ChessPiece("peon_plata_2_2", PieceType.PEON, PieceColor.PLATA, Position(2, 2)),
                ChessPiece("peon_plata_3_1", PieceType.PEON, PieceColor.PLATA, Position(3, 1))
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(2, 4), Position(1, 3))),
                "¿Cuánto es 6 x 6 ", "36", 3, 1, "Golpe maestro"),

            47 to NivelConfig(47, 6, listOf(
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
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(4, 2), Position(3, 2))),
                "¿Cuánto es 6 x 7 ", "42", 2, 1, "Avanza firme"),

            48 to NivelConfig(48, 4, listOf(
                ChessPiece("reina_oro_3_2", PieceType.REINA, PieceColor.ORO, Position(3, 2)),
                ChessPiece("torre_oro_0_0", PieceType.TORRE, PieceColor.ORO, Position(0, 0)),
                ChessPiece("peon_plata_1_2", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
                ChessPiece("peon_plata_1_3", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
                ChessPiece("caballo_plata_0_2", PieceType.CABALLO, PieceColor.PLATA, Position(0, 2)),
                ChessPiece("rey_plata_0_3", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
                ChessPiece("torre_oro_3_1", PieceType.TORRE, PieceColor.ORO, Position(3, 1))
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(3, 1), Position(2, 1))),
                "¿Cuánto es 7 x 7 ", "49", 3, 1, "Táctica perfecta"),

            49 to NivelConfig(49, 5, listOf(
                ChessPiece("peon_oro_4_3", PieceType.PEON, PieceColor.ORO, Position(4, 3)),
                ChessPiece("peon_oro_4_4", PieceType.PEON, PieceColor.ORO, Position(4, 4)),
                ChessPiece("caballo_oro_3_3", PieceType.CABALLO, PieceColor.ORO, Position(3, 3)),
                ChessPiece("torre_oro_1_0", PieceType.TORRE, PieceColor.ORO, Position(1, 0)),
                ChessPiece("alfil_plata_3_1", PieceType.ALFIL, PieceColor.PLATA, Position(3, 1)),
                ChessPiece("peon_plata_2_3", PieceType.PEON, PieceColor.PLATA, Position(2, 3)),
                ChessPiece("torre_plata_0_4", PieceType.TORRE, PieceColor.PLATA, Position(0, 4)),
                ChessPiece("rey_plata_2_4", PieceType.REY, PieceColor.PLATA, Position(2, 4)),
                ChessPiece("reina_oro_1_2", PieceType.REINA, PieceColor.ORO, Position(1, 2))
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(1, 2), Position(1, 4))),
                "¿Cuánto es 7 x 2 ", "14", 2, 1, "Ataque veloz"),

            50 to NivelConfig(50, 5, listOf(
                ChessPiece("peon_oro_2_3", PieceType.PEON, PieceColor.ORO, Position(2, 3)),
                ChessPiece("torre_oro_4_1", PieceType.TORRE, PieceColor.ORO, Position(4, 1)),
                ChessPiece("alfil_oro_4_0", PieceType.ALFIL, PieceColor.ORO, Position(4, 0)),
                ChessPiece("reina_oro_2_1", PieceType.REINA, PieceColor.ORO, Position(2, 1)),
                ChessPiece("peon_plata_2_2", PieceType.PEON, PieceColor.PLATA, Position(2, 2)),
                ChessPiece("caballo_plata_2_4", PieceType.CABALLO, PieceColor.PLATA, Position(2, 4)),
                ChessPiece("torre_plata_0_4", PieceType.TORRE, PieceColor.PLATA, Position(0, 4)),
                ChessPiece("torre_plata_0_0", PieceType.TORRE, PieceColor.PLATA, Position(0, 0)),
                ChessPiece("rey_plata_0_2", PieceType.REY, PieceColor.PLATA, Position(0, 2))
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(2, 1), Position(2, 2))),
                "¿Cuánto es 4 x 3 ", "12", 2, 1, "Victoria final"),

            51 to NivelConfig(51, 5, listOf(
                ChessPiece("reina_oro_3_0", PieceType.REINA, PieceColor.ORO, Position(3, 0)),
                ChessPiece("alfil_oro_3_2", PieceType.ALFIL, PieceColor.ORO, Position(3, 2)),
                ChessPiece("peon_plata_3_1", PieceType.PEON, PieceColor.PLATA, Position(3, 1)),
                ChessPiece("peon_plata_2_2", PieceType.PEON, PieceColor.PLATA, Position(2, 2)),
                ChessPiece("peon_plata_1_1", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
                ChessPiece("rey_plata_0_1", PieceType.REY, PieceColor.PLATA, Position(0, 1)),
                ChessPiece("torre_plata_0_3", PieceType.TORRE, PieceColor.PLATA, Position(0, 3)),
                ChessPiece("reina_plata_0_4", PieceType.REINA, PieceColor.PLATA, Position(0, 4)),
                ChessPiece("caballo_plata_1_3", PieceType.CABALLO, PieceColor.PLATA, Position(1, 3))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(3, 2), Position(2, 3))), "¿Cuánto es 9 x 3 ", "27", 2, 1, "Victoria táctica"),

            52 to NivelConfig(52, 5, listOf(
                ChessPiece("torre_oro_4_4", PieceType.TORRE, PieceColor.ORO, Position(4, 4)),
                ChessPiece("alfil_oro_3_0", PieceType.ALFIL, PieceColor.ORO, Position(3, 0)),
                ChessPiece("reina_oro_1_2", PieceType.REINA, PieceColor.ORO, Position(1, 2)),
                ChessPiece("peon_plata_1_3", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
                ChessPiece("caballo_plata_1_4", PieceType.CABALLO, PieceColor.PLATA, Position(1, 4)),
                ChessPiece("rey_plata_0_4", PieceType.REY, PieceColor.PLATA, Position(0, 4)),
                ChessPiece("torre_plata_0_1", PieceType.TORRE, PieceColor.PLATA, Position(0, 1)),
                ChessPiece("reina_plata_0_0", PieceType.REINA, PieceColor.PLATA, Position(0, 0))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(4, 4), Position(1, 4))), "¿Cuánto es 2 x 9 ", "18", 2, 1, "Victoria táctica"),

            53 to NivelConfig(53, 5, listOf(
                ChessPiece("reina_oro_3_0", PieceType.REINA, PieceColor.ORO, Position(3, 0)),
                ChessPiece("alfil_oro_3_2", PieceType.ALFIL, PieceColor.ORO, Position(3, 2)),
                ChessPiece("peon_plata_3_1", PieceType.PEON, PieceColor.PLATA, Position(3, 1)),
                ChessPiece("peon_plata_2_2", PieceType.PEON, PieceColor.PLATA, Position(2, 2)),
                ChessPiece("peon_plata_1_1", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
                ChessPiece("torre_plata_0_4", PieceType.TORRE, PieceColor.PLATA, Position(0, 4)),
                ChessPiece("torre_plata_0_3", PieceType.TORRE, PieceColor.PLATA, Position(0, 3)),
                ChessPiece("caballo_plata_1_3", PieceType.CABALLO, PieceColor.PLATA, Position(1, 3)),
                ChessPiece("rey_plata_0_1", PieceType.REY, PieceColor.PLATA, Position(0, 1))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(3, 2), Position(2, 3))), "¿Cuánto es 9 x 9 ", "81", 2, 1, "Victoria táctica"),

            54 to NivelConfig(54, 5, listOf(
                ChessPiece("peon_oro_3_1", PieceType.PEON, PieceColor.ORO, Position(3, 1)),
                ChessPiece("peon_oro_2_0", PieceType.PEON, PieceColor.ORO, Position(2, 0)),
                ChessPiece("peon_oro_2_3", PieceType.PEON, PieceColor.ORO, Position(2, 3)),
                ChessPiece("reina_oro_0_4", PieceType.REINA, PieceColor.ORO, Position(0, 4)),
                ChessPiece("torre_plata_0_3", PieceType.TORRE, PieceColor.PLATA, Position(0, 3)),
                ChessPiece("rey_plata_0_2", PieceType.REY, PieceColor.PLATA, Position(0, 2)),
                ChessPiece("peon_plata_2_1", PieceType.PEON, PieceColor.PLATA, Position(2, 1)),
                ChessPiece("peon_plata_1_0", PieceType.PEON, PieceColor.PLATA, Position(1, 0))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(0, 4), Position(2, 2))), "¿Cuánto es 9 x 9 + 4 ", "85", 2, 1, "Victoria táctica"),

            55 to NivelConfig(55, 5, listOf(
                ChessPiece("torre_plata_0_2", PieceType.TORRE, PieceColor.PLATA, Position(0, 2)),
                ChessPiece("torre_plata_1_2", PieceType.TORRE, PieceColor.PLATA, Position(1, 2)),
                ChessPiece("rey_plata_0_4", PieceType.REY, PieceColor.PLATA, Position(0, 4)),
                ChessPiece("peon_plata_2_4", PieceType.PEON, PieceColor.PLATA, Position(2, 4)),
                ChessPiece("caballo_oro_2_2", PieceType.CABALLO, PieceColor.ORO, Position(2, 2)),
                ChessPiece("reina_oro_2_3", PieceType.REINA, PieceColor.ORO, Position(2, 3))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(2, 3), Position(1, 4))), "¿Cuánto es 9 x 3 + 4 ", "31", 2, 1, "Victoria táctica"),

            56 to NivelConfig(56, 5, listOf(
                ChessPiece("alfil_oro_3_0", PieceType.ALFIL, PieceColor.ORO, Position(3, 0)),
                ChessPiece("alfil_oro_3_3", PieceType.ALFIL, PieceColor.ORO, Position(3, 3)),
                ChessPiece("reina_oro_3_4", PieceType.REINA, PieceColor.ORO, Position(3, 4)),
                ChessPiece("peon_plata_1_3", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
                ChessPiece("torre_plata_1_2", PieceType.TORRE, PieceColor.PLATA, Position(1, 2)),
                ChessPiece("rey_plata_0_3", PieceType.REY, PieceColor.PLATA, Position(0, 3))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(3, 4), Position(1, 2))), "¿Cuánto es 9 x 7 + 7 ", "70", 2, 1, "Victoria táctica"),

            57 to NivelConfig(57, 5, listOf(
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
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(2, 2), Position(1, 1))), "¿Cuánto es 9 x 6 + 6 ", "60", 2, 1, "Victoria táctica"),

            58 to NivelConfig(58, 6, listOf(
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
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(3, 5), Position(3, 1))), "¿Cuánto es 9 x 5 + 5 ", "50", 2, 1, "Victoria táctica"),

            59 to NivelConfig(59, 6, listOf(
                ChessPiece("peon_oro_3_4", PieceType.PEON, PieceColor.ORO, Position(3, 4)),
                ChessPiece("reina_oro_2_4", PieceType.REINA, PieceColor.ORO, Position(2, 4)),
                ChessPiece("peon_oro_2_2", PieceType.PEON, PieceColor.ORO, Position(2, 2)),
                ChessPiece("peon_plata_1_2", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
                ChessPiece("peon_plata_1_0", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
                ChessPiece("reina_oro_0_3", PieceType.REINA, PieceColor.PLATA, Position(0, 3)),
                ChessPiece("rey_plata_0_1", PieceType.REY, PieceColor.PLATA, Position(0, 1))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(2, 4), Position(5, 2))), "¿Cuánto es 9 x 4 + 4 ", "40", 2, 1, "Victoria táctica"),

            60 to NivelConfig(60, 6, listOf(
                ChessPiece("rey_plata_0_1", PieceType.REY, PieceColor.PLATA, Position(0, 1)),
                ChessPiece("peon_plata_1_2", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
                ChessPiece("peon_plata_2_3", PieceType.PEON, PieceColor.PLATA, Position(2, 3)),
                ChessPiece("torre_plata_0_4", PieceType.TORRE, PieceColor.PLATA, Position(0, 4)),
                ChessPiece("reina_plata_2_4", PieceType.REINA, PieceColor.PLATA, Position(2, 4)),
                ChessPiece("alfil_oro_3_2", PieceType.ALFIL, PieceColor.ORO, Position(3, 2)),
                ChessPiece("reina_oro_2_0", PieceType.REINA, PieceColor.ORO, Position(2, 0))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(3, 2), Position(1, 0))), "¿Cuánto es 9 x 3 + 3 ", "30", 4, 1, "Victoria táctica"),

            61 to NivelConfig(61, 5, listOf(
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
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(4, 4), Position(2, 3))), "¿Cuánto es 8 x 3 + 3 ", "27", 2, 1, "Victoria táctica"),

            62 to NivelConfig(62, 5, listOf(
                ChessPiece("torre_oro_1_0", PieceType.TORRE, PieceColor.ORO, Position(1, 0)),
                ChessPiece("caballo_oro_3_0", PieceType.CABALLO, PieceColor.ORO, Position(3, 0)),
                ChessPiece("alfil_oro_3_1", PieceType.ALFIL, PieceColor.ORO, Position(3, 1)),
                ChessPiece("peon_plata_1_4", PieceType.PEON, PieceColor.PLATA, Position(1, 4)),
                ChessPiece("peon_plata_2_3", PieceType.PEON, PieceColor.PLATA, Position(2, 3)),
                ChessPiece("alfil_plata_3_2", PieceType.ALFIL, PieceColor.PLATA, Position(3, 2)),
                ChessPiece("caballo_plata_2_4", PieceType.CABALLO, PieceColor.PLATA, Position(2, 4)),
                ChessPiece("reina_plata_3_4", PieceType.REINA, PieceColor.PLATA, Position(3, 4)),
                ChessPiece("rey_plata_0_3", PieceType.REY, PieceColor.PLATA, Position(0, 3))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(3, 0), Position(2, 2))), "¿Cuánto es 8 x 4 + 3 ", "35", 2, 1, "Victoria táctica"),

            63 to NivelConfig(63, 6, listOf(
                ChessPiece("peon_oro_5_5", PieceType.PEON, PieceColor.ORO, Position(5, 5)),
                ChessPiece("peon_oro_5_4", PieceType.PEON, PieceColor.ORO, Position(5, 4)),
                ChessPiece("alfil_oro_5_3", PieceType.ALFIL, PieceColor.ORO, Position(5, 3)),
                ChessPiece("reina_oro_1_2", PieceType.REINA, PieceColor.ORO, Position(1, 2)),
                ChessPiece("peon_plata_1_5", PieceType.PEON, PieceColor.PLATA, Position(1, 5)),
                ChessPiece("peon_plata_2_4", PieceType.PEON, PieceColor.PLATA, Position(2, 4)),
                ChessPiece("peon_plata_3_2", PieceType.PEON, PieceColor.PLATA, Position(3, 2)),
                ChessPiece("alfil_plata_4_1", PieceType.ALFIL, PieceColor.PLATA, Position(4, 1)),
                ChessPiece("rey_plata_2_5", PieceType.REY, PieceColor.PLATA, Position(2, 5))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(1, 2), Position(0, 3))), "¿Cuánto es 4 x 3 + 3 ", "15", 2, 1, "Victoria táctica"),

            64 to NivelConfig(64, 4, listOf(
                ChessPiece("rey_plata_0_0", PieceType.REY, PieceColor.PLATA, Position(0, 0)),
                ChessPiece("peon_oro_3_1", PieceType.PEON, PieceColor.ORO, Position(3, 1)),
                ChessPiece("peon_oro_2_2", PieceType.PEON, PieceColor.ORO, Position(2, 2)),
                ChessPiece("alfil_oro_2_0", PieceType.ALFIL, PieceColor.ORO, Position(2, 0)),
                ChessPiece("alfil_oro_1_2", PieceType.ALFIL, PieceColor.ORO, Position(1, 2))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(2, 0), Position(1, 1))), "¿Cuánto es 6 x 3 + 8 ", "26", 2, 1, "Victoria táctica"),

            65 to NivelConfig(65, 5, listOf(
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
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(0, 4), Position(0, 1))), "¿Cuánto es 6 x 6 + 9 ", "45", 2, 1, "Victoria táctica"),

            66 to NivelConfig(66, 5, listOf(
                ChessPiece("reina_plata_4_1", PieceType.REINA, PieceColor.PLATA, Position(4, 1)),
                ChessPiece("peon_plata_3_4", PieceType.PEON, PieceColor.PLATA, Position(3, 4)),
                ChessPiece("peon_plata_2_3", PieceType.PEON, PieceColor.PLATA, Position(2, 3)),
                ChessPiece("peon_plata_1_2", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
                ChessPiece("rey_plata_1_3", PieceType.REY, PieceColor.PLATA, Position(1, 3)),
                ChessPiece("alfil_oro_3_3", PieceType.ALFIL, PieceColor.ORO, Position(3, 3)),
                ChessPiece("reina_oro_0_0", PieceType.REINA, PieceColor.ORO, Position(0, 0))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(3, 3), Position(2, 2))), "¿Cuánto es 7 x 6 - 7 ", "35", 2, 1, "Victoria táctica"),

            67 to NivelConfig(67, 6, listOf(
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
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(3, 3), Position(2, 2))), "¿Cuánto es 6 x 6 - 7 ", "43", 2, 1, "Victoria táctica"),

            68 to NivelConfig(68, 6, listOf(
                ChessPiece("reina_oro_5_3", PieceType.REINA, PieceColor.ORO, Position(5, 3)),
                ChessPiece("peon_oro_2_1", PieceType.PEON, PieceColor.ORO, Position(2, 1)),
                ChessPiece("alfil_oro_1_3", PieceType.ALFIL, PieceColor.ORO, Position(1, 3)),
                ChessPiece("peon_plata_1_1", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
                ChessPiece("peon_plata_3_0", PieceType.PEON, PieceColor.PLATA, Position(3, 0)),
                ChessPiece("rey_plata_0_0", PieceType.REY, PieceColor.PLATA, Position(0, 0)),
                ChessPiece("alfil_plata_0_1", PieceType.ALFIL, PieceColor.PLATA, Position(0, 1))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(5, 3), Position(2, 0))), "¿Cuánto es 6 x 9 - 7 ", "47", 2, 1, "Victoria táctica"),

            69 to NivelConfig(69, 4, listOf(
                ChessPiece("rey_plata_0_0", PieceType.REY, PieceColor.PLATA, Position(0, 0)),
                ChessPiece("peon_plata_1_1", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
                ChessPiece("torre_plata_1_2", PieceType.TORRE, PieceColor.PLATA, Position(1, 2)),
                ChessPiece("caballo_plata_2_2", PieceType.CABALLO, PieceColor.PLATA, Position(2, 2)),
                ChessPiece("caballo_oro_0_2", PieceType.CABALLO, PieceColor.ORO, Position(0, 2)),
                ChessPiece("caballo_oro_1_3", PieceType.CABALLO, PieceColor.ORO, Position(1, 3)),
                ChessPiece("reina_oro_3_2", PieceType.REINA, PieceColor.ORO, Position(3, 2))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(5, 3), Position(2, 0))), "¿Cuánto es 8 x 9 - 5 ", "67", 2, 1, "Victoria táctica"),

            70 to NivelConfig(70, 4, listOf(
                ChessPiece("reina_oro_2_0", PieceType.REINA, PieceColor.ORO, Position(2, 0)),
                ChessPiece("caballo_oro_1_1", PieceType.CABALLO, PieceColor.ORO, Position(1, 1)),
                ChessPiece("caballo_oro_3_2", PieceType.CABALLO, PieceColor.ORO, Position(3, 2)),
                ChessPiece("caballo_plata_2_1", PieceType.CABALLO, PieceColor.PLATA, Position(2, 1)),
                ChessPiece("alfil_plata_1_2", PieceType.ALFIL, PieceColor.PLATA, Position(1, 2)),
                ChessPiece("rey_plata_0_2", PieceType.REY, PieceColor.PLATA, Position(0, 2))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(1, 1), Position(2, 3))), "¿Cuánto es 9 x 9 - 6 ", "75", 2, 1, "Victoria táctica"),

            71 to NivelConfig(71, 5, listOf(
                ChessPiece("reina_oro_4_0", PieceType.REINA, PieceColor.ORO, Position(4, 0)),
                ChessPiece("alfil_oro_4_2", PieceType.ALFIL, PieceColor.ORO, Position(4, 2)),
                ChessPiece("alfil_oro_4_3", PieceType.ALFIL, PieceColor.ORO, Position(4, 3)),
                ChessPiece("peon_plata_2_2", PieceType.PEON, PieceColor.PLATA, Position(2, 2)),
                ChessPiece("peon_plata_2_3", PieceType.PEON, PieceColor.PLATA, Position(2, 3)),
                ChessPiece("peon_plata_1_2", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
                ChessPiece("torre_plata_0_3", PieceType.TORRE, PieceColor.PLATA, Position(0, 3)),
                ChessPiece("rey_plata_0_1", PieceType.REY, PieceColor.PLATA, Position(0, 1))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(4, 0), Position(2, 2))), "¿Cuánto es 9 x 9 - 6 ", "75", 3, 1, "Victoria táctica"),

            72 to NivelConfig(72, 6, listOf(
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
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(2, 2), Position(1, 1))), "¿Cuánto es 9 x 6 - 8 ", "46", 5, 1, "Victoria táctica"),

            73 to NivelConfig(73, 6, listOf(
                ChessPiece("torre_oro_5_1", PieceType.TORRE, PieceColor.ORO, Position(5, 1)),
                ChessPiece("torre_oro_4_3", PieceType.TORRE, PieceColor.ORO, Position(4, 3)),
                ChessPiece("reina_oro_4_0", PieceType.REINA, PieceColor.ORO, Position(4, 0)),
                ChessPiece("rey_plata_1_0", PieceType.REY, PieceColor.PLATA, Position(1, 0)),
                ChessPiece("alfil_plata_2_0", PieceType.ALFIL, PieceColor.PLATA, Position(2, 0)),
                ChessPiece("peon_plata_2_3", PieceType.PEON, PieceColor.PLATA, Position(2, 3)),
                ChessPiece("peon_plata_1_2", PieceType.PEON, PieceColor.PLATA, Position(1, 2))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(4, 0), Position(2, 0))), "¿Cuánto es 7 x 9 - 7 ", "56", 2, 1, "Victoria táctica"),

            74 to NivelConfig(74, 6, listOf(
                ChessPiece("peon_plata_3_4", PieceType.PEON, PieceColor.PLATA, Position(3, 4)),
                ChessPiece("peon_plata_2_4", PieceType.PEON, PieceColor.PLATA, Position(2, 4)),
                ChessPiece("peon_plata_1_4", PieceType.PEON, PieceColor.PLATA, Position(1, 4)),
                ChessPiece("rey_plata_1_5", PieceType.REY, PieceColor.PLATA, Position(1, 5)),
                ChessPiece("caballo_plata_0_5", PieceType.CABALLO, PieceColor.PLATA, Position(0, 5)),
                ChessPiece("peon_plata_0_0", PieceType.PEON, PieceColor.PLATA, Position(0, 0)),
                ChessPiece("peon_oro_3_5", PieceType.PEON, PieceColor.ORO, Position(3, 5)),
                ChessPiece("torre_oro_0_1", PieceType.TORRE, PieceColor.ORO, Position(0, 1)),
                ChessPiece("torre_oro_1_1", PieceType.TORRE, PieceColor.ORO, Position(1, 1)),
                ChessPiece("alfil_oro_4_2", PieceType.ALFIL, PieceColor.ORO, Position(4, 2))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(0, 1), Position(0, 5))), "¿Cuánto es 3 x 9 - 7 ", "20", 4, 1, "Victoria táctica"),

            75 to NivelConfig(75, 6, listOf(
                ChessPiece("rey_plata_0_5", PieceType.REY, PieceColor.PLATA, Position(0, 5)),
                ChessPiece("peon_plata_2_5", PieceType.PEON, PieceColor.PLATA, Position(2, 5)),
                ChessPiece("peon_plata_1_4", PieceType.PEON, PieceColor.PLATA, Position(1, 4)),
                ChessPiece("peon_plata_1_3", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
                ChessPiece("torre_plata_0_3", PieceType.TORRE, PieceColor.PLATA, Position(0, 3)),
                ChessPiece("alfil_oro_3_2", PieceType.ALFIL, PieceColor.ORO, Position(3, 2)),
                ChessPiece("reina_oro_4_3", PieceType.REINA, PieceColor.ORO, Position(4, 3))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(4, 3), Position(2, 5))), "¿Cuánto es 3 x 8 - 7 ", "17", 2, 1, "Victoria táctica"),

            76 to NivelConfig(76, 5, listOf(
                ChessPiece("peon_oro_2_3", PieceType.PEON, PieceColor.ORO, Position(2, 3)),
                ChessPiece("peon_oro_2_4", PieceType.PEON, PieceColor.ORO, Position(2, 4)),
                ChessPiece("alfil_oro_1_1", PieceType.ALFIL, PieceColor.ORO, Position(1, 1)),
                ChessPiece("rey_plata_0_3", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
                ChessPiece("caballo_oro_4_3", PieceType.CABALLO, PieceColor.ORO, Position(4, 3))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(4, 3), Position(2, 2))), "¿Cuánto es 4 x 8 - 2 ", "30", 2, 1, "Victoria táctica"),

            77 to NivelConfig(77, 5, listOf(
                ChessPiece("reina_oro_4_3", PieceType.REINA, PieceColor.ORO, Position(4, 3)),
                ChessPiece("caballo_oro_3_3", PieceType.CABALLO, PieceColor.ORO, Position(3, 3)),
                ChessPiece("torre_oro_4_1", PieceType.TORRE, PieceColor.ORO, Position(4, 1)),
                ChessPiece("peon_plata_2_1", PieceType.PEON, PieceColor.PLATA, Position(2, 1)),
                ChessPiece("peon_plata_1_2", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
                ChessPiece("torre_plata_2_4", PieceType.TORRE, PieceColor.PLATA, Position(2, 4)),
                ChessPiece("rey_plata_1_3", PieceType.REY, PieceColor.PLATA, Position(1, 3))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(3, 3), Position(2, 1))), "¿Cuánto es 4 x 8 - 7 ", "25", 2, 1, "Victoria táctica"),

            78 to NivelConfig(78, 5, listOf(
                ChessPiece("alfil_oro_4_1", PieceType.ALFIL, PieceColor.ORO, Position(4, 1)),
                ChessPiece("reina_oro_3_2", PieceType.REINA, PieceColor.ORO, Position(3, 2)),
                ChessPiece("peon_plata_2_4", PieceType.PEON, PieceColor.PLATA, Position(2, 4)),
                ChessPiece("peon_plata_1_3", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
                ChessPiece("torre_plata_0_2", PieceType.TORRE, PieceColor.PLATA, Position(0, 2)),
                ChessPiece("rey_plata_0_3", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
                ChessPiece("alfil_plata_2_2", PieceType.ALFIL, PieceColor.PLATA, Position(2, 2)),
                ChessPiece("caballo_oro_4_3", PieceType.CABALLO, PieceColor.ORO, Position(4, 3))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(4, 3), Position(2, 4))), "¿Cuánto es 3 x 9 - 7 ", "20", 2, 1, "Victoria táctica"),

            79 to NivelConfig(79, 6, listOf(
                ChessPiece("rey_oro_5_3", PieceType.REY, PieceColor.ORO, Position(5, 3)),
                ChessPiece("reina_oro_4_4", PieceType.REINA, PieceColor.ORO, Position(4, 4)),
                ChessPiece("peon_plata_3_4", PieceType.PEON, PieceColor.PLATA, Position(3, 4)),
                ChessPiece("peon_plata_2_3", PieceType.PEON, PieceColor.PLATA, Position(2, 3)),
                ChessPiece("peon_plata_3_2", PieceType.PEON, PieceColor.PLATA, Position(3, 2)),
                ChessPiece("peon_plata_2_5", PieceType.PEON, PieceColor.PLATA, Position(2, 5)),
                ChessPiece("torre_oro_1_1", PieceType.TORRE, PieceColor.ORO, Position(1, 1)),
                ChessPiece("rey_plata_0_4", PieceType.REY, PieceColor.PLATA, Position(0, 4))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(4, 4), Position(2, 2))), "¿Cuánto es 6 x 8 + 7 ", "55", 2, 1, "Victoria táctica"),

            80 to NivelConfig(80, 5, listOf(
                ChessPiece("caballo_oro_3_3", PieceType.CABALLO, PieceColor.ORO, Position(3, 3)),
                ChessPiece("alfil_oro_2_1", PieceType.ALFIL, PieceColor.ORO, Position(2, 1)),
                ChessPiece("torre_oro_1_4", PieceType.TORRE, PieceColor.ORO, Position(1, 4)),
                ChessPiece("torre_plata_0_2", PieceType.TORRE, PieceColor.PLATA, Position(0, 2)),
                ChessPiece("rey_plata_0_1", PieceType.REY, PieceColor.PLATA, Position(0, 1))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(2, 1), Position(1, 0))), "¿Cuánto es 3 x 4 + 9 ", "21", 2, 1, "Victoria táctica"),

            81 to NivelConfig(81, 5, listOf(
                ChessPiece("reina_oro_4_2", PieceType.REINA, PieceColor.ORO, Position(4, 2)),
                ChessPiece("alfil_oro_3_1", PieceType.ALFIL, PieceColor.ORO, Position(3, 1)),
                ChessPiece("peon_plata_2_4", PieceType.PEON, PieceColor.PLATA, Position(2, 4)),
                ChessPiece("peon_plata_1_3", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
                ChessPiece("peon_plata_1_2", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
                ChessPiece("torre_plata_0_2", PieceType.TORRE, PieceColor.PLATA, Position(0, 2)),
                ChessPiece("rey_plata_0_4", PieceType.REY, PieceColor.PLATA, Position(0, 4))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(4, 2), Position(2, 4))), "¿Cuánto es 5 x 8 - 7 ", "33", 2, 1, "Victoria táctica"),

            82 to NivelConfig(82, 6, listOf(
                ChessPiece("torre_oro_5_4", PieceType.TORRE, PieceColor.ORO, Position(5, 4)),
                ChessPiece("reina_oro_4_5", PieceType.REINA, PieceColor.ORO, Position(4, 5)),
                ChessPiece("alfil_oro_4_2", PieceType.ALFIL, PieceColor.ORO, Position(4, 2)),
                ChessPiece("peon_oro_3_2", PieceType.PEON, PieceColor.ORO, Position(3, 2)),
                ChessPiece("alfil_plata_2_2", PieceType.ALFIL, PieceColor.PLATA, Position(2, 2)),
                ChessPiece("peon_plata_1_5", PieceType.PEON, PieceColor.PLATA, Position(1, 5)),
                ChessPiece("peon_plata_1_4", PieceType.PEON, PieceColor.PLATA, Position(1, 4)),
                ChessPiece("rey_plata_0_4", PieceType.REY, PieceColor.PLATA, Position(0, 4)),
                ChessPiece("torre_plata_0_3", PieceType.TORRE, PieceColor.PLATA, Position(0, 3))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(5, 4), Position(1, 4))), "¿Cuánto es 9 x 8 - 7 ", "65", 2, 1, "Victoria táctica"),

            83 to NivelConfig(83, 5, listOf(
                ChessPiece("rey_plata_0_0", PieceType.REY, PieceColor.PLATA, Position(0, 0)),
                ChessPiece("torre_plata_0_1", PieceType.TORRE, PieceColor.PLATA, Position(0, 1)),
                ChessPiece("peon_plata_1_1", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
                ChessPiece("peon_plata_2_0", PieceType.PEON, PieceColor.PLATA, Position(2, 0)),
                ChessPiece("peon_plata_2_2", PieceType.PEON, PieceColor.PLATA, Position(2, 2)),
                ChessPiece("reina_oro_1_2", PieceType.REINA, PieceColor.ORO, Position(1, 2)),
                ChessPiece("caballo_oro_3_2", PieceType.CABALLO, PieceColor.ORO, Position(3, 2))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(1, 2), Position(2, 1))), "¿Cuánto es 3 x 3 + 9 ", "18", 2, 1, "Victoria táctica"),

            84 to NivelConfig(84, 5, listOf(
                ChessPiece("rey_plata_1_4", PieceType.REY, PieceColor.PLATA, Position(1, 4)),
                ChessPiece("torre_plata_0_3", PieceType.TORRE, PieceColor.PLATA, Position(0, 3)),
                ChessPiece("caballo_plata_0_2", PieceType.CABALLO, PieceColor.PLATA, Position(0, 2)),
                ChessPiece("peon_plata_3_4", PieceType.PEON, PieceColor.PLATA, Position(3, 4)),
                ChessPiece("reina_oro_3_3", PieceType.REINA, PieceColor.ORO, Position(3, 3)),
                ChessPiece("torre_oro_4_2", PieceType.TORRE, PieceColor.ORO, Position(4, 2))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(4, 2), Position(1, 2))), "¿Cuánto es 3 x 8 + 7 ", "31", 2, 1, "Victoria táctica"),

            85 to NivelConfig(85, 6, listOf(
                ChessPiece("torre_oro_4_2", PieceType.TORRE, PieceColor.ORO, Position(4, 2)),
                ChessPiece("reina_oro_3_2", PieceType.REINA, PieceColor.ORO, Position(3, 2)),
                ChessPiece("peon_plata_1_3", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
                ChessPiece("peon_plata_1_4", PieceType.PEON, PieceColor.PLATA, Position(1, 4)),
                ChessPiece("peon_plata_2_5", PieceType.PEON, PieceColor.PLATA, Position(2, 5)),
                ChessPiece("rey_plata_2_4", PieceType.REY, PieceColor.PLATA, Position(2, 4)),
                ChessPiece("torre_plata_0_5", PieceType.TORRE, PieceColor.PLATA, Position(0, 5)),
                ChessPiece("caballo_plata_0_4", PieceType.CABALLO, PieceColor.PLATA, Position(0, 4))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(4, 2), Position(4, 4))), "¿Cuánto es 3 x 6 - 8 ", "10", 2, 1, "Victoria táctica"),

            86 to NivelConfig(86, 6, listOf(
                ChessPiece("rey_oro_5_4", PieceType.REY, PieceColor.ORO, Position(5, 4)),
                ChessPiece("peon_oro_4_4", PieceType.PEON, PieceColor.ORO, Position(4, 4)),
                ChessPiece("peon_oro_3_5", PieceType.PEON, PieceColor.ORO, Position(3, 5)),
                ChessPiece("peon_plata_2_4", PieceType.PEON, PieceColor.PLATA, Position(2, 4)),
                ChessPiece("rey_plata_2_5", PieceType.REY, PieceColor.PLATA, Position(2, 5)),
                ChessPiece("peon_plata_1_5", PieceType.PEON, PieceColor.PLATA, Position(1, 5)),
                ChessPiece("reina_oro_2_3", PieceType.REINA, PieceColor.ORO, Position(2, 3)),
                ChessPiece("reina_plata_0_4", PieceType.REINA, PieceColor.PLATA, Position(0, 4))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(5, 4), Position(4, 5))), "¿Cuánto es 9 x 6 - 8 ", "46", 2, 1, "Victoria táctica"),

            87 to NivelConfig(87, 5, listOf(
                ChessPiece("rey_plata_1_0", PieceType.REY, PieceColor.PLATA, Position(1, 0)),
                ChessPiece("rey_oro_1_3", PieceType.REY, PieceColor.ORO, Position(1, 3)),
                ChessPiece("reina_oro_2_2", PieceType.REINA, PieceColor.ORO, Position(2, 2)),
                ChessPiece("peon_plata_3_0", PieceType.PEON, PieceColor.PLATA, Position(3, 0))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(1, 3), Position(1, 2))), "¿Cuánto es 7 x 6 + 8 ", "50", 2, 1, "Victoria táctica"),

            88 to NivelConfig(88, 6, listOf(
                ChessPiece("reina_oro_4_0", PieceType.REINA, PieceColor.ORO, Position(4, 0)),
                ChessPiece("torre_oro_5_1", PieceType.TORRE, PieceColor.ORO, Position(5, 1)),
                ChessPiece("torre_oro_4_3", PieceType.TORRE, PieceColor.ORO, Position(4, 3)),
                ChessPiece("peon_plata_2_3", PieceType.PEON, PieceColor.PLATA, Position(2, 3)),
                ChessPiece("peon_plata_1_2", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
                ChessPiece("alfil_plata_2_0", PieceType.ALFIL, PieceColor.PLATA, Position(2, 0)),
                ChessPiece("rey_plata_1_0", PieceType.REY, PieceColor.PLATA, Position(1, 0))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(4, 0), Position(2, 0))), "¿Cuánto es 7 x 9 + 8 ", "71", 2, 1, "Victoria táctica"),

            89 to NivelConfig(89, 5, listOf(
                ChessPiece("caballo_oro_2_4", PieceType.CABALLO, PieceColor.ORO, Position(2, 4)),
                ChessPiece("peon_plata_1_0", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
                ChessPiece("peon_plata_2_1", PieceType.PEON, PieceColor.PLATA, Position(2, 1)),
                ChessPiece("peon_plata_1_2", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
                ChessPiece("torre_oro_3_3", PieceType.TORRE, PieceColor.ORO, Position(3, 3)),
                ChessPiece("rey_plata_0_2", PieceType.REY, PieceColor.PLATA, Position(0, 2)),
                ChessPiece("torre_oro_4_2", PieceType.TORRE, PieceColor.ORO, Position(4, 2))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(4, 2), Position(1, 2))), "¿Cuánto es 8 x 9 + 8 ", "80", 2, 1, "Victoria táctica"),

            90 to NivelConfig(90, 5, listOf(
                ChessPiece("torre_oro_3_1", PieceType.TORRE, PieceColor.ORO, Position(3, 1)),
                ChessPiece("torre_oro_1_0", PieceType.TORRE, PieceColor.ORO, Position(1, 0)),
                ChessPiece("reina_oro_0_1", PieceType.REINA, PieceColor.ORO, Position(0, 1)),
                ChessPiece("peon_plata_3_3", PieceType.PEON, PieceColor.PLATA, Position(3, 3)),
                ChessPiece("torre_plata_2_4", PieceType.TORRE, PieceColor.PLATA, Position(2, 4)),
                ChessPiece("alfil_plata_0_3", PieceType.ALFIL, PieceColor.PLATA, Position(0, 3)),
                ChessPiece("rey_plata_0_4", PieceType.REY, PieceColor.PLATA, Position(0, 4))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(0, 1), Position(0, 3))), "¿Cuánto es 9 x 9 + 9 ", "90", 2, 1, "Victoria táctica"),

            91 to NivelConfig(91, 5, listOf(
                ChessPiece("torre_oro_4_3", PieceType.TORRE, PieceColor.ORO, Position(4, 3)),
                ChessPiece("reina_oro_3_4", PieceType.REINA, PieceColor.ORO, Position(3, 4)),
                ChessPiece("rey_oro_1_0", PieceType.REY, PieceColor.ORO, Position(1, 0)),
                ChessPiece("rey_plata_0_2", PieceType.REY, PieceColor.PLATA, Position(0, 2)),
                ChessPiece("peon_plata_1_2", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
                ChessPiece("peon_plata_1_3", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
                ChessPiece("torre_plata_0_3", PieceType.TORRE, PieceColor.PLATA, Position(0, 3))
            ), PieceColor.ORO, listOf(MovimientoSolucion(Position(3, 4), Position(2, 3))), "¿Cuánto es 9 x 9 + 10 ", "91", 2, 1, "Victoria táctica"),

            92 to NivelConfig(92, 6, listOf(
                ChessPiece("torre_oro_2_4", PieceType.TORRE, PieceColor.ORO, Position(2, 4)),
                ChessPiece("caballo_oro_2_3", PieceType.CABALLO, PieceColor.ORO, Position(2, 3)),
                ChessPiece("alfil_plata_2_5", PieceType.ALFIL, PieceColor.PLATA, Position(2, 5)),
                ChessPiece("reina_plata_0_4", PieceType.REINA, PieceColor.PLATA, Position(0, 4)),
                ChessPiece("peon_plata_1_0", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
                ChessPiece("peon_plata_2_1", PieceType.PEON, PieceColor.PLATA, Position(2, 1)),
                ChessPiece("peon_plata_1_2", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
                ChessPiece("rey_plata_0_1", PieceType.REY, PieceColor.PLATA, Position(0, 1))
            ), PieceColor.ORO,
                listOf(MovimientoSolucion(Position(2, 4), Position(0, 4))),
                "¿Cuánto es 9 x 2 + 10 ", "28", 2, 1, "Victoria táctica"),

            93 to NivelConfig(id = 93, size = 6, piezas = listOf(
                ChessPiece("reina_oro_2_0", PieceType.REINA, PieceColor.ORO, Position(2, 0)),
                ChessPiece("torre_oro_0_0", PieceType.TORRE, PieceColor.ORO, Position(0, 0)),
                ChessPiece("reina_plata_1_0", PieceType.REINA, PieceColor.PLATA, Position(1, 0)),
                ChessPiece("torre_plata_0_1", PieceType.TORRE, PieceColor.PLATA, Position(0, 1)),
                ChessPiece("alfil_plata_2_1", PieceType.ALFIL, PieceColor.PLATA, Position(2, 1)),
                ChessPiece("caballo_plata_1_2", PieceType.CABALLO, PieceColor.PLATA, Position(1, 2)),
                ChessPiece("peon_plata_1_3", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
                ChessPiece("peon_plata_1_4", PieceType.PEON, PieceColor.PLATA, Position(1, 4)),
                ChessPiece("peon_plata_1_5", PieceType.PEON, PieceColor.PLATA, Position(1, 5)),
                ChessPiece("rey_plata_0_4", PieceType.REY, PieceColor.PLATA, Position(0, 4))
            ), turnoInicial = PieceColor.ORO,
                listOf(MovimientoSolucion(Position(2, 0), Position(0, 2))),
                "¿Cuánto es 9 x 3 + 10 ", "37", 2, 1, "Concentrate en la Victoria táctica"),

        )
    // FUNCIONES DENTRO DEL OBJETO
    fun obtenerTotalDeNiveles(): Int = totalNiveles.size

    fun guardarNivel(config: NivelConfig) {
        if (totalNiveles.containsKey(config.id)) {
            android.util.Log.e("NivelRepository", "Error: El nivel ${config.id} ya existe.")
            return
        }
        totalNiveles[config.id] = config
    }

    fun obtenerNivel(id: Int): NivelConfig? = totalNiveles[id]

    fun generarSetupPorDefecto(size: Int): List<ChessPiece> {
        return if (size == 8) {
            val pieces = mutableListOf<ChessPiece>()
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
} // <--- ESTA LLAVE CIERRA EL OBJECT
