package com.inbedroom.pokedexter.data.pokemonservice.entity

data class PaginationBase<out T: Any>(
    val count: Int? = null,
    val next: String? = null,
    val previous: String? = null,
    val results: T? = null
)