package com.inbedroom.pokedexter.utils

import com.inbedroom.pokedexter.core.NetworkClient
import com.inbedroom.pokedexter.data.catchservice.CatchRepository
import com.inbedroom.pokedexter.data.catchservice.CatchService
import com.inbedroom.pokedexter.data.pokemonservice.PokemonRepository
import com.inbedroom.pokedexter.data.pokemonservice.PokemonService

class AppModule() {
    fun getPokemonRepository(): PokemonRepository = PokemonRepository(
        NetworkClient.provideRetrofit().create(PokemonService::class.java)
    )

    fun getCatchRepository(): CatchRepository = CatchRepository(
        NetworkClient.provideRetrofit().create(CatchService::class.java)
    )
}