package com.lion.virtualcamreceiver.utils

import android.content.Context
import com.lion.virtualcamreceiver.BuildConfig

object AppInstance {
    const val API_URL: String =  BuildConfig.API_URL
    var AppContext: Context? = null
    var ClientId: String? = null
}