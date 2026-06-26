package com.jasc.jasc_chess.data.local

import android.content.Context
import android.util.Log
import androidx.core.content.edit // Import necesario para usar .edit { }

object PreferencesManager {
    private const val PREFS_NAME = "JascChessPrefs"
    private const val KEY_NIVEL_MAXIMO = "nivel_maximo"
    private const val KEY_PUNTOS = "puntos_totales"
    private const val KEY_NIVEL_COMPLETADOS = "niveles_completados_set" // Esta faltaba

    fun marcarNivelComoCompletado(nivel: Int, context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val completados = prefs.getStringSet(KEY_NIVEL_COMPLETADOS, mutableSetOf())?.toMutableSet() ?: mutableSetOf()

        completados.add(nivel.toString())

        // Usando sintaxis KTX recomendada
        prefs.edit {
            putStringSet(KEY_NIVEL_COMPLETADOS, completados)
        }
    }

    fun esNivelCompletado(nivel: Int, context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val completados = prefs.getStringSet(KEY_NIVEL_COMPLETADOS, emptySet())
        return completados?.contains(nivel.toString()) == true
    }

    fun guardarNivelMaximo(nivel: Int, context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if (nivel > prefs.getInt(KEY_NIVEL_MAXIMO, 1)) {
            prefs.edit {
                putInt(KEY_NIVEL_MAXIMO, nivel)
            }
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

        prefs.edit {
            putInt(KEY_PUNTOS, nuevoTotal)
        }
        Log.d("JascChess", "Puntos: $actuales -> $nuevoTotal (Cambio: $puntosCambio)")
    }

    fun obtenerPuntos(context: Context): Int {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getInt(KEY_PUNTOS, 0)
    }
}