package com.inbedroom.pokedexter.core

sealed interface ResponseResult<out T> {
    data class Success<out T: Any>(val data: T): ResponseResult<T>
    data class Error(val message: String, val code: Int = ErrorCode.UNKNOWN): ResponseResult<Nothing>
}

object ErrorCode {
    const val UNKNOWN = -99
    const val EMPTY_DATA = 1
}