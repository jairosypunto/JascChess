package com.jasc.jasc_chess.model

enum class PieceType { PEON, TORRE, CABALLO, ALFIL, REINA, REY }
enum class PieceColor { ORO, PLATA }

data class Position(val row: Int, val col: Int)

data class ChessPiece(
    val id: String,
    val type: PieceType,
    val color: PieceColor,
    val position: Position,
    val hasMoved: Boolean = false, // <--- Si le das un valor por defecto, ya no estás obligado a ponerlo siempre
    val isFallen: Boolean = false // <--- NUEVA PROPIEDAD
)