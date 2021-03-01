package com.example.colearn.network

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T, val pageInfo :Int?) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    data class HeaderInfo<out T : Any>(val data: T):Result<T>()
    object Loading :Result<Nothing>()
}