package com.inbedroom.pokedexter.features.pokemondetail

import com.inbedroom.pokedexter.data.pokemonservice.entity.PokemonDetail
import com.inbedroom.pokedexter.features.model.PokemonType

data class PokemonDetailModel(
    val id: Int,
    val name: String,
    val artwork: String,
    val details: List<Details>,
)

data class Details(
    val name: String,
    val detail: DetailValue
)

sealed interface DetailValue {
    data class StringValue(val value: String): DetailValue
    data class PokemonTypeValue(val value: List<PokemonType>): DetailValue
}

fun PokemonDetail.toPokemonModel(): PokemonDetailModel {
    val pokemonTypes: List<PokemonType> = types.map { type ->
        try {
            PokemonType.values().first { it.name.equals(type.type?.name ?: "", true) }
        } catch (e: NoSuchElementException) {
            PokemonType.UNKNOWN
        }
    }
    val details: List<Details> = listOf(
        Details("dex entry", DetailValue.StringValue(id.toString().padStart(4, '0'))),
        Details("type(s)", DetailValue.PokemonTypeValue(pokemonTypes)),
        Details("abilities", DetailValue.StringValue(abilities.joinToString { it.ability?.name.orEmpty() })),
    )

    return PokemonDetailModel(
        id = id ?: 0,
        name = name.orEmpty(),
        artwork = artwork.orEmpty(),
        details = details
    )
}
