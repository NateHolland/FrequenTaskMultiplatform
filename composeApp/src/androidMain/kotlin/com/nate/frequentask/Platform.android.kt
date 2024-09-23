package com.nate.frequentask

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val os: OS = OS.ANDROID
}

actual fun getPlatform(): Platform = AndroidPlatform()