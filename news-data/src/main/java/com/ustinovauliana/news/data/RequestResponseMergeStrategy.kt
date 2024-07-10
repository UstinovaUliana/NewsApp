package com.ustinovauliana.news.data

import com.ustinovauliana.news.data.RequestResult.Error
import com.ustinovauliana.news.data.RequestResult.InProgress
import com.ustinovauliana.news.data.RequestResult.Success

interface MergeStrategy<E> {

    fun merge(first: E, second: E): E
}

internal class RequestResponseMergeStrategy<T : Any> : MergeStrategy<RequestResult<T>> {
    @Suppress("CyclomaticComplexMethod")
    override fun merge(cache: RequestResult<T>, server: RequestResult<T>): RequestResult<T> {
        return when {
            cache is InProgress && server is InProgress -> merge(cache, server)
            cache is Success && server is InProgress -> merge(cache, server)
            cache is InProgress && server is Success -> merge(cache, server)
            cache is Success && server is Error -> merge(cache, server)
            cache is Success && server is Success -> merge(cache, server)
            cache is InProgress && server is Error -> merge(cache, server)
            cache is Error && server is InProgress -> merge(cache, server)
            cache is Error && server is Error -> merge(cache, server)
            cache is Error && server is Success -> merge(cache, server)

            else -> error("Unimplemented branch")
        }
    }

    private fun merge(
        cache: InProgress<T>,
        server: InProgress<T>
    ): RequestResult<T> {
        return when {
            server.data != null -> return InProgress(server.data)
            else -> InProgress(cache.data)
        }
    }

    @Suppress("UnusedParameter")
    private fun merge(
        cache: Success<T>,
        server: InProgress<T>
    ): RequestResult<T> {
        return InProgress(cache.data)
    }

    @Suppress("UnusedParameter")
    private fun merge(
        cache: InProgress<T>,
        server: Success<T>
    ): RequestResult<T> {
        return InProgress(server.data)
    }

    @Suppress("UnusedParameter")
    private fun merge(
        cache: Success<T>,
        server: Success<T>
    ): RequestResult<T> {
        return Success(server.data)
    }

    private fun merge(
        cache: Success<T>,
        server: Error<T>
    ): RequestResult<T> {
        return Error(cache.data, error = server.error)
    }

    private fun merge(
        cache: InProgress<T>,
        server: Error<T>
    ): RequestResult<T> {
        return Error(server.data ?: cache.data, error = server.error)
    }

    @Suppress("UnusedParameter")
    private fun merge(
        cache: Error<T>,
        server: InProgress<T>
    ): RequestResult<T> {
        return InProgress(server.data)
    }

    @Suppress("UnusedParameter")
    private fun merge(
        cache: Error<T>,
        server: Success<T>
    ): RequestResult<T> {
        return Success(server.data)
    }

    private fun merge(
        cache: Error<T>,
        server: Error<T>
    ): RequestResult<T> {
        return Error(server.data ?: cache.data, error = server.error)
    }
}
