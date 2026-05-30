package com.jasc.jasc_chess.data.engine

import com.jasc.jasc_chess.model.*

object FENParser {
    fun parse(fen: String, size: Int): List<ChessPiece> {
        val piezas = mutableListOf<ChessPiece>()
        val tableroPart = fen.split(" ")[0]
        val filas = tableroPart.split("/")

        filas.forEachIndexed { rowIndex, rowString ->
            if (rowIndex >= size) return@forEachIndexed

            var colIndex = 0
            for (char in rowString) {
                if (char.isDigit()) {
                    colIndex += char.toString().toInt()
                } else {
                    val color = if (char.isUpperCase()) PieceColor.ORO else PieceColor.PLATA
                    val type = obtenerTipo(char) // Llama a la función de abajo

                    if (colIndex < size) {
                        piezas.add(
                            ChessPiece(
                                id = "${type}_${color}_${rowIndex}_${colIndex}",
                                type = type,
                                color = color,
                                position = Position(rowIndex, colIndex)
                            )
                        )
                        colIndex++
                    }
                }
            }
        }
        return piezas
    }

    // Esta función ya no estará en gris porque es usada arriba
    private fun obtenerTipo(c: Char): PieceType = when (c.lowercaseChar()) {
        'p' -> PieceType.PEON
        'r' -> PieceType.TORRE
        'n' -> PieceType.CABALLO
        'b' -> PieceType.ALFIL
        'q' -> PieceType.REINA
        'k' -> PieceType.REY
        else -> PieceType.PEON
    }
}