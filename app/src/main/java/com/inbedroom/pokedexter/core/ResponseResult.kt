package com.inbedroom.pokedexter.core

sealed interface ResponseResult<out T> {
    data class Success<out T: Any>(val data: T): ResponseResult<T>
    data class Error(val message: String, val code: Int = -99): ResponseResult<Nothing>
}