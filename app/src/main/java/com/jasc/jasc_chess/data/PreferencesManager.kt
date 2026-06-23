package com.jasc.jasc_chess.data.local

import android.content.Context
import android.util.Log

object PreferencesManager {
    private const val PREFS_NAME = "JascChessPrefs"
    private const val KEY_NIVEL_MAXIMO = "nivel_maximo"
    private const val KEY_PUNTOS = "puntos_totales"

    fun guardarNivelMaximo(nivel: Int, context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if (nivel > prefs.getInt(KEY_NIVEL_MAXIMO, 1)) {
            prefs.edit().putInt(KEY_NIVEL_MAXIMO, nivel).apply()
        }
    }

    fun obtenerNivelMaximo(context: Context): Int {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getInt(KEY_NIVEL_MAXIMO, 1)
    }

    // Acepta valores positivos (sumar) o negativos (cobrar/restar)
    fun guardarPuntos(puntosCambio: Int, context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val actuales = prefs.getInt(KEY_PUNTOS, 0)

        var nuevoTotal = actuales + puntosCambio
        if (nuevoTotal < 0) nuevoTotal = 0

        prefs.edit().putInt(KEY_PUNTOS, nuevoTotal).apply()
        Log.d("JascChess", "Puntos: $actuales -> $nuevoTotal (Cambio: $puntosCambio)")
    }

    fun obtenerPuntos(context: Context): Int {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getInt(KEY_PUNTOS, 0)
    }
}