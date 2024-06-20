package com.lion.virtualcamreceiver.utils
import android.os.Build
import java.util.*

class Device {
    fun getUniqueDeviceId(): String {
        // 设备品牌
        val brand = Build.BRAND
        // 设备制造商
        val manufacturer = Build.MANUFACTURER
        // 设备型号
        val model = Build.MODEL
        // 设备硬件名
        val hardware = Build.HARDWARE

        val timestamp = System.currentTimeMillis().toString()
        // 将所有信息拼接成一个字符串
        val longId = brand + manufacturer + model + hardware + timestamp

        // 生成唯一ID
        return UUID.nameUUIDFromBytes(longId.toByteArray()).toString()
    }


}