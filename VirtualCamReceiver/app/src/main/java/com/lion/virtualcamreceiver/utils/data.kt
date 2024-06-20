package com.lion.virtualcamreceiver.utils

data class ApiResponse<T>(
    val code: Int,
    val message: String,
    val data:T
)

data class CameraData(
    val clientType: String,
    val cameraStatus: String,
)