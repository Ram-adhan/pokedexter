package com.inbedroom.pokedexter.features.pokemondetail

sealed interface PokemonDetailUiState {
    object Loading: PokemonDetailUiState
    data class SuccessGetDetail(val data: PokemonDetailModel): PokemonDetailUiState
    data class ErrorGetDetail(val message: String): PokemonDetailUiState
}