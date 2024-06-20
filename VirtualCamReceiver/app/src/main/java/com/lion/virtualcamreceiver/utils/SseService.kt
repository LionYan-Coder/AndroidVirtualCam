package com.lion.virtualcamreceiver.utils

import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lion.virtualcamreceiver.MainHook.Companion.origin_preview_camera
import de.robv.android.xposed.XposedBridge
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import java.util.concurrent.TimeUnit

class SseService() {

    companion object {
        private val apiUrl = AppInstance.API_URL
        private val gson = Gson()
        private val CameraDataType = object : TypeToken<ApiResponse<CameraData>>() {}.type



        fun initSse(){
            var clientId = AppInstance.ClientId
            var httpClient = OkHttpClient().newBuilder().connectTimeout(5 * 60 * 60 * 1000, TimeUnit.MILLISECONDS).build()
            Log.d("sse","config: $apiUrl, $clientId")
//        XposedBridge.log("config: $apiUrl, $clientId")
            val request = Request.Builder()
                .url("${apiUrl}/sse/connect/${clientId}")
                .build()
            EventSources.createFactory(httpClient).newEventSource(request,object :EventSourceListener(){
                override fun onOpen(eventSource: EventSource, response: okhttp3.Response) {
//                XposedBridge.log("SSE connection opened")
                    Log.d("sse","SSE connection opened")
//                    Toast.makeText(AppInstance.AppContext!!,"服务已连接！",Toast.LENGTH_SHORT).show()
                }

                override fun onClosed(eventSource: EventSource) {
//                XposedBridge.log("SSE connection closed")
                    Log.d("sse","SSE connection closed")
//                    Toast.makeText(AppInstance.AppContext!!,"服务已关闭！",Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(eventSource: EventSource, t: Throwable?, response: okhttp3.Response?) {
//                XposedBridge.log("SSE connection error: ${t?.message}")
                    Log.d("sse","SSE connection error: ${t?.message}")
//                    Toast.makeText(AppInstance.AppContext!!,"服务连接出错！",Toast.LENGTH_LONG).show()
                }

                override fun onEvent(eventSource: EventSource, id: String?, type: String?, data: String) {
                    Log.d("sse","heartbeat: $data ${AppInstance.AppContext}")
                    if (data.startsWith("{")){
                        Log.d("sse","SSE event received:  $data")
//                    XposedBridge.log("SSE event received: $id, $type, $data")
                        var response: ApiResponse<CameraData?>  = gson.fromJson(data,CameraDataType)
                        if (response.code == 200 && response.data != null){

                            when(response.data?.cameraStatus){
                                "DISABLE" -> {
                                    Log.d("sse","AppInstance.AppContext: ${AppInstance.AppContext}")
                                    origin_preview_camera?.stopPreview()
//                                    FileUtils.writeBooleanToFile(AppInstance.AppContext!!,IS_PAUSED_FILE)
//                                    val currentFrame = FileUtils.readFromFile(AppInstance.AppContext!!,CURRENT_FRAME_FILE)
//                                    Log.d("sse","currentFrame: $currentFrame")
//                                    if (currentFrame != null) {
//                                        val lastFrame = ByteArray(currentFrame.size)
//                                        System.arraycopy(currentFrame, 0, lastFrame, 0, currentFrame.size)
//                                        AppInstance.AppContext?.let {
//                                            FileUtils.writeToFile(it, CURRENT_FRAME_FILE, currentFrame)
//                                        }
//                                        FileUtils.writeToFile(AppInstance.AppContext!!,LAST_FRAME_FILE,lastFrame)
//
//                                        Log.d("sse","DISABLE CameraObj: $origin_preview_camera")
//
//                                        Log.d("sse","mSurfacetexture: $mSurfacetexture")
//                                        mSurface = Surface(mSurfacetexture)
//                                        val width = origin_preview_camera!!.getParameters().getPreviewSize().width
//                                        val height =  origin_preview_camera!!.getParameters().getPreviewSize().height
//
//                                        videoEncoder = VideoEncoder(AppInstance.AppContext!!,width,height,outputPath)
//
//                                        Log.d("sse","videoEncoder: $videoEncoder")
//                                        videoEncoder?.encodeFrame(lastFrame)
//                                        val outputPath = "${AppInstance.AppContext!!.filesDir.path}/$LAST_FRAME_FILE"
//                                        videoPlayer = VideoPlayer(outputPath)
//                                        videoPlayer?.startPlaying(mSurface!!)
//                                        Log.d("sse","lastFrame = currentFrame")
//                                    }
                                }
                                "ENABLED" -> {
                                    Log.d("sse","ENABLED CameraObj: $origin_preview_camera")
                                    origin_preview_camera?.startPreview()
//                                    videoPlayer?.stopPlaying()
//                                    FileUtils.delToFile(AppInstance.AppContext!!,IS_PAUSED_FILE)
//                                    FileUtils.delToFile(AppInstance.AppContext!!,LAST_FRAME_FILE)
                                }
                                else -> {
                                    Log.d("sse","没有匹配数据: $response")
                                }
                            }
                        }

                    }
                }
            })
        }

        fun closeSse(){
            val clientId = AppInstance.ClientId
            val client = OkHttpClient()
            val request  = Request.Builder()
                .url(AppInstance.API_URL + "/sse/close/$clientId")
                .build()
            client.newCall(request).execute()
        }
    }


}