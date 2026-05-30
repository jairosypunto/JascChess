package com.jasc.jasc_chess.data.engine

import com.jasc.jasc_chess.model.*

object AIEngine {

    private fun obtenerValorPieza(t: PieceType) = when(t) {
        PieceType.PEON -> 100
        PieceType.CABALLO -> 320
        PieceType.ALFIL -> 330
        PieceType.TORRE -> 500
        PieceType.REINA -> 950
        PieceType.REY -> 20000
    }

    fun calcularMejorMovimiento(piezas: List<ChessPiece>, nivel: NivelDificultad, size: Int): Pair<ChessPiece, Position>? {
        val depth = when(nivel) {
            NivelDificultad.PRINCIPIANTE -> 1
            NivelDificultad.INTERMEDIO -> 2
            NivelDificultad.INFIERNO -> 3
        }

        val aliadas = piezas.filter { it.color == PieceColor.PLATA }
        var mejorMovimiento: Pair<ChessPiece, Position>? = null
        var maxEval = Int.MIN_VALUE

        val movimientos = aliadas.flatMap { p ->
            MoveValidator.obtenerMovimientosValidos(p, piezas, size).map { p to it }
        }.sortedByDescending { (_, dest) ->
            if (piezas.any { it.position == dest && it.color == PieceColor.ORO }) 1000 else 0
        }

        for ((p, d) in movimientos) {
            val simulacion = simularMovimiento(p, d, piezas)
            val valor = minimax(simulacion, depth - 1, false, Int.MIN_VALUE, Int.MAX_VALUE, size)

            if (valor > maxEval) {
                maxEval = valor
                mejorMovimiento = p to d
            }
        }
        return mejorMovimiento
    }

    private fun minimax(tablero: List<ChessPiece>, depth: Int, isMax: Boolean, alpha: Int, beta: Int, size: Int): Int {
        if (depth == 0) return evaluarTablero(tablero, size)
        var (a, b) = alpha to beta

        if (isMax) {
            var maxE = Int.MIN_VALUE
            for (p in tablero.filter { it.color == PieceColor.PLATA }) {
                for (d in MoveValidator.obtenerMovimientosValidos(p, tablero, size)) {
                    maxE = maxOf(maxE, minimax(simularMovimiento(p, d, tablero), depth - 1, false, a, b, size))
                    a = maxOf(a, maxE)
                    if (b <= a) break
                }
            }
            return maxE
        } else {
            var minE = Int.MAX_VALUE
            for (p in tablero.filter { it.color == PieceColor.ORO }) {
                for (d in MoveValidator.obtenerMovimientosValidos(p, tablero, size)) {
                    minE = minOf(minE, minimax(simularMovimiento(p, d, tablero), depth - 1, true, a, b, size))
                    b = minOf(b, minE)
                    if (b <= a) break
                }
            }
            return minE
        }
    }

    private fun evaluarTablero(piezas: List<ChessPiece>, size: Int): Int {
        var score = 0
        for (p in piezas) {
            val v = obtenerValorPieza(p.type)
            val material = if (p.color == PieceColor.PLATA) v else -v

            val centroMin = size / 2 - 1
            val centroMax = size / 2
            val central = if (p.position.row in centroMin..centroMax && p.position.col in centroMin..centroMax) 20 else 0
            val desarrollo = if (p.position.row == 0 && (p.type == PieceType.CABALLO || p.type == PieceType.ALFIL)) -15 else 0

            score += material + (if (p.color == PieceColor.PLATA) (central + desarrollo) else -(central + desarrollo))
        }

        if (estaElReyEnJaqueEnSimulacion(PieceColor.PLATA, piezas, size)) score -= 500
        if (estaElReyEnJaqueEnSimulacion(PieceColor.ORO, piezas, size)) score += 500

        return score
    }

    fun simularMovimiento(p: ChessPiece, d: Position, piezas: List<ChessPiece>): List<ChessPiece> {
        return piezas.filterNot { it.position == d || it.id == p.id } + p.copy(position = d)
    }

    fun estaElReyEnJaqueEnSimulacion(color: PieceColor, piezas: List<ChessPiece>, size: Int): Boolean {
        val rey = piezas.find { it.type == PieceType.REY && it.color == color } ?: return false
        val colorOponente = if (color == PieceColor.ORO) PieceColor.PLATA else PieceColor.ORO

        // Nombre exacto definido en MoveValidator
        return MoveValidator.esCasillaAmenazadaPorGeometria(rey.position, colorOponente, piezas, size)
    }
}