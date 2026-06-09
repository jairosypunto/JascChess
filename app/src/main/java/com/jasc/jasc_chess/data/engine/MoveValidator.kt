package com.jasc.jasc_chess.data.engine

import com.jasc.jasc_chess.model.*
import com.jasc.jasc_chess.model.ChessPiece
import kotlin.Int
import kotlin.math.abs

object MoveValidator {

    fun esMovimientoValido(pieza: ChessPiece, destino: Position, piezas: List<ChessPiece>, size: Int): Boolean {
        val movimientos: List<Position> = obtenerMovimientosValidos(pieza, piezas, size)
        return movimientos.contains(destino)
    }

    fun esPosicionValida(row: Int, col: Int, size: Int): Boolean = row in 0 until size && col in 0 until size

    fun obtenerMovimientosValidos(pieza: ChessPiece, piezas: List<ChessPiece>, size: Int): List<Position> {
        val movimientos: List<Position> = obtenerMovimientosBasicos(pieza, piezas, size)
        return movimientos.filter { destino ->
            val simulacion = piezas.filterNot { it.position == destino || it.position == pieza.position } + pieza.copy(position = destino)
            !estaElReyEnJaque(pieza.color, simulacion, size)
        }
    }

    fun estaElReyEnJaque(color: PieceColor, piezas: List<ChessPiece>, size: Int): Boolean {
        val rey = piezas.find { it.type == PieceType.REY && it.color == color } ?: return false
        val op = if (color == PieceColor.ORO) PieceColor.PLATA else PieceColor.ORO
        return esCasillaAmenazadaPorGeometria(rey.position, op, piezas, size)
    }

    fun esCasillaAmenazadaPorGeometria(pos: Position, atacanteColor: PieceColor, piezas: List<ChessPiece>, size: Int): Boolean {
        return piezas.any { p ->
            if (p.color != atacanteColor) return@any false
            val dr = pos.row - p.position.row
            val dc = pos.col - p.position.col
            val absR = abs(dr); val absC = abs(dc)

            when (p.type) {
                PieceType.PEON -> (dr == (if (p.color == PieceColor.ORO) -1 else 1)) && absC == 1
                PieceType.CABALLO -> (absR == 2 && absC == 1) || (absR == 1 && absC == 2)
                PieceType.REY -> absR <= 1 && absC <= 1
                PieceType.TORRE -> (dr == 0 || dc == 0) && !estaBloqueado(p.position, pos, piezas)
                PieceType.ALFIL -> (absR == absC) && !estaBloqueado(p.position, pos, piezas)
                PieceType.REINA -> ((dr == 0 || dc == 0) || (absR == absC)) && !estaBloqueado(p.position, pos, piezas)
            }
        }
    }

    private fun estaBloqueado(origen: Position, destino: Position, piezas: List<ChessPiece>): Boolean {
        val dr = Integer.signum(destino.row - origen.row)
        val dc = Integer.signum(destino.col - origen.col)
        var r = origen.row + dr; var c = origen.col + dc
        while (r != destino.row || c != destino.col) {
            if (piezas.any { it.position.row == r && it.position.col == c }) return true
            r += dr; c += dc
        }
        return false
    }

    private fun estaCaminoLibre(fila: Int, cols: List<Int>, piezas: List<ChessPiece>): Boolean {
        return cols.all { c -> piezas.none { it.position.row == fila && it.position.col == c } }
    }

    fun obtenerMovimientosBasicos(pieza: ChessPiece, piezas: List<ChessPiece>, size: Int): List<Position> {
        return when (pieza.type) {
            PieceType.PEON -> obtenerMovimientosPeon(pieza, piezas, size)
            PieceType.TORRE -> obtenerMovimientosTorre(pieza, piezas, size)
            PieceType.ALFIL -> obtenerMovimientosAlfil(pieza, piezas, size)
            PieceType.REINA -> obtenerMovimientosTorre(pieza, piezas, size) + obtenerMovimientosAlfil(pieza, piezas, size)
            PieceType.CABALLO -> obtenerMovimientosCaballo(pieza, piezas, size)
            PieceType.REY -> obtenerMovimientosRey(pieza, piezas, size)
        }
    }

    private fun obtenerMovimientosPeon(p: ChessPiece, piezas: List<ChessPiece>, size: Int): List<Position> {
        val moves = mutableListOf<Position>()
        val dir = if (p.color == PieceColor.ORO) -1 else 1
        val filaSiguiente = p.position.row + dir

        // Movimiento simple hacia adelante
        val f1 = Position(filaSiguiente, p.position.col)
        if (esPosicionValida(f1.row, f1.col, size) && piezas.none { it.position == f1 }) {
            moves.add(f1)

            // Salto doble (dinámico según la fila de inicio)
            val filaInicio = if (p.color == PieceColor.ORO) size - 2 else 1
            val f2 = Position(p.position.row + (dir * 2), p.position.col)
            if (p.position.row == filaInicio && piezas.none { it.position == f2 }) {
                moves.add(f2)
            }
        }

        // Captura diagonal
        listOf(-1, 1).forEach { colOffset ->
            val diagonal = Position(filaSiguiente, p.position.col + colOffset)
            if (piezas.any { it.position == diagonal && it.color != p.color }) {
                moves.add(diagonal)
            }
        }
        return moves
    }

