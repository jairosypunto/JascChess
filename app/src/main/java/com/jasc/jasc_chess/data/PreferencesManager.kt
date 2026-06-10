package com.jasc.jasc_chess.data

import android.content.Context

object PreferencesManager {

    private const val PREFS_NAME = "JascChessPrefs"
    private const val KEY_NIVEL_MAXIMO = "nivel_maximo"

    /**
     * Guarda el nivel máximo alcanzado.
     * Solo actualiza si el nuevo nivel es mayor al guardado anteriormente.
     */
    fun guardarNivelMaximo(nivel: Int, context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val actual = prefs.getInt(KEY_NIVEL_MAXIMO, 1)

        if (nivel > actual) {
            prefs.edit().putInt(KEY_NIVEL_MAXIMO, nivel).apply()
        }
    }

    /**
     * Obtiene el nivel máximo desbloqueado por el jugador.
     * Por defecto retorna 1 (el primer nivel).
     */
    fun obtenerNivelMaximo(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_NIVEL_MAXIMO, 1)
    }

    /**
     * Opcional: Útil para la función de "Reiniciar progreso"
     */
    fun resetearProgreso(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_NIVEL_MAXIMO, 1).apply()
    }
}

