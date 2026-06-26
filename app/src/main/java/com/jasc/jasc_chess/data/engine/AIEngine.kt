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
            NivelDificultad.AVANZADO -> 3
        }

        val aliadas = piezas.filter { it.color == PieceColor.PLATA }
        val movimientosPosibles = aliadas.flatMap { p ->
            MoveValidator.obtenerMovimientosValidos(p, piezas, size).map { p to it }
        }

        if (movimientosPosibles.isEmpty()) return null

        var maxEval = Int.MIN_VALUE
        val mejoresMovimientos = mutableListOf<Pair<ChessPiece, Position>>()

        for ((p, d) in movimientosPosibles) {
            val simulacion = simularMovimiento(p, d, piezas)
            val valor = minimax(simulacion, depth - 1, false, Int.MIN_VALUE, Int.MAX_VALUE, size)

            if (valor > maxEval) {
                maxEval = valor
                mejoresMovimientos.clear()
                mejoresMovimientos.add(p to d)
            } else if (valor == maxEval) {
                mejoresMovimientos.add(p to d)
            }
        }

        return mejoresMovimientos.randomOrNull()
    }

    private fun minimax(tablero: List<ChessPiece>, depth: Int, isMax: Boolean, alpha: Int, beta: Int, size: Int): Int {
        if (depth == 0 || esFinPartida(tablero)) return evaluarTablero(tablero, size)

        var a = alpha
        var b = beta

        val colorTurno = if (isMax) PieceColor.PLATA else PieceColor.ORO
        val todasLasPiezas = tablero.filter { it.color == colorTurno }

        val movimientos = todasLasPiezas.flatMap { p ->
            MoveValidator.obtenerMovimientosValidos(p, tablero, size).map { d ->
                val captura = tablero.find { it.position == d }
                val valorCaptura = if (captura != null) obtenerValorPieza(captura.type) else 0
                Triple(p, d, valorCaptura)
            }
        }.sortedByDescending { it.third }

        if (isMax) {
            var maxE = Int.MIN_VALUE
            for ((p, d, _) in movimientos) {
                maxE = maxOf(maxE, minimax(simularMovimiento(p, d, tablero), depth - 1, false, a, b, size))
                a = maxOf(a, maxE)
                if (b <= a) break
            }
            return maxE
        } else {
            var minE = Int.MAX_VALUE
            for ((p, d, _) in movimientos) {
                minE = minOf(minE, minimax(simularMovimiento(p, d, tablero), depth - 1, true, a, b, size))
                b = minOf(b, minE)
                if (b <= a) break
            }
            return minE
        }
    }

    private fun evaluarTablero(piezas: List<ChessPiece>, size: Int): Int {
        var score = 0
        for (p in piezas) {
            val v = obtenerValorPieza(p.type)
            val esPlata = p.color == PieceColor.PLATA
            val factor = if (esPlata) 1 else -1

            // 1. Material
            score += (v * factor)

            // 2. Penalización agresiva por estar bajo amenaza
            if (MoveValidator.esCasillaAmenazadaPorGeometria(p.position, if (esPlata) PieceColor.ORO else PieceColor.PLATA, piezas, size)) {
                score -= (v / 2) * factor
            }
        }

        // 3. Seguridad del Rey
        if (estaElReyEnJaqueEnSimulacion(PieceColor.PLATA, piezas, size)) score -= 1000
        if (estaElReyEnJaqueEnSimulacion(PieceColor.ORO, piezas, size)) score += 1000

        return score
    }

    private fun esFinPartida(piezas: List<ChessPiece>): Boolean {
        return piezas.none { it.type == PieceType.REY }
    }

    fun simularMovimiento(p: ChessPiece, d: Position, piezas: List<ChessPiece>): List<ChessPiece> {
        return piezas.filterNot { it.position == d || it.id == p.id } + p.copy(position = d)
    }

    fun estaElReyEnJaqueEnSimulacion(color: PieceColor, piezas: List<ChessPiece>, size: Int): Boolean {
        val rey = piezas.find { it.type == PieceType.REY && it.color == color } ?: return false
        val colorOponente = if (color == PieceColor.ORO) PieceColor.PLATA else PieceColor.ORO
        return MoveValidator.esCasillaAmenazadaPorGeometria(rey.position, colorOponente, piezas, size)
    }
}