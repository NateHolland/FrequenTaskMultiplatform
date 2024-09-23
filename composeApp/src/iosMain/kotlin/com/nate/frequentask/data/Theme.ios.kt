package com.nate.frequentask.data
import platform.Foundation.NSUUID

actual fun randomUUID(): String = NSUUID().UUIDString()