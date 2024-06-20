package com.lion.virtualcamreceiver.utils

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

const val CURRENT_FRAME_FILE = "current_frame.dat"
const val IS_PAUSED_FILE = "is_paused.dat"
const val CLIENT_FILE = "client.txt"
const val LAST_FRAME_FILE = "last_frame.mp4"

object FileUtils {

    fun writeToFile(context: Context, fileName: String, data: ByteArray) {
        val file = File(context.filesDir, fileName)
        file.writeBytes(data)
    }

    fun readFromFile(context: Context, fileName: String): ByteArray? {
        val file = File(context.filesDir, fileName)
        if (!file.exists()) {
            return null
        }
        return file.readBytes()
    }

    fun writeBooleanToFile(context: Context, fileName: String) {
        val file = File(context.filesDir, fileName)
        file.createNewFile()
    }

    fun readBooleanFromFile(context: Context, fileName: String): Boolean {
        val file = File(context.filesDir, fileName)
        return file.exists()
    }


    fun writeStringToFile(context: Context, fileName: String, data: String) {
        val file = File(context.filesDir, fileName)
        if (!file.exists()){

        }
        file.writeText(data)
    }

    fun readStringFromFile(context: Context, fileName: String): String? {
        val file = File(context.filesDir, fileName)
        if (!file.exists()) {
            return null
        }
        return file.readText()
    }

    fun delToFile(context: Context, fileName: String) {
        val file = File(context.filesDir, fileName)
        if (file.exists()){
            file.delete()
        }
    }
}
