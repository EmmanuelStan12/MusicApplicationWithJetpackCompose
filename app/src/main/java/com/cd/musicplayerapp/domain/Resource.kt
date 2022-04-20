package com.cd.musicplayerapp.domain

sealed class Resource<T>(
    val data: T? = null,
    val loading: Boolean = false,
    val error: String? = null
) {
    class Success<T>(data: T): Resource<T>(data, loading = false)

    class Error<T>(error: String): Resource<T>(error = error, loading = false)

    class Loading<T>(): Resource<T>(loading = true)
}
