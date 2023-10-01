package com.inbedroom.pokedexter.features.pokemondetail

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.inbedroom.pokedexter.BaseApplication
import com.inbedroom.pokedexter.core.ResponseResult
import com.inbedroom.pokedexter.data.pokemonservice.PokemonRepository
import com.inbedroom.pokedexter.data.pokemonservice.entity.ChainLink
import com.inbedroom.pokedexter.features.model.EvolutionData
import com.inbedroom.pokedexter.features.model.EvolutionType
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

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

    fun getDetails(id: Int) {
        _uiState.tryEmit(PokemonDetailUiState.Loading)
        viewModelScope.launch(ioDispatcher) {
            getPokemonDetail(id)
            withContext(Dispatchers.Default) {
                getPokemonEvolutionChain(
                    id
                )
            }
        }
    }

    private suspend fun getPokemonDetail(id: Int) {
        return when (val result = repository.getPokemonDetail(id)) {
            is ResponseResult.Success -> {
                _uiState.emit(PokemonDetailUiState.SuccessGetDetail(data = result.data.toPokemonModel()))
            }
            is ResponseResult.Error -> {
                _uiState.emit(PokemonDetailUiState.Error(message = result.message))
            }
        }
    }

    private suspend fun getPokemonEvolutionChain(id: Int) {
        when (val result = repository.getPokemonSpecies(id)) {
            is ResponseResult.Success -> {
                result.data.evolutionChain?.url?.toUri()?.lastPathSegment?.toIntOrNull()?.let {
                    when (val evolveChain = repository.getEvolutionChain(it)) {
                        is ResponseResult.Success -> {
                            val evolutionList = mutableListOf<EvolutionData>()
                            var chain: ChainLink = evolveChain.data.chain
                            var hasNext: Boolean
                            do {
                                val pokemonId = chain.species?.url?.toUri()?.lastPathSegment?.toIntOrNull()
                                val evolutionDetail = try {
                                    chain.evolvesTo.first().evolutionDetails.first()
                                } catch (e: NoSuchElementException) {
                                    null
                                }
                                val evolutionType = if (evolutionDetail != null) {
                                    val hasSpecialTrigger = evolutionDetail.minLevel == null
                                    EvolutionType.LevelUp(
                                        at = if (hasSpecialTrigger) "" else evolutionDetail.minLevel.toString(),
                                        specialTrigger = hasSpecialTrigger
                                    )
                                } else {
                                    null
                                }
                                evolutionList.add(
                                    EvolutionData(
                                        name = chain.species?.name.orEmpty(),
                                        sprite = if (pokemonId != null) PokemonRepository.getDefaultSpriteLink(pokemonId) else "",
                                        evolutionType = evolutionType
                                    )
                                )
                                hasNext = chain.evolvesTo.firstOrNull() != null
                                if (hasNext)
                                    chain = chain.evolvesTo.first()
                            } while (hasNext)

                            if (evolutionList.isEmpty() || evolutionList.count() == 1) {
                                _uiState.emit(PokemonDetailUiState.NoEvolutionChain)
                            } else {
                                _uiState.emit(
                                    PokemonDetailUiState.SuccessGetEvolutionChain(
                                        evolutionList
                                    )
                                )
                            }
                        }
                        else -> {
                            _uiState.emit(PokemonDetailUiState.Error(message = "Failed to get Evolution Chain"))
                        }
                    }
                }
            }
            else -> {}
        }
    }
}