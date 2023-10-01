package com.inbedroom.pokedexter.data.pokemonservice

import com.inbedroom.pokedexter.data.pokemonservice.entity.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonService {
    companion object {
        const val PAGE_SIZE = 20
    }

    @GET("pokemon/")
    suspend fun getPokemonList(@Query("offset") offset: Int? = null, @Query("limit") limit: Int = PAGE_SIZE): Response<PaginationBase<List<BasicData>>>

    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(@Path("id") id: Int): Response<PokemonDetail>

    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpecies(@Path("id") id: Int): Response<PokemonSpecies>

    @GET("evolution-chain/{id}")
    suspend fun getEvolutionChain(@Path("id") resourceId: Int): Response<EvolutionChain>
}