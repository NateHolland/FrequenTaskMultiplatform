package com.nate.frequentask.data

import java.util.UUID

actual fun randomUUID(): String = UUID.randomUUID().toString()