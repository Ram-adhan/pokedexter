package com.inbedroom.pokedexter.data.catchservice

import com.inbedroom.pokedexter.core.ResponseResult
import java.io.IOException

class CatchRepository(private val service: CatchService) {

    suspend fun catchPokemon(): ResponseResult<Boolean> {
        return try {
            val result = service.catchPokemon()
            if (result.isSuccessful && result.body() != null) {
                if (result.body()!!.isCaught) {
                    ResponseResult.Success(data = true)
                } else {
                    ResponseResult.Error(message = "Not Caught")
                }
            } else {
                ResponseResult.Error(message = "Not Caught")
            }
        } catch (e: IOException) {
            ResponseResult.Error(message = e.localizedMessage.orEmpty())
        }
    }

    suspend fun releasePokemon(): ResponseResult<Boolean> {
        return try {
            val result = service.releasePokemon()
            if (result.isSuccessful && result.body() != null) {
                if (result.body()!!.isReleased) {
                    ResponseResult.Success(data = true)
                } else {
                    ResponseResult.Error(message = "Not Released")
                }
            } else {
                ResponseResult.Error(message = "Not Released")
            }
        } catch (e: IOException) {
            ResponseResult.Error(message = e.localizedMessage.orEmpty())
        }
    }
}