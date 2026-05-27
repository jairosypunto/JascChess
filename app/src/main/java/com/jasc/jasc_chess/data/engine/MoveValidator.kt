package com.jasc.jasc_chess.data.engine

import com.jasc.jasc_chess.model.*

object MoveValidator {
    // Variable global de control para el tamaño del tablero
    var boardSize: Int = 8

    fun obtenerMovimientosValidos(pieza: ChessPiece, piezas: List<ChessPiece>): List<Position> {
        val movimientos = when (pieza.type) {
            PieceType.PEON -> obtenerMovimientosPeon(pieza, piezas)
            PieceType.TORRE -> obtenerMovimientosTorre(pieza, piezas)
            PieceType.ALFIL -> obtenerMovimientosAlfil(pieza, piezas)
            PieceType.REINA -> obtenerMovimientosReina(pieza, piezas)
            PieceType.REY -> obtenerMovimientosRey(pieza, piezas)
            PieceType.CABALLO -> obtenerMovimientosCaballo(pieza, piezas)
        }

        return movimientos.filter { destino ->
            val simulacion = AIEngine.simularMovimiento(pieza, destino, piezas)
            !AIEngine.estaElReyEnJaqueEnSimulacion(pieza.color, simulacion)
        }
    }

    fun obtenerMovimientosBasicos(pieza: ChessPiece, piezas: List<ChessPiece>): List<Position> {
        return when (pieza.type) {
            PieceType.PEON -> obtenerMovimientosPeonBasico(pieza, piezas)
            PieceType.TORRE -> obtenerMovimientosTorre(pieza, piezas)
            PieceType.ALFIL -> obtenerMovimientosAlfil(pieza, piezas)
            PieceType.REINA -> obtenerMovimientosReina(pieza, piezas)
            PieceType.CABALLO -> obtenerMovimientosCaballo(pieza, piezas)
            PieceType.REY -> {
                val moves = mutableListOf<Position>()
                for (dr in -1..1) for (dc in -1..1) {
                    if (dr == 0 && dc == 0) continue
                    val r = pieza.position.row + dr; val c = pieza.position.col + dc
                    if (r in 0 until boardSize && c in 0 until boardSize) moves.add(Position(r, c))
                }
                moves
            }
        }
    }

    fun casillaEstaAtacada(pos: Position, colorAtacado: PieceColor, piezas: List<ChessPiece>): Boolean {
        return piezas.filter { it.color != colorAtacado }.any { enemigo ->
            obtenerMovimientosBasicos(enemigo, piezas).contains(pos)
        }
    }

    private fun obtenerMovimientosPeon(p: ChessPiece, piezas: List<ChessPiece>): List<Position> {
        val moves = mutableListOf<Position>()
        val dir = if (p.color == PieceColor.ORO) -1 else 1
        val f1 = Position(p.position.row + dir, p.position.col)
        if (f1.row in 0 until boardSize && piezas.none { it.position == f1 }) {
            moves.add(f1)
            val base = if (p.color == PieceColor.ORO) boardSize - 2 else 1
            val f2 = Position(p.position.row + (dir * 2), p.position.col)
            if (p.position.row == base && piezas.none { it.position == f2 }) moves.add(f2)
        }
        listOf(-1, 1).forEach { col ->
            val diag = Position(p.position.row + dir, p.position.col + col)
            val target = piezas.find { it.position == diag }
            if (target != null && target.color != p.color) moves.add(diag)
        }
        return moves
    }

    private fun obtenerMovimientosPeonBasico(p: ChessPiece, piezas: List<ChessPiece>): List<Position> {
        val dir = if (p.color == PieceColor.ORO) -1 else 1
        return listOf(-1, 1).map { Position(p.position.row + dir, p.position.col + it) }
            .filter { it.row in 0 until boardSize && it.col in 0 until boardSize }
    }

