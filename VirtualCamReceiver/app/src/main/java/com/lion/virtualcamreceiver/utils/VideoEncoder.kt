package com.lion.virtualcamreceiver.utils

import android.content.Context
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.media.MediaMuxer
import android.util.Log
import kotlin.math.min


class VideoEncoder(
    private val context: Context,
    private val width: Int,
    private val height: Int,
    private val videoFilePath: String
) {

    private val frameRate = 30  // 帧率
    private val bitRate = 2_000_000 // 比特率
    private val codecName = "video/avc"

    private var mediaCodec: MediaCodec? = null
    private var mediaMuxer: MediaMuxer? = null
    private var trackIndex = -1
    private var muxerStarted = false

    init {
        setupEncoder()
    }


    private fun setupEncoder() {
        Log.d("video", "video size $width , $height")
        val format = MediaFormat.createVideoFormat(codecName, width, height)
        format.setInteger(
            MediaFormat.KEY_COLOR_FORMAT,
            MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible
        )
        format.setInteger(MediaFormat.KEY_BIT_RATE, bitRate)
        format.setInteger(MediaFormat.KEY_FRAME_RATE, frameRate)
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1)

        mediaCodec = MediaCodec.createEncoderByType(codecName)
        mediaCodec?.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
        mediaMuxer = MediaMuxer(videoFilePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)

        mediaCodec?.start()
    }


    fun encodeFrame(input: ByteArray) {
        val maxInputSize = mediaCodec?.getInputBuffer(0)?.capacity() ?: 0
        Log.d("video", "encodeFrame maxInputSize: $maxInputSize")
        Log.d("video", "encodeFrame input: ${input.size}")
        var offset = 0



        while (offset < input.size) {
            val inputBufferIndex = mediaCodec?.dequeueInputBuffer(10000)
            if (inputBufferIndex != null && inputBufferIndex >= 0) {
                val inputBuffer = mediaCodec?.getInputBuffer(inputBufferIndex)!!
                inputBuffer.clear()

                val chunkSize =
                    min(input.size - offset, inputBuffer.limit() - inputBuffer.position())
                Log.d("video", "encodeFrame chunkSize: $chunkSize")
                inputBuffer.put(input, offset, chunkSize)

                mediaCodec?.queueInputBuffer(
                    inputBufferIndex,
                    0,
                    chunkSize,
                    System.nanoTime() / 1000,
                    0
                )
                offset += chunkSize
            }

            val bufferInfo = MediaCodec.BufferInfo()
            var outputBufferIndex = mediaCodec?.dequeueOutputBuffer(bufferInfo, 10000)
            while (outputBufferIndex != null && outputBufferIndex >= 0) {
                val outputBuffer = mediaCodec?.getOutputBuffer(outputBufferIndex)!!

                if (bufferInfo.flags and MediaCodec.BUFFER_FLAG_CODEC_CONFIG != 0) {
                    bufferInfo.size = 0
                }

                if (bufferInfo.size != 0) {
                    if (!muxerStarted) {
                        val newFormat = mediaCodec?.outputFormat
                        trackIndex = newFormat?.let { mediaMuxer?.addTrack(it) }!!
                        mediaMuxer?.start()
                        muxerStarted = true
                    }

                    outputBuffer.position(bufferInfo.offset)
                    outputBuffer.limit(bufferInfo.offset + bufferInfo.size)
                    mediaMuxer?.writeSampleData(trackIndex, outputBuffer, bufferInfo)
                }

                mediaCodec?.releaseOutputBuffer(outputBufferIndex, false)
                outputBufferIndex = mediaCodec?.dequeueOutputBuffer(bufferInfo, 0)
            }
        }

        release()
    }


    fun release() {
        mediaCodec?.signalEndOfInputStream()
        mediaCodec?.stop()
        mediaCodec?.release()
        mediaMuxer?.stop()
        mediaMuxer?.release()
    }
}