package com.inbedroom.pokedexter.features.pokemonlist

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
import com.inbedroom.pokedexter.utils.adapter.pokemonlist.PokemonModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PokemonListViewModel(
    private val pokemonRepository: PokemonRepository,
    private val pokemonDatabase: PokemonDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BaseApplication).repositoryModule
                PokemonListViewModel(repository, DatabaseClient.getDB())
            }
        }
    }

    private val _uiState: MutableStateFlow<PokemonListUiState> = MutableStateFlow(PokemonListUiState.Loading)
    val uiState: StateFlow<PokemonListUiState> = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PokemonListUiState.Loading
    )

    fun getPokemonList(searchKeyWord: String = "") {
        viewModelScope.launch(ioDispatcher) {
            when (val result = pokemonRepository.getAllPokemon()) {
                is ResponseResult.Success -> {
                    val data = if (searchKeyWord.isNotEmpty()) {
                        result.data.filter { it.name.contains(searchKeyWord) }
                    } else {
                        result.data
                    }.map {
                        PokemonModel(
                            name = it.name,
                            id = it.id,
                            sprite = it.sprites
                        )
                    }

                    _uiState.emit(PokemonListUiState.SuccessGetPokemonList(data = data))
                }
                is ResponseResult.Error -> {
                    _uiState.emit(PokemonListUiState.ErrorGetPokemonList(result.code))
                }
            }
        }
    }

    fun getCaughtPokemon(searchKeyWord: String) {
        viewModelScope.launch(ioDispatcher) {
            val result = pokemonDatabase.caughtPokemonDao().getAllCaughtPokemon().map {
                PokemonModel(
                    name = it.pokemonName,
                    id = it.id,
                    sprite = PokemonRepository.getDefaultSpriteLink(it.id),
                    nickname = it.givenName.ifBlank { it.pokemonName }
                )
            }
            _uiState.emit(PokemonListUiState.SuccessGetPokemonList(result))
        }
    }
}