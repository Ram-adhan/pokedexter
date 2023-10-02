package com.inbedroom.pokedexter.data.catchservice

import com.inbedroom.pokedexter.core.NetworkClient
import com.inbedroom.pokedexter.core.ResponseResult
import com.inbedroom.pokedexter.data.catchpokemon.entity.CaughtPokemon
import com.inbedroom.pokedexter.data.catchservice.entity.RenamePokemonRequest
import com.inbedroom.pokedexter.data.catchservice.entity.RenamePokemonResponse
import com.inbedroom.pokedexter.data.pokemonservice.entity.Pokemon
import java.io.IOException

class CatchRepository(private val service: CatchService) {

    suspend fun catchPokemon(): ResponseResult<Boolean> {
        return try {
            val result = service.catchPokemon()
            if (result.isSuccessful && result.body() != null) {
                if (result.body()!!.isCaught) {
                    ResponseResult.Success(data = true)
                } else {
                    ResponseResult.Error(message = "Pokemon Broke Free")
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
                    ResponseResult.Error(message = "Pokemon wants to stay")
                }
            } else {
                ResponseResult.Error(message = "Failed to release pokemon")
            }
        } catch (e: IOException) {
            ResponseResult.Error(message = e.localizedMessage.orEmpty())
        }
    }

    suspend fun renamePokemon(pokemon: CaughtPokemon): ResponseResult<RenamePokemonResponse> {
        return try {
            NetworkClient
                .getResultOrFailure(
                    service.renamePokemon(
                        RenamePokemonRequest(
                            pokemon.givenName,
                            pokemon.renameCount
                        )
                    )
                )
        } catch (e: IOException) {
            ResponseResult.Error(message = e.localizedMessage.orEmpty())
        }
    }
}