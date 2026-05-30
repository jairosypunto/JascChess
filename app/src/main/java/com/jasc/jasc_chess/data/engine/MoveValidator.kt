package com.jasc.jasc_chess.data.engine

import com.jasc.jasc_chess.model.*
import kotlin.math.abs

object MoveValidator {

    // Dentro de MoveValidator.kt
    fun esPosicionValida(row: Int, col: Int, size: Int): Boolean {
        return row in 0 until size && col in 0 until size
    }

    // --- CAMINO A: VALIDA JUGADAS ---
    fun obtenerMovimientosValidos(pieza: ChessPiece, piezas: List<ChessPiece>, size: Int): List<Position> {
        val movimientos = obtenerMovimientosBasicos(pieza, piezas, size)
        return movimientos.filter { destino ->
            val simulacion = piezas.filterNot { it.position == destino || it.position == pieza.position } + pieza.copy(position = destino)
            !estaElReyEnJaque(pieza.color, simulacion, size)
        }
    }

// EN MoveValidator.kt

    // Quita el "private" para que BoardViewModel pueda verla
    fun estaElReyEnJaque(color: PieceColor, piezas: List<ChessPiece>, size: Int): Boolean {
        val rey = piezas.find { it.type == PieceType.REY && it.color == color } ?: return false
        val colorOponente = if (color == PieceColor.ORO) PieceColor.PLATA else PieceColor.ORO

        // Aquí llamas a la lógica de ataque puro que ya limpiamos antes
        return esCasillaAmenazadaPorGeometria(rey.position, colorOponente, piezas, size)
    }

    // --- CAMINO B: ATAQUE PURO (SIN RECURSIÓN) ---
    // CORREGIDO: Acceso correcto a .position.row y .position.col
// --- Dentro de MoveValidator.kt ---
    fun esCasillaAmenazadaPorGeometria(pos: Position, atacanteColor: PieceColor, piezas: List<ChessPiece>, size: Int): Boolean {
        return piezas.any { pieza ->
            if (pieza.color != atacanteColor) return@any false

            val dRow = pos.row - pieza.position.row
            val dCol = pos.col - pieza.position.col
            val absRow = abs(dRow)
            val absCol = abs(dCol)

            when (pieza.type) {
                PieceType.PEON -> {
                    val dir = if (pieza.color == PieceColor.ORO) -1 else 1
                    (dRow == dir) && (absCol == 1)
                }
                PieceType.CABALLO -> (absRow == 2 && absCol == 1) || (absRow == 1 && absCol == 2)
                PieceType.REY -> absRow <= 1 && absCol <= 1
                PieceType.TORRE -> (dRow == 0 || dCol == 0) && !estaBloqueado(pieza.position, pos, piezas)
                PieceType.ALFIL -> (absRow == absCol) && !estaBloqueado(pieza.position, pos, piezas)
                PieceType.REINA -> ((dRow == 0 || dCol == 0) || (absRow == absCol)) && !estaBloqueado(pieza.position, pos, piezas)
                // Se debe incluir cualquier otro tipo de pieza aunque devuelva false
                else -> false
            }
        }
    }

