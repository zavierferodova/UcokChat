package com.robbies.ucokchat.util

import kotlinx.coroutines.*

class CustomScope : CoroutineScope {
    private val job = Job()
    override val coroutineContext = Dispatchers.Default + job

    fun cancelScope() {
        job.cancel()
    }
}