    private fun obtenerMovimientosTorre(pieza: ChessPiece, piezas: List<ChessPiece>): List<Position> {
        val moves = mutableListOf<Position>()
        val dirs = listOf(0 to 1, 0 to -1, 1 to 0, -1 to 0)
        for ((dr, dc) in dirs) {
            var r = pieza.position.row + dr; var c = pieza.position.col + dc
            while (r in 0 until boardSize && c in 0 until boardSize) {
                val pos = Position(r, c)
                val target = piezas.find { it.position == pos }
                if (target == null) moves.add(pos)
                else { if (target.color != pieza.color) moves.add(pos); break }
                r += dr; c += dc
            }
        }
        return moves
    }

    private fun obtenerMovimientosAlfil(pieza: ChessPiece, piezas: List<ChessPiece>): List<Position> {
        val moves = mutableListOf<Position>()
        val dirs = listOf(1 to 1, 1 to -1, -1 to 1, -1 to -1)
        for ((dr, dc) in dirs) {
            var r = pieza.position.row + dr; var c = pieza.position.col + dc
            while (r in 0 until boardSize && c in 0 until boardSize) {
                val pos = Position(r, c)
                val target = piezas.find { it.position == pos }
                if (target == null) moves.add(pos)
                else { if (target.color != pieza.color) moves.add(pos); break }
                r += dr; c += dc
            }
        }
        return moves
    }

    private fun obtenerMovimientosReina(pieza: ChessPiece, piezas: List<ChessPiece>) = obtenerMovimientosTorre(pieza, piezas) + obtenerMovimientosAlfil(pieza, piezas)

    private fun obtenerMovimientosCaballo(pieza: ChessPiece, piezas: List<ChessPiece>): List<Position> {
        val moves = mutableListOf<Position>()
        val saltos = listOf(-2 to 1, -2 to -1, 2 to 1, 2 to -1, -1 to 2, -1 to -2, 1 to 2, 1 to -2)
        for ((dr, dc) in saltos) {
            val r = pieza.position.row + dr; val c = pieza.position.col + dc
            if (r in 0 until boardSize && c in 0 until boardSize) {
                val target = piezas.find { it.position == Position(r, c) }
                if (target == null || target.color != pieza.color) moves.add(Position(r, c))
            }
        }
        return moves
    }

    private fun obtenerMovimientosRey(rey: ChessPiece, piezas: List<ChessPiece>): List<Position> {
        val moves = mutableListOf<Position>()
        // 1. Movimientos normales del Rey
        for (dr in -1..1) for (dc in -1..1) {
            if (dr == 0 && dc == 0) continue
            val r = rey.position.row + dr; val c = rey.position.col + dc
            if (r in 0 until boardSize && c in 0 until boardSize) {
                val pos = Position(r, c)
                val target = piezas.find { it.position == pos }
                if ((target == null || target.color != rey.color) && !casillaEstaAtacada(pos, rey.color, piezas)) {
                    moves.add(pos)
                }
            }
        }

        // 2. Lógica de Enroque FIDE
        if (!rey.hasMoved && !AIEngine.estaElReyEnJaqueEnSimulacion(rey.color, piezas)) {
            val r = rey.position.row

            // Enroque Corto (lado de la torre en columna boardSize-1)
            val torreCorto = piezas.find { it.type == PieceType.TORRE && it.position.col == boardSize - 1 && it.position.row == r && !it.hasMoved }
            if (torreCorto != null) {
                val caminoLibre = (rey.position.col + 1 until torreCorto.position.col).all { c -> piezas.none { it.position == Position(r, c) } }
                if (caminoLibre && !casillaEstaAtacada(Position(r, rey.position.col + 1), rey.color, piezas)) {
                    moves.add(Position(r, rey.position.col + 2))
                }
            }

            // Enroque Largo (lado de la torre en columna 0)
            val torreLargo = piezas.find { it.type == PieceType.TORRE && it.position.col == 0 && it.position.row == r && !it.hasMoved }
            if (torreLargo != null) {
                val caminoLibre = (1 until rey.position.col).all { c -> piezas.none { it.position == Position(r, c) } }
                if (caminoLibre && !casillaEstaAtacada(Position(r, rey.position.col - 1), rey.color, piezas)) {
                    moves.add(Position(r, rey.position.col - 2))
                }
            }
        }
        return moves
    }
}