    private fun estaBloqueado(origen: Position, destino: Position, piezas: List<ChessPiece>): Boolean {
        val dRow = Integer.signum(destino.row - origen.row)
        val dCol = Integer.signum(destino.col - origen.col)
        var currRow = origen.row + dRow
        var currCol = origen.col + dCol
        while (currRow != destino.row || currCol != destino.col) {
            if (piezas.any { it.position.row == currRow && it.position.col == currCol }) return true
            currRow += dRow
            currCol += dCol
        }
        return false
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


    // ... (Mantén aquí tus funciones existentes: obtenerMovimientosPeon, obtenerMovimientosTorre, etc.)
    // SOLO ASEGÚRATE DE QUE NO LLAMEN A CASILLAESTAATACADA

    private fun obtenerMovimientosPeon(p: ChessPiece, piezas: List<ChessPiece>, size: Int): List<Position> {
        val moves = mutableListOf<Position>()
        val dir = if (p.color == PieceColor.ORO) -1 else 1
        val f1 = Position(p.position.row + dir, p.position.col)
        if (esPosicionValida(f1.row, f1.col, size) && piezas.none { it.position == f1 }) {
            moves.add(f1)
            if (size == 8) { // Lógica mantenida para 8x8
                val base = if (p.color == PieceColor.ORO) 6 else 1
                val f2 = Position(p.position.row + (dir * 2), p.position.col)
                if (p.position.row == base && piezas.none { it.position == f2 }) moves.add(f2)
            }
        }
        listOf(-1, 1).forEach { colOffset ->
            val diag = Position(p.position.row + dir, p.position.col + colOffset)
            if (piezas.any { it.position == diag && it.color != p.color }) moves.add(diag)
        }
        return moves
    }

    private fun obtenerMovimientosTorre(pieza: ChessPiece, piezas: List<ChessPiece>, size: Int): List<Position> {
        val moves = mutableListOf<Position>()
        val dirs = listOf(0 to 1, 0 to -1, 1 to 0, -1 to 0)
        for ((dr, dc) in dirs) {
            var r = pieza.position.row + dr; var c = pieza.position.col + dc
            while (esPosicionValida(r, c, size)) {
                val pos = Position(r, c)
                val target = piezas.find { it.position == pos }
                if (target == null) moves.add(pos)
                else { if (target.color != pieza.color) moves.add(pos); break }
                r += dr; c += dc
            }
        }
        return moves
    }

    private fun obtenerMovimientosAlfil(pieza: ChessPiece, piezas: List<ChessPiece>, size: Int): List<Position> {
        val moves = mutableListOf<Position>()
        val dirs = listOf(1 to 1, 1 to -1, -1 to 1, -1 to -1)
        for ((dr, dc) in dirs) {
            var r = pieza.position.row + dr; var c = pieza.position.col + dc
            while (esPosicionValida(r, c, size)) {
                val pos = Position(r, c)
                val target = piezas.find { it.position == pos }
                if (target == null) moves.add(pos)
                else { if (target.color != pieza.color) moves.add(pos); break }
                r += dr; c += dc
            }
        }
        return moves
    }

    private fun obtenerMovimientosCaballo(pieza: ChessPiece, piezas: List<ChessPiece>, size: Int): List<Position> {
        val moves = mutableListOf<Position>()
        val saltos = listOf(-2 to 1, -2 to -1, 2 to 1, 2 to -1, -1 to 2, -1 to -2, 1 to 2, 1 to -2)
        for ((dr, dc) in saltos) {
            val r = pieza.position.row + dr; val c = pieza.position.col + dc
            if (esPosicionValida(r, c, size)) {
                val target = piezas.find { it.position == Position(r, c) }
                if (target == null || target.color != pieza.color) moves.add(Position(r, c))
            }
        }
        return moves
    }

    private fun obtenerMovimientosRey(rey: ChessPiece, piezas: List<ChessPiece>, size: Int): List<Position> {
        val moves = mutableListOf<Position>()
        // 1. Movimientos normales de rey
        for (dr in -1..1) for (dc in -1..1) {
            if (dr == 0 && dc == 0) continue
            val r = rey.position.row + dr; val c = rey.position.col + dc
            if (esPosicionValida(r, c, size)) {
                val pos = Position(r, c)
                val target = piezas.find { it.position == pos }
                if (target == null || target.color != rey.color) moves.add(pos)
            }
        }

        // 2. Lógica de Enroque (Solo si el rey no se ha movido - requerirías un flag en ChessPiece)
        // Supongamos que tu ChessPiece tiene un atributo 'haMovido'
        // Aquí validamos: Rey en fila base, torres en columnas 0 y 7, y camino libre
        val filaBase = if (rey.color == PieceColor.ORO) 7 else 0
        if (rey.position.row == filaBase && rey.position.col == 4) {
            // Enroque corto (hacia columna 7)
            if (estaLibreYNoAtacado(filaBase, 5, 6, piezas)) moves.add(Position(filaBase, 6))
            // Enroque largo (hacia columna 0)
            if (estaLibreYNoAtacado(filaBase, 1, 3, piezas)) moves.add(Position(filaBase, 2))
        }
        return moves
    }

    private fun estaLibreYNoAtacado(row: Int, colInicio: Int, colFin: Int, piezas: List<ChessPiece>): Boolean {
        for (c in colInicio..colFin) {
            if (piezas.any { it.position.row == row && it.position.col == c }) return false
        }
        return true
    }
}