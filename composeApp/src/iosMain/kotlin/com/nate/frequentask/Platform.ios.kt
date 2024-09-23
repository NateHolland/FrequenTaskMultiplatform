package com.nate.frequentask

import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val os: OS = OS.IOS
}

actual fun getPlatform(): Platform = IOSPlatform()