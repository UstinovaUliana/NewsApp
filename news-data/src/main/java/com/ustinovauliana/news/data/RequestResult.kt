package com.ustinovauliana.news.data

sealed class RequestResult<out E: Any>(internal val data: E? = null) {

    class InProgress<E: Any>(data: E? = null) : RequestResult<E>(data)
    class Success<E : Any>(data: E) : RequestResult<E>(data)
    class Error<E: Any>(data: E? = null, val error: Throwable? = null) : RequestResult<E>(data)
}

internal fun <InType: Any, OutType: Any> RequestResult<InType>.map(mapper: (InType) -> OutType): RequestResult<OutType> {
    return when (this) {
        is RequestResult.Success -> RequestResult.Success(mapper(checkNotNull(data)))
        is RequestResult.Error -> RequestResult.Error(data?.let(mapper))
        is RequestResult.InProgress -> RequestResult.InProgress(data?.let(mapper))
    }
}

internal fun <T:Any> Result<T>.toRequestResult(): RequestResult<T> {
    return when {
        isSuccess -> RequestResult.Success(getOrThrow())
        isFailure -> RequestResult.Error()
        else -> error("Invalid branch")
    }
}