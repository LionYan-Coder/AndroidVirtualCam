package com.lion.virtualcamreceiver

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.view.Surface
import android.widget.Toast
import com.lion.virtualcamreceiver.utils.AppInstance
import com.lion.virtualcamreceiver.utils.CLIENT_FILE
import com.lion.virtualcamreceiver.utils.CURRENT_FRAME_FILE
import com.lion.virtualcamreceiver.utils.Device
import com.lion.virtualcamreceiver.utils.FileUtils
import com.lion.virtualcamreceiver.utils.IS_PAUSED_FILE
import com.lion.virtualcamreceiver.utils.LAST_FRAME_FILE
import com.lion.virtualcamreceiver.utils.SseService
import com.lion.virtualcamreceiver.utils.VideoEncoder
import com.lion.virtualcamreceiver.utils.VideoPlayer
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.math.min

class MainHook: IXposedHookLoadPackage {

    companion object {
        var origin_preview_camera: Camera? = null
//        var mSurfacetexture:SurfaceTexture? = null
//        var mSurface: Surface? = null
//        var videoEncoder: VideoEncoder? = null
//        var videoPlayer: VideoPlayer? = null
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName == "com.tencent.mm") {


            XposedHelpers.findAndHookMethod(
                "android.app.Instrumentation", lpparam.classLoader, "callApplicationOnCreate",
                Application::class.java, object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam?) {
                        super.afterHookedMethod(param)
                        if (param!!.args[0] is Application) {
                            if (AppInstance.AppContext == null){
                                AppInstance.AppContext = (param.args[0] as Application).applicationContext
                                Log.d("sse", "AppInstance.AppContext : ${AppInstance.AppContext}")
                            }
                        }
                    }
                }
            )

            XposedHelpers.findAndHookMethod("android.hardware.Camera", lpparam.classLoader, "setPreviewTexture",
                SurfaceTexture::class.java, object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (param.args[0] == null) {
                            return
                        }
                        // 将当前setPreviewTexture hook的camera示例给 origin_preview_camera
                        origin_preview_camera = param.thisObject as Camera
                        // 将当前setPreviewTexture hook的图像帧给original_c1_preview_SurfaceTexture
//                        mSurfacetexture = param.args[0] as SurfaceTexture
                    }
                })

            XposedHelpers.findAndHookMethod("android.hardware.Camera", lpparam.classLoader, "startPreview", object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    log("开始预览 ${AppInstance.ClientId}")
                    SseService.initSse()
                }
//                override fun afterHookedMethod(param: MethodHookParam) {
//                    val camera = param.thisObject as Camera
//                    setPreviewCallbackWithBuffer(camera)
//                }
            })

            XposedHelpers.findAndHookMethod("android.hardware.Camera",lpparam.classLoader,"release",object : XC_MethodHook(){
                override fun beforeHookedMethod(param: MethodHookParam) {
//                    FileUtils.delToFile(AppInstance.AppContext!!,IS_PAUSED_FILE)
//                    FileUtils.delToFile(AppInstance.AppContext!!, LAST_FRAME_FILE)
//                    FileUtils.delToFile(AppInstance.AppContext!!, CURRENT_FRAME_FILE)
//                    Log.d("sse","已删除文件")
//                    origin_preview_camera?.release()

//                    mSurface?.release()
//                    videoEncoder?.release()
                }
            })
//            XposedHelpers.findAndHookMethod(
//                Camera::class.java, "setPreviewCallbackWithBuffer",
//                Camera.PreviewCallback::class.java,
//                object : XC_MethodHook() {
//                    override fun beforeHookedMethod(param: MethodHookParam) {
//                        XposedBridge.log("预览回调")
//                        val originalCallback = param.args[0] as Camera.PreviewCallback?
//                        Log.d("sse","originalCallback: $originalCallback")
//                        var currentFrame =
//                            AppInstance.AppContext?.let { FileUtils.readFromFile(it,CURRENT_FRAME_FILE) }
//                        var isPaused =
//                            AppInstance.AppContext?.let { FileUtils.readBooleanFromFile(it,IS_PAUSED_FILE) }
//                        var lastFrame = AppInstance.AppContext?.let { FileUtils.readFromFile(it,LAST_FRAME_FILE) }
//                        param.args[0] = Camera.PreviewCallback { data, camera ->
//                            if (isPaused == true && lastFrame != null) {
//                                // 使用最后一帧数据替换当前数据
//                                System.arraycopy(lastFrame, 0, data, 0, Math.min(lastFrame!!.size, data.size))
//                            }
//                            originalCallback?.onPreviewFrame(data, camera)
//                            // 需要将buffer重新添加回去，以继续使用
//                            camera.addCallbackBuffer(data)
//                        }
//                    }
//                })

            // Hook Camera.addCallbackBuffer
//            XposedHelpers.findAndHookMethod(
//                Camera::class.java, "addCallbackBuffer",
//                ByteArray::class.java,
//                object : XC_MethodHook() {
//                    override fun beforeHookedMethod(param: MethodHookParam) {
////                        Log.d("sse","addCallbackBuffer data size ${(param.args[0] as ByteArray).size}")
//                        var currentFrame =
//                            AppInstance.AppContext?.let { FileUtils.readFromFile(it,CURRENT_FRAME_FILE) }
//                        var isPaused =
//                            AppInstance.AppContext?.let { FileUtils.readBooleanFromFile(it,IS_PAUSED_FILE) }
//                        var lastFrame = AppInstance.AppContext?.let { FileUtils.readFromFile(it,LAST_FRAME_FILE) }
//                        if (isPaused == true && lastFrame != null) {
////                            var CameraObj = param.thisObject as Camera
////                            if (mSurface != null && videoPlayer != null) {
////                                Log.d("sse","addCallbackBuffer startPlaying $videoPlayer")
////
////                            }
////                            System.arraycopy(lastFrame , 0, param.args[0], 0, min(lastFrame !!.size, (param.args[0] as ByteArray).size))
//                        }
//                    }
//                })


        }


        if (lpparam.packageName == "com.lion.virtualcamreceiver"){

            var cls = "com.lion.virtualcamreceiver.MainActivity"
            XposedHelpers.findAndHookMethod(cls,lpparam.classLoader,"onCreate",
                Bundle::class.java, object: XC_MethodHook(){
                override fun afterHookedMethod(param: MethodHookParam?) {
                    Log.d("sse","开始预览v1.0")
                    log("开始预览v1.0")
                    Toast.makeText(param?.thisObject as Activity, "模块加载成功v1.0！", Toast.LENGTH_SHORT).show()
                }
            })

            XposedHelpers.findAndHookMethod(
                cls,
                lpparam.classLoader,
                "onDestroy",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam?) {
                        // 在这里可以添加应用关闭时的逻辑
                        log("onDestroy")
                        log("释放相机")
                        Log.d("sse","释放相机")
                        origin_preview_camera = null
                        SseService.closeSse()
//                        val clientId =  AppInstance.ClientId
//                        FileUtils.delToFile(AppInstance.AppContext!!,IS_PAUSED_FILE)
//                        SseService.closeSse()
//                        val client = OkHttpClient()
//                        val request  = Request.Builder()
//                            .url(AppInstance.API_URL + "/sse/close/${clientId}")
//                            .build()
//                        client.newCall(request).execute()
                    }
                }
            )
        }

    }


    private fun setPreviewCallbackWithBuffer(camera: Camera) {
        camera.setPreviewCallbackWithBuffer(object : Camera.PreviewCallback {
            override fun onPreviewFrame(data: ByteArray, camera: Camera) {
//                Log.d("sse","data size: ${data.size}")
//                val lastFrame = AppInstance.AppContext?.let { FileUtils.readFromFile(it,LAST_FRAME_FILE) }
//                val isPaused = AppInstance.AppContext?.let { FileUtils.readBooleanFromFile(it,IS_PAUSED_FILE) }
//                if (isPaused == true && lastFrame != null) {
//                    Log.d("sse","onPreviewFrame 使用最后一帧数据替换当前数据 替换帧lastFrame,$lastFrame")
//                    log("预览回调 替换帧lastFrame,${lastFrame}")
//                    // 使用最后一帧数据替换当前数据
//                    System.arraycopy(lastFrame, 0, data, 0, min(lastFrame!!.size, data.size))
//                }
                AppInstance.AppContext?.let {
                    val currentFrame = ByteArray(data.size)
                    System.arraycopy(data, 0, currentFrame, 0, data.size)
                    AppInstance.AppContext?.let {
                        FileUtils.writeToFile(it, CURRENT_FRAME_FILE, currentFrame)
                    }
                }
                // 重新添加 buffer 以继续使用
                camera.addCallbackBuffer(data)
            }
        })
        // 添加一个缓冲区
        camera.addCallbackBuffer(ByteArray(1024 * 1024)) // 根据相机预览尺寸调整缓冲区大小
    }


    private fun log(message: String) {
        XposedBridge.log("VirtualCameraModule: $message")
    }

//    fun createClientId(context: Context){
//        try {
//            var clientId = FileUtils.readStringFromFile(context, CLIENT_FILE)
//            if (clientId == null){
//                clientId = Device().getUniqueDeviceId()
//                AppInstance.ClientId = clientId
//                FileUtils.writeStringToFile(context, CLIENT_FILE,clientId)
//            }else {
//                AppInstance.ClientId = clientId
//            }
//        } catch (e: Exception) {
//            Log.i("sse","VirtualCameraModule createClientId error ${e.message}")
//        }
//
//    }

}