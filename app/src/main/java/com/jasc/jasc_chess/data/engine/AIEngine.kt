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

    fun calcularMejorMovimiento(piezas: List<ChessPiece>, nivel: NivelDificultad): Pair<ChessPiece, Position>? {
        val depth = when(nivel) {
            NivelDificultad.PRINCIPIANTE -> 1
            NivelDificultad.INTERMEDIO -> 2
            NivelDificultad.INFIERNO -> 3
        }

        val aliadas = piezas.filter { it.color == PieceColor.PLATA }
        var mejorMovimiento: Pair<ChessPiece, Position>? = null
        var maxEval = Int.MIN_VALUE

        // Ordenamos movimientos para optimizar la poda Alfa-Beta (capturas primero)
        val movimientos = aliadas.flatMap { p ->
            MoveValidator.obtenerMovimientosValidos(p, piezas).map { p to it }
        }.sortedByDescending { (_, dest) ->
            if (piezas.any { it.position == dest && it.color == PieceColor.ORO }) 1000 else 0
        }

        for ((p, d) in movimientos) {
            val simulacion = simularMovimiento(p, d, piezas)
            val valor = minimax(simulacion, depth - 1, false, Int.MIN_VALUE, Int.MAX_VALUE)

            if (valor > maxEval) {
                maxEval = valor
                mejorMovimiento = p to d
            }
        }
        return mejorMovimiento
    }

    private fun minimax(tablero: List<ChessPiece>, depth: Int, isMax: Boolean, alpha: Int, beta: Int): Int {
        if (depth == 0) return evaluarTablero(tablero)
        var (a, b) = alpha to beta

        if (isMax) {
            var maxE = Int.MIN_VALUE
            for (p in tablero.filter { it.color == PieceColor.PLATA }) {
                for (d in MoveValidator.obtenerMovimientosValidos(p, tablero)) {
                    maxE = maxOf(maxE, minimax(simularMovimiento(p, d, tablero), depth - 1, false, a, b))
                    a = maxOf(a, maxE)
                    if (b <= a) break
                }
            }
            return maxE
        } else {
            var minE = Int.MAX_VALUE
            for (p in tablero.filter { it.color == PieceColor.ORO }) {
                for (d in MoveValidator.obtenerMovimientosValidos(p, tablero)) {
                    minE = minOf(minE, minimax(simularMovimiento(p, d, tablero), depth - 1, true, a, b))
                    b = minOf(b, minE)
                    if (b <= a) break
                }
            }
            return minE
        }
    }

    private fun evaluarTablero(piezas: List<ChessPiece>): Int {
        var score = 0
        for (p in piezas) {
            val v = obtenerValorPieza(p.type)

            // 1. Valor material
            val material = if (p.color == PieceColor.PLATA) v else -v

            // 2. Control Central (premio)
            val central = if (p.position.row in 3..4 && p.position.col in 3..4) 20 else 0

            // 3. Desarrollo (castigo si las piezas menores siguen en casa)
            val desarrollo = if (p.position.row == 0 && (p.type == PieceType.CABALLO || p.type == PieceType.ALFIL)) -15 else 0

            score += material + (if (p.color == PieceColor.PLATA) (central + desarrollo) else -(central + desarrollo))
        }

        // 4. Seguridad del Rey (Penalización severa si el Rey está en jaque)
        if (estaElReyEnJaqueEnSimulacion(PieceColor.PLATA, piezas)) score -= 500
        if (estaElReyEnJaqueEnSimulacion(PieceColor.ORO, piezas)) score += 500

        return score
    }

    fun simularMovimiento(p: ChessPiece, d: Position, piezas: List<ChessPiece>): List<ChessPiece> {
        // Mantenemos la estructura inmutable creando una nueva lista
        return piezas.filterNot { it.position == d || it.id == p.id } + p.copy(position = d)
    }

    fun estaElReyEnJaqueEnSimulacion(color: PieceColor, piezas: List<ChessPiece>): Boolean {
        val rey = piezas.find { it.type == PieceType.REY && it.color == color } ?: return false
        return MoveValidator.casillaEstaAtacada(rey.position, color, piezas)
    }
}