package com.robbies.ucokchat.data

interface RepositoryCallback<T> {
    fun onResult(result: Resource<T>)
}