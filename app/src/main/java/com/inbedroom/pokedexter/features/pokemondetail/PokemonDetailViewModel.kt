package com.inbedroom.pokedexter.features.pokemondetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.inbedroom.pokedexter.BaseApplication
import com.inbedroom.pokedexter.core.ResponseResult
import com.inbedroom.pokedexter.data.pokemonservice.PokemonRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PokemonDetailViewModel(
    private val repository: PokemonRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BaseApplication).repositoryModule
                PokemonDetailViewModel(repository)
            }
        }
    }

    private val _uiState: MutableStateFlow<PokemonDetailUiState> = MutableStateFlow(PokemonDetailUiState.Loading)
    val uiState: StateFlow<PokemonDetailUiState> = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PokemonDetailUiState.Loading
    )

    fun getDetail(id: Int) {
        viewModelScope.launch(ioDispatcher) {
            _uiState.emit(PokemonDetailUiState.Loading)
            val result = repository.getPokemonDetail(id)
            when (result) {
                is ResponseResult.Success -> {
                    _uiState.emit(PokemonDetailUiState.SuccessGetDetail(data = result.data.toPokemonModel()))
                }
                is ResponseResult.Error -> {
                    _uiState.emit(PokemonDetailUiState.ErrorGetDetail(message = result.message))
                }
            }
        }
    }
}