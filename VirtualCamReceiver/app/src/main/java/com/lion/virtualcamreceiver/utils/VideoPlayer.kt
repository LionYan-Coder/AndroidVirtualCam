package com.lion.virtualcamreceiver.utils

import android.content.Context
import android.media.MediaPlayer
import android.view.Surface


class VideoPlayer(private val videoFilePath: String) {

    private var mediaPlayer: MediaPlayer? = null

    fun startPlaying(surface: Surface) {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(videoFilePath)
            setSurface(surface)
            setOnPreparedListener { start() }
            prepareAsync()
        }
    }

    fun stopPlaying() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
    }
}