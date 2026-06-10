package com.jasc.jasc_chess.audio

import com.jasc.jasc_chess.audio.SoundManager
import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.jasc.jasc_chess.R

// Usamos object para que sea un Singleton accesible desde cualquier parte
object SoundManager {
    private var soundPool: SoundPool? = null
    private val soundMap = mutableMapOf<Int, Int>()

    // Inicializamos el SoundPool
    fun init(context: Context) {
        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5) // Podemos reproducir hasta 5 sonidos a la vez
            .setAudioAttributes(attributes)
            .build()

        // Cargamos los sonidos y guardamos su ID
        // Debes tener estos archivos en res/raw/
        soundMap[R.raw.move] = soundPool!!.load(context, R.raw.move, 1)
        soundMap[R.raw.capture] = soundPool!!.load(context, R.raw.capture, 1)
        soundMap[R.raw.check] = soundPool!!.load(context, R.raw.check, 1)
        // --- AGREGA ESTA LÍNEA PARA REGISTRAR EL CABALLO ---
        soundMap[R.raw.knight] = soundPool!!.load(context, R.raw.knight, 1)
        // En SoundManager.kt -> fun init(context: Context)
        soundMap[R.raw.victoria] = soundPool!!.load(context, R.raw.victoria, 1)
        soundMap[R.raw.mate] = soundPool!!.load(context, R.raw.mate, 1)
    }

    // Función para reproducir un sonido por su ID de recurso
    fun play(resId: Int) {
        val soundId = soundMap[resId] ?: return
        soundPool?.play(soundId, 1f, 1f, 1, 0, 1f)
    }

    // Liberar memoria cuando la app se cierra
    fun release() {
        soundPool?.release()
        soundPool = null
    }
}