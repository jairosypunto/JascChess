package com.jasc.jasc_chess.data.engine

import com.jasc.jasc_chess.model.*

object FENParser {
    fun parse(fen: String, size: Int): List<ChessPiece> {
        val pieces = mutableListOf<ChessPiece>()
        val boardPart = fen.split(" ")[0]
        val rows = boardPart.split("/")

        for (r in 0 until minOf(rows.size, size)) {
            var c = 0
            for (char in rows[r]) {
                if (char.isDigit()) {
                    c += char.digitToInt()
                } else if (c < size) {
                    val color = if (char.isUpperCase()) PieceColor.ORO else PieceColor.PLATA
                    val type = when (char.lowercaseChar()) {
                        'r' -> PieceType.TORRE
                        'n' -> PieceType.CABALLO
                        'b' -> PieceType.ALFIL
                        'q' -> PieceType.REINA
                        'k' -> PieceType.REY
                        'p' -> PieceType.PEON
                        else -> PieceType.PEON
                    }
                    // ID Único basado en tipo, color y posición para evitar colisiones
                    val id = "${type}_${color}_${r}_${c}"
                    pieces.add(ChessPiece(id, type, color, Position(r, c), hasMoved = false))
                    c++
                }
            }
        }
        return pieces
    }
}