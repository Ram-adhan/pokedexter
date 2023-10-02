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
import com.inbedroom.pokedexter.data.catchpokemon.entity.CaughtPokemon
import com.inbedroom.pokedexter.data.catchservice.CatchRepository
import com.inbedroom.pokedexter.data.pokemonservice.PokemonRepository
import com.inbedroom.pokedexter.features.pokemondetail.PokemonDetailUiState
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
    private val catchRepository: CatchRepository,
    private val pokemonDatabase: PokemonDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BaseApplication).pokemonRepository
                val catchRepository =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BaseApplication).catchRepository
                PokemonStorageViewModel(repository, catchRepository, DatabaseClient.getDB())
            }
        }
    }

    private val _uiState: MutableStateFlow<PokemonListUiState> = MutableStateFlow(PokemonListUiState.Loading)
    val uiState: StateFlow<PokemonListUiState> = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PokemonListUiState.Loading
    )
    private var currentCaughtPokemon: List<CaughtPokemon> = listOf()

    fun getCaughtPokemon(searchKeyWord: String) {
        _uiState.tryEmit(PokemonListUiState.Loading)
        viewModelScope.launch(ioDispatcher) {
            val result = pokemonDatabase.caughtPokemonDao().getAllCaughtPokemon().also {
                currentCaughtPokemon = it
            }.map {
                PokemonModel(
                    name = it.pokemonName,
                    id = it.id,
                    sprite = PokemonRepository.getDefaultSpriteLink(it.id),
                    nickname = it.givenName.ifBlank { it.pokemonName } + it.prefix,
                    showEdit = true
                )
            }.filter {
                it.name.contains(searchKeyWord, true) || it.nickname.contains(
                    searchKeyWord,
                    true
                )
            }
            _uiState.emit(PokemonListUiState.SuccessGetPokemonList(result))
        }
    }

    fun releasePokemon(pokemonModel: PokemonModel) {
        val caughtPokemon = currentCaughtPokemon.firstOrNull { it.id == pokemonModel.id } ?: return
        viewModelScope.launch(ioDispatcher) {
            _uiState.emit(PokemonListUiState.Loading)
            when(val result = catchRepository.releasePokemon()) {
                is ResponseResult.Success -> {
                    val deleteResult = pokemonDatabase.caughtPokemonDao().deletePokemon(caughtPokemon)
                    if (deleteResult > 0) {
                        _uiState.emit(PokemonListUiState.SuccessReleasePokemon(caughtPokemon.givenName))
                    } else {
                        _uiState.emit(PokemonListUiState.Error("Failed to Release Pokemon"))
                    }
                }
                is ResponseResult.Error -> {
                    _uiState.emit(PokemonListUiState.Error(result.message))
                }
            }
        }
    }

    fun renamePokemon(pokemonModel: PokemonModel) {
        val caughtPokemon = currentCaughtPokemon
            .firstOrNull { it.id == pokemonModel.id }
            ?.let { it -> it.copy(givenName = it.givenName.ifBlank { it.pokemonName }) }
            ?: return
        viewModelScope.launch(ioDispatcher) {
            _uiState.emit(PokemonListUiState.Loading)
            when (val result = catchRepository.renamePokemon(caughtPokemon)) {
                is ResponseResult.Success -> {
                    val newData = caughtPokemon
                        .copy(prefix = result.data.suffix, renameCount = result.data.count)
                    pokemonDatabase.caughtPokemonDao().update(newData)
                    _uiState.emit(PokemonListUiState.SuccessRenamePokemon("Success change name ${caughtPokemon.givenName} to ${newData.givenName}"))
                }
                is ResponseResult.Error -> {
                    _uiState.emit(PokemonListUiState.Error(result.message))
                }
            }
        }
    }
}