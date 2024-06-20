package com.lion.virtualcamcontroller

import com.google.gson.Gson
import com.lion.virtualcamreceiver.utils.AppEnv
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.Objects

object NetworkManager {
    private val client = OkHttpClient()
    private val gson = Gson()

    suspend fun postData(url: String,postData: Any): ApiResponse<*>? {
        return withContext(Dispatchers.IO) {
            val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
            val jsonBody = gson.toJson(postData)
            val body = jsonBody.toRequestBody(jsonMediaType)

            val request = Request.Builder()
                .url(AppEnv.API_URL + url)
                .post(body)
                .build()
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: throw NetworkException("Empty response")
            gson.fromJson(responseBody, ApiResponse::class.java)
        }
    }
}


data class ApiResponse<T>(
    val code: Int,
    val message: String,
    val data:T
)

data class CameraData(
    val clientType: Int,
    val cameraStatus: Int,
)

class NetworkException(message: String) : Exception(message)