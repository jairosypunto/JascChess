package com.jasc.jasc_chess.model

data class UserProfile(
    val nombre: String = "Jugador Jasc",
    val victorias: Int = 0,
    val derrotas: Int = 0,
    val nivelActual: String = "Principiante"
)