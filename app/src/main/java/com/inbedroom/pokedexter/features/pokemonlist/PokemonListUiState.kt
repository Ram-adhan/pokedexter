package com.inbedroom.pokedexter.features.pokemonlist

import com.inbedroom.pokedexter.utils.adapter.pokemonlist.PokemonModel

sealed interface PokemonListUiState {
    object Loading: PokemonListUiState
    data class SuccessGetPokemonList(val data: List<PokemonModel>): PokemonListUiState
    data class ErrorGetPokemonList(val code: Int): PokemonListUiState
}