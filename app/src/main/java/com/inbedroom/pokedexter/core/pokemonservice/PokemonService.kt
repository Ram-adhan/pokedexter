package com.inbedroom.pokedexter.core.pokemonservice

import com.inbedroom.pokedexter.core.pokemonservice.entity.BasicData
import com.inbedroom.pokedexter.core.pokemonservice.entity.PaginationBase
import com.inbedroom.pokedexter.core.pokemonservice.entity.PokemonDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonService {
    companion object {
        const val PAGE_SIZE = 20
    }

    @GET("api/v2/pokemon/")
    suspend fun getPokemonList(@Query("offset") offset: Int? = null, @Query("limit") limit: Int = PAGE_SIZE): Response<PaginationBase<List<BasicData>>>

    @GET("api/v2/pokemon/{id}")
    suspend fun getPokemonDetail(@Path("id") id: Int): Response<PokemonDetail>
}