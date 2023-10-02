package com.inbedroom.pokedexter.features.pokemonlist.pokemonstorage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.inbedroom.pokedexter.BaseApplication
import com.inbedroom.pokedexter.core.DatabaseClient
import com.inbedroom.pokedexter.core.PokemonDatabase
import com.inbedroom.pokedexter.core.ResponseResult
import com.inbedroom.pokedexter.data.pokemonservice.PokemonRepository
import com.inbedroom.pokedexter.features.pokemonlist.PokemonListUiState
import com.inbedroom.pokedexter.utils.adapter.pokemonlist.PokemonModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PokemonStorageViewModel(
    private val pokemonRepository: PokemonRepository,
    private val pokemonDatabase: PokemonDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BaseApplication).pokemonRepository
                PokemonStorageViewModel(repository, DatabaseClient.getDB())
            }
        }
    }

    private val _uiState: MutableStateFlow<PokemonListUiState> = MutableStateFlow(PokemonListUiState.Loading)
    val uiState: StateFlow<PokemonListUiState> = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PokemonListUiState.Loading
    )

    fun getCaughtPokemon(searchKeyWord: String) {
        viewModelScope.launch(ioDispatcher) {
            val result = pokemonDatabase.caughtPokemonDao().getAllCaughtPokemon().map {
                PokemonModel(
                    name = it.pokemonName,
                    id = it.id,
                    sprite = PokemonRepository.getDefaultSpriteLink(it.id),
                    nickname = it.givenName.ifBlank { it.pokemonName },
                    showEdit = true
                )
            }
            _uiState.emit(PokemonListUiState.SuccessGetPokemonList(result))
        }
    }
}