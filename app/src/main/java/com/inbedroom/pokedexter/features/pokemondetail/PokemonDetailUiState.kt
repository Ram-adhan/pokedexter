package com.inbedroom.pokedexter.features.pokemondetail

import com.inbedroom.pokedexter.features.model.EvolutionData

sealed interface PokemonDetailUiState {
    object Loading: PokemonDetailUiState
    object FinishLoading: PokemonDetailUiState
    data class SuccessGetDetail(val data: PokemonDetailModel): PokemonDetailUiState
    data class Error(val message: String): PokemonDetailUiState
    data class SuccessGetEvolutionChain(val data: List<EvolutionData>): PokemonDetailUiState
    object NoEvolutionChain: PokemonDetailUiState
    data class PokemonAlreadyCaught(val pokemonName: String): PokemonDetailUiState
    data class SuccessCatchPokemon(val pokemonName: String): PokemonDetailUiState
    object FailedCatchPokemon: PokemonDetailUiState
    object PokemonReleased: PokemonDetailUiState
    data class SuccessRenamePokemon(val pokemonName: String): PokemonDetailUiState
}