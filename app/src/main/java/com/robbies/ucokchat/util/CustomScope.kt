package com.robbies.ucokchat.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class CustomScope : CoroutineScope {
    private val job = Job()
    override val coroutineContext = Dispatchers.Default + job

    fun cancelScope() {
        job.cancel()
    }
}
