package com.inbedroom.pokedexter.data.catchservice

import com.inbedroom.pokedexter.core.NetworkClient
import com.inbedroom.pokedexter.data.catchservice.entity.CatchPokemonResponse
import com.inbedroom.pokedexter.data.catchservice.entity.ReleasePokemonResponse
import retrofit2.Response
import retrofit2.http.GET

interface CatchService {
    @GET("${NetworkClient.CATCH_BASE_URL}api/catch-pokemon")
    suspend fun catchPokemon(): Response<CatchPokemonResponse>

    @GET("${NetworkClient.CATCH_BASE_URL}api/release-pokemon")
    suspend fun releasePokemon(): Response<ReleasePokemonResponse>
}