    private fun obtenerMovimientosTorre(p: ChessPiece, ps: List<ChessPiece>, s: Int): List<Position> {
        val moves = mutableListOf<Position>()
        val piezasMap = ps.associateBy { it.position } // Optimización O(1)
        val dirs = listOf(0 to 1, 0 to -1, 1 to 0, -1 to 0)

        for ((dr, dc) in dirs) {
            var r = p.position.row + dr; var c = p.position.col + dc
            while (esPosicionValida(r, c, s)) {
                val dest = Position(r, c)
                val target = piezasMap[dest]
                if (target == null) moves.add(dest)
                else { if (target.color != p.color) moves.add(dest); break }
                r += dr; c += dc
            }
        }
        return moves
    }

    private fun obtenerMovimientosAlfil(p: ChessPiece, ps: List<ChessPiece>, s: Int): List<Position> {
        val moves = mutableListOf<Position>()
        val piezasMap = ps.associateBy { it.position } // Optimización O(1)
        val dirs = listOf(1 to 1, 1 to -1, -1 to 1, -1 to -1)

        for ((dr, dc) in dirs) {
            var r = p.position.row + dr; var c = p.position.col + dc
            while (esPosicionValida(r, c, s)) {
                val pos = Position(r, c)
                val target = piezasMap[pos]
                if (target == null) moves.add(pos)
                else { if (target.color != p.color) moves.add(pos); break }
                r += dr; c += dc
            }
        }
        return moves
    }

    private fun obtenerMovimientosCaballo(p: ChessPiece, ps: List<ChessPiece>, s: Int): List<Position> {
        val moves = mutableListOf<Position>()
        val piezasMap = ps.associateBy { it.position } // Optimización O(1)
        val saltos = listOf(-2 to 1, -2 to -1, 2 to 1, 2 to -1, -1 to 2, -1 to -2, 1 to 2, 1 to -2)

        for ((dr, dc) in saltos) {
            val r = p.position.row + dr; val c = p.position.col + dc
            if (esPosicionValida(r, c, s)) {
                val target = piezasMap[Position(r, c)]
                if (target == null || target.color != p.color) moves.add(Position(r, c))
            }
        }
        return moves
    }

    private fun obtenerMovimientosRey(rey: ChessPiece, piezas: List<ChessPiece>, size: Int): List<Position> {
        val moves = mutableListOf<Position>()
        val piezasMap = piezas.associateBy { it.position } // Optimización O(1)

        // 1. Movimientos normales
        for (dr in -1..1) {
            for (dc in -1..1) {
                if (dr == 0 && dc == 0) continue
                val r = rey.position.row + dr; val c = rey.position.col + dc
                if (esPosicionValida(r, c, size)) {
                    val pos = Position(r, c)
                    val target = piezasMap[pos]
                    if (target == null || target.color != rey.color) moves.add(pos)
                }
            }
        }

        // 2. Enroque dinámico
        if (!rey.hasMoved) {
            val opColor = if (rey.color == PieceColor.ORO) PieceColor.PLATA else PieceColor.ORO
            if (!esCasillaAmenazadaPorGeometria(rey.position, opColor, piezas, size)) {

                // Enroque Corto
                val torreCorta = piezas.find { it.type == PieceType.TORRE && it.color == rey.color && it.position.col == size - 1 && it.position.row == rey.position.row }
                if (torreCorta != null && !torreCorta.hasMoved) {
                    val caminoCorto = (rey.position.col + 1 until size - 1).toList()
                    if (estaCaminoLibre(rey.position.row, caminoCorto, piezas)) {
                        if (caminoCorto.all { !esCasillaAmenazadaPorGeometria(Position(rey.position.row, it), opColor, piezas, size) }) {
                            moves.add(Position(rey.position.row, size - 2))
                        }
                    }
                }

                // Enroque Largo
                val torreLarga = piezas.find { it.type == PieceType.TORRE && it.color == rey.color && it.position.col == 0 && it.position.row == rey.position.row }
                if (torreLarga != null && !torreLarga.hasMoved) {
                    val caminoLargo = (1 until rey.position.col).toList()
                    if (estaCaminoLibre(rey.position.row, caminoLargo, piezas)) {
                        if (caminoLargo.all { !esCasillaAmenazadaPorGeometria(Position(rey.position.row, it), opColor, piezas, size) }) {
                            moves.add(Position(rey.position.row, 2))
                        }
                    }
                }
            }
        }
        return moves
    }
}