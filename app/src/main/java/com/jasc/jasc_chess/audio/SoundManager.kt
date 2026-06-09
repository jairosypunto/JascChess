package com.jasc.jasc_chess.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.jasc.jasc_chess.R

object SoundManager {
    private var soundPool: SoundPool? = null
    private val soundMap = mutableMapOf<Int, Int>()

    fun init(context: Context) {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()

        // Cargar sonidos
        //soundMap[R.raw.jaque_mate_sound] = soundPool!!.load(context, R.raw.jaque_mate_sound, 1)
        //soundMap[R.raw.level_up_sound] = soundPool!!.load(context, R.raw.level_up_sound, 1)
        //soundMap[R.raw.move_wood_sound] = soundPool!!.load(context, R.raw.move_wood_sound, 1)
    }

    fun play(rawResId: Int) {
        val soundId = soundMap[rawResId] ?: return
        soundPool?.play(soundId, 1f, 1f, 1, 0, 1f)
    }
}