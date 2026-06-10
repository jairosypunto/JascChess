package com.jasc.jasc_chess.data.local

import com.jasc.jasc_chess.model.*

data class NivelConfig(
    val id: Int,
    val size: Int,
    val piezas: List<ChessPiece>,
    val turnoInicial: PieceColor,
    val jugadasMaximas: Int = 2
)

object NivelRepository {

    val totalNiveles = mutableMapOf<Int, NivelConfig>(
        1 to NivelConfig(1, 4, listOf(
            ChessPiece("p_1_2_1780876623237", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("p_2_1_1780876625924", PieceType.ALFIL, PieceColor.PLATA, Position(2, 1)),
            ChessPiece("p_3_2_1780876633828", PieceType.CABALLO, PieceColor.ORO, Position(3, 2)),
            ChessPiece("p_3_1_1780876641967", PieceType.REINA, PieceColor.ORO, Position(3, 1)),
            ChessPiece("p_0_3_1780876660724", PieceType.TORRE, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("p_0_2_1780876668438", PieceType.REY, PieceColor.PLATA, Position(0, 2))
        ), PieceColor.ORO),

        2 to NivelConfig(2, 4, listOf(
            ChessPiece("p_0_3_1780877020212", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("p_1_1_1780877075733", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
            ChessPiece("p_1_1_1780877075733", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
            ChessPiece("p_3_0_1780877095084", PieceType.CABALLO, PieceColor.ORO, Position(3, 0)),
            ChessPiece("p_3_2_1780877102870", PieceType.REINA, PieceColor.ORO, Position(3, 2)),
            ChessPiece("p_0_0_1780877109112", PieceType.REINA, PieceColor.PLATA, Position(0, 0)),
            ChessPiece("p_2_3_1780877165881", PieceType.CABALLO, PieceColor.ORO, Position(2, 3))
        ), PieceColor.ORO),

        3 to NivelConfig(3, 4, listOf(
            ChessPiece("p_1_3_1780877008581", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
            ChessPiece("p_0_3_1780877020212", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("p_3_0_1780877095084", PieceType.CABALLO, PieceColor.ORO, Position(3, 0)),
            ChessPiece("p_3_2_1780877102870", PieceType.REINA, PieceColor.ORO, Position(3, 2)),
            ChessPiece("p_2_0_1780877455710", PieceType.ALFIL, PieceColor.ORO, Position(2, 0)),
            ChessPiece("p_2_3_1780877481867", PieceType.PEON, PieceColor.ORO, Position(2, 3)),
            ChessPiece("p_1_0_1780877491323", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("p_0_0_1780877549511", PieceType.TORRE, PieceColor.PLATA, Position(0, 0))
        ), PieceColor.ORO),

        4 to NivelConfig(4, 4, listOf(
            ChessPiece("n3_k", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("n3_P", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("n3_P", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
            ChessPiece("n1_t", PieceType.TORRE, PieceColor.PLATA, Position(0, 1)),

            ChessPiece("n3_q_oro", PieceType.REINA, PieceColor.ORO, Position(2, 0)),
            ChessPiece("n3_C_oro", PieceType.CABALLO, PieceColor.ORO, Position(3, 2)),
            ChessPiece("n3_t_oro", PieceType.TORRE  , PieceColor.ORO, Position(2, 2))
        ), PieceColor.ORO),

        5 to NivelConfig(5, 4, listOf(
            ChessPiece("n1_q", PieceType.REINA, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("n1_a", PieceType.ALFIL, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("n1_k", PieceType.REY, PieceColor.PLATA, Position(0, 2)),
            ChessPiece("n1_p", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
            ChessPiece("n1_t_oro", PieceType.TORRE, PieceColor.ORO, Position(1, 3)),
            ChessPiece("n1_a_oro", PieceType.ALFIL, PieceColor.ORO, Position(3, 0)),
            ChessPiece("n1_t_oro", PieceType.TORRE, PieceColor.ORO, Position(3, 3))
        ), PieceColor.ORO),

        6 to NivelConfig(6, 4, listOf(
            ChessPiece("n1_k", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("n1_c", PieceType.CABALLO, PieceColor.PLATA, Position(2, 1)),
            ChessPiece("n1_q_oro", PieceType.REINA, PieceColor.ORO, Position(3, 0)),
            ChessPiece("n1_c_oro", PieceType.CABALLO, PieceColor.ORO, Position(3, 3))
        ), PieceColor.ORO),

        7 to NivelConfig(7, 4, listOf(
            ChessPiece("n1_k", PieceType.REY, PieceColor.PLATA, Position(0, 0)),
            ChessPiece("n1_t", PieceType.TORRE, PieceColor.PLATA, Position(0, 2)),
            ChessPiece("n1_P", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("n1_P", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
            ChessPiece("n1_c_oro", PieceType.CABALLO, PieceColor.ORO, Position(2, 0)),
            ChessPiece("n1_q_oro", PieceType.REINA, PieceColor.ORO, Position(2, 3))
        ), PieceColor.ORO),

        8 to NivelConfig(8, 4, listOf(
            ChessPiece("n1_k", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("n1_t", PieceType.TORRE, PieceColor.PLATA, Position(0, 1)),
            ChessPiece("n1_a", PieceType.ALFIL, PieceColor.PLATA, Position(0, 0)),
            ChessPiece("n1_t", PieceType.TORRE, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("n1_p", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
            ChessPiece("n1_p_oro", PieceType.PEON, PieceColor.ORO, Position(2, 3)),
            ChessPiece("n3_a_oro", PieceType.ALFIL, PieceColor.ORO, Position(3, 1)),
            ChessPiece("n3_a_oro", PieceType.ALFIL, PieceColor.ORO, Position(3, 2))
        ), PieceColor.ORO),

        9 to NivelConfig(9, 4, listOf(
            ChessPiece("n1_k", PieceType.REY, PieceColor.PLATA, Position(0, 1)),
            ChessPiece("n1_a", PieceType.ALFIL, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("n1_q", PieceType.REINA, PieceColor.PLATA, Position(2, 0)),
            ChessPiece("n1_p", PieceType.PEON, PieceColor.PLATA, Position(3, 1)),
            ChessPiece("n1_p", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("n1_q_oro", PieceType.REINA, PieceColor.ORO, Position(2, 2)),
            ChessPiece("n3_c_oro", PieceType.CABALLO, PieceColor.ORO, Position(3, 2))
        ), PieceColor.ORO),

        10 to NivelConfig(10, 4, listOf(
            ChessPiece("n1_k", PieceType.REY, PieceColor.PLATA, Position(0, 0)),
            ChessPiece("n1_a", PieceType.ALFIL, PieceColor.PLATA, Position(0, 1)),
            ChessPiece("n1_p", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
            ChessPiece("n1_p", PieceType.PEON, PieceColor.PLATA, Position(3, 0)),
            ChessPiece("n1_q_oro", PieceType.REINA, PieceColor.ORO, Position(3, 1)),
            ChessPiece("n3_p_oro", PieceType.PEON, PieceColor.ORO, Position(2, 1)),
            ChessPiece("n3_a_oro", PieceType.ALFIL, PieceColor.ORO, Position(1, 3))
        ), PieceColor.ORO),

        11 to NivelConfig(11, 4, listOf(
            ChessPiece("n1_t1", PieceType.TORRE, PieceColor.PLATA, Position(0, 0)),
            ChessPiece("n1_t2", PieceType.TORRE, PieceColor.PLATA, Position(0, 1)),
            ChessPiece("n1_k", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("n1_p", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
            ChessPiece("n1_a", PieceType.ALFIL, PieceColor.PLATA, Position(2, 1)),
            ChessPiece("n1_q_oro", PieceType.REINA, PieceColor.ORO, Position(2, 0)),
            ChessPiece("n1_c_oro", PieceType.CABALLO, PieceColor.ORO, Position(3, 0)),
            ChessPiece("n1_t_oro", PieceType.TORRE, PieceColor.ORO, Position(3, 2))
        ), PieceColor.ORO),

        12 to NivelConfig(12, 4, listOf(
            ChessPiece("n2_t_oro", PieceType.TORRE, PieceColor.ORO, Position(3, 1)),
            ChessPiece("n1_q_oro", PieceType.REINA, PieceColor.ORO, Position(2, 0)),
            ChessPiece("n2_k", PieceType.REY, PieceColor.PLATA, Position(0, 0)),
            ChessPiece("n2_t", PieceType.TORRE, PieceColor.PLATA, Position(0, 1)),
            ChessPiece("n1_p", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("n2_t", PieceType.REINA, PieceColor.PLATA, Position(2, 2))
        ), PieceColor.ORO),

        13 to NivelConfig(13, 5, listOf(
            // Plata (Fila 0)
            ChessPiece("n2_k", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("n1_a", PieceType.ALFIL, PieceColor.PLATA, Position(2, 2)),
            ChessPiece("n1_p1", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),
            ChessPiece("n2_t2", PieceType.TORRE, PieceColor.PLATA, Position(0, 4)),
            ChessPiece("n2_q", PieceType.REINA, PieceColor.ORO, Position(4, 3)),
            ChessPiece("n1_c_oro", PieceType.CABALLO, PieceColor.ORO, Position(3, 3)),

            ), PieceColor.ORO),

        14 to NivelConfig(14, 6, listOf(
            // Plata (Fila 0)
            ChessPiece("n2_k", PieceType.REY, PieceColor.PLATA, Position(0, 5)),
            ChessPiece("n2_t1", PieceType.TORRE, PieceColor.PLATA, Position(0, 0)),
            ChessPiece("n2_t2", PieceType.TORRE, PieceColor.PLATA, Position(0, 2)),

            // Peones Plata (Fila 1)
            ChessPiece("n1_p1", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("n1_p2", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),

            // Oro (Fila 5)
            ChessPiece("n1_q_oro", PieceType.REINA, PieceColor.ORO, Position(5, 2)),
            ChessPiece("n2_t_oro", PieceType.TORRE, PieceColor.ORO, Position(5, 4))
        ), PieceColor.ORO),
        15 to NivelConfig(15, 6, listOf(
            // Plata (Fila 0)
            ChessPiece("n2_k", PieceType.REY, PieceColor.PLATA, Position(0, 3)),
            ChessPiece("n1_a", PieceType.ALFIL, PieceColor.PLATA, Position(0, 4)),
            ChessPiece("n2_t1", PieceType.TORRE, PieceColor.PLATA, Position(0, 0)),
            ChessPiece("n2_t2", PieceType.TORRE, PieceColor.PLATA, Position(0, 2)),

            // Peones Plata (Fila 1)
            ChessPiece("n1_p1", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("n1_p2", PieceType.PEON, PieceColor.PLATA, Position(1, 3)),

            // Oro (Fila 5)
            ChessPiece("n1_c_oro", PieceType.CABALLO, PieceColor.ORO, Position(3, 0)),
            ChessPiece("n1_q_oro", PieceType.REINA, PieceColor.ORO, Position(2, 0)),
            ChessPiece("n2_t_oro", PieceType.TORRE, PieceColor.ORO, Position(5, 4))
        ), PieceColor.ORO),
        16 to NivelConfig(16, 5, listOf(
            // Plata (Fila 0)
            ChessPiece("p_0_1_1780980688004", PieceType.REY, PieceColor.PLATA, Position(0, 1)),
            ChessPiece("p_0_2_1780980691412", PieceType.REINA, PieceColor.PLATA, Position(0, 2)),
            ChessPiece("p_1_0_1780980694431", PieceType.PEON, PieceColor.PLATA, Position(1, 0)),
            ChessPiece("p_1_1_1780980694685", PieceType.PEON, PieceColor.PLATA, Position(1, 1)),
            ChessPiece("p_1_2_1780980694917", PieceType.PEON, PieceColor.PLATA, Position(1, 2)),
            ChessPiece("p_3_0_1780980702060", PieceType.REINA, PieceColor.ORO, Position(3, 0)),
            ChessPiece("p_3_3_1780980708475", PieceType.TORRE, PieceColor.ORO, Position(3, 3)),
            ChessPiece("p_3_4_1780980711305", PieceType.ALFIL, PieceColor.ORO, Position(3, 4)),
        ), PieceColor.ORO),

    )

// Dentro de object NivelRepository, al final de la clase

    fun generarSetupPorDefecto(size: Int): List<ChessPiece> {
        return when (size) {
            8 -> {
                // Este es el setup clásico que tenías antes en el ViewModel
                val pieces = mutableListOf<ChessPiece>()
                // Fila 0 y 1 (Plata)
                pieces.add(ChessPiece("t_plata_1", PieceType.TORRE, PieceColor.PLATA, Position(0, 0)))
                pieces.add(ChessPiece("c_plata_1", PieceType.CABALLO, PieceColor.PLATA, Position(0, 1)))
                pieces.add(ChessPiece("a_plata_1", PieceType.ALFIL, PieceColor.PLATA, Position(0, 2)))
                pieces.add(ChessPiece("q_plata", PieceType.REINA, PieceColor.PLATA, Position(0, 3)))
                pieces.add(ChessPiece("k_plata", PieceType.REY, PieceColor.PLATA, Position(0, 4)))
                pieces.add(ChessPiece("a_plata_2", PieceType.ALFIL, PieceColor.PLATA, Position(0, 5)))
                pieces.add(ChessPiece("c_plata_2", PieceType.CABALLO, PieceColor.PLATA, Position(0, 6)))
                pieces.add(ChessPiece("t_plata_2", PieceType.TORRE, PieceColor.PLATA, Position(0, 7)))
                for (i in 0..7) { pieces.add(ChessPiece("p_plata_$i", PieceType.PEON, PieceColor.PLATA, Position(1, i))) }

                // Fila 6 y 7 (Oro)
                for (i in 0..7) { pieces.add(ChessPiece("p_oro_$i", PieceType.PEON, PieceColor.ORO, Position(6, i))) }
                pieces.add(ChessPiece("t_oro_1", PieceType.TORRE, PieceColor.ORO, Position(7, 0)))
                pieces.add(ChessPiece("c_oro_1", PieceType.CABALLO, PieceColor.ORO, Position(7, 1)))
                pieces.add(ChessPiece("a_oro_1", PieceType.ALFIL, PieceColor.ORO, Position(7, 2)))
                pieces.add(ChessPiece("q_oro", PieceType.REINA, PieceColor.ORO, Position(7, 3)))
                pieces.add(ChessPiece("k_oro", PieceType.REY, PieceColor.ORO, Position(7, 4)))
                pieces.add(ChessPiece("a_oro_2", PieceType.ALFIL, PieceColor.ORO, Position(7, 5)))
                pieces.add(ChessPiece("c_oro_2", PieceType.CABALLO, PieceColor.ORO, Position(7, 6)))
                pieces.add(ChessPiece("t_oro_2", PieceType.TORRE, PieceColor.ORO, Position(7, 7)))
                pieces
            }
            else -> {
                // Si el tamaño no es 8, usa el generador profesional que ya tienes hecho
                generarSetupProfesional(size)
            }
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

    // Dentro de object NivelRepository
    fun obtenerNivel(id: Int): NivelConfig? {
        return totalNiveles[id]
    }
}