package com.lion.virtualcamcontroller

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.lion.virtualcamreceiver.utils.AppEnv
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class HomeViewModel: ViewModel() {
    var isPaused = mutableStateOf(false)
    var loading = mutableStateOf(false)
    private val client = OkHttpClient()
    private val gson = Gson()

    fun handleChangePaused(){
        var cameraStatus = 1
        if (isPaused.value){
            cameraStatus = 0
        }

        var body = CameraData(clientType = 0, cameraStatus = cameraStatus)
        sendRequest(body)

    }



    private fun sendRequest(postData: CameraData) {
        loading.value = true
        val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val jsonBody = gson.toJson(postData)
        val body = jsonBody.toRequestBody(jsonMediaType)
        val request = Request.Builder()
            .url(AppEnv.API_URL + "/sse/camera")
            .post(body)
            .build()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                val responseData = response.body?.string()
                var res: ApiResponse<*> = gson.fromJson(responseData, ApiResponse::class.java)
                if (res.code == 200){
                    isPaused.value = !isPaused.value
                }
                println("Response: $responseData")
            } catch (e: IOException) {
                println("Error: ${e.message}")
            } finally {
                loading.value = false
            }
        }
    }
}