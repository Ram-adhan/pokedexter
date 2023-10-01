package com.inbedroom.pokedexter.features.pokemondetail

import androidx.core.net.toUri
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
import com.inbedroom.pokedexter.data.pokemonservice.PokemonRepository
import com.inbedroom.pokedexter.data.pokemonservice.entity.ChainLink
import com.inbedroom.pokedexter.features.model.EvolutionData
import com.inbedroom.pokedexter.features.model.EvolutionType
import com.inbedroom.pokedexter.utils.adapter.pokemonlist.PokemonModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PokemonDetailViewModel(
    private val repository: PokemonRepository,
    private val pokemonDatabase: PokemonDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BaseApplication).repositoryModule
                PokemonDetailViewModel(repository, DatabaseClient.getDB())
            }
        }
    }

    private val _uiState: MutableStateFlow<PokemonDetailUiState> = MutableStateFlow(PokemonDetailUiState.Loading)
    val uiState: StateFlow<PokemonDetailUiState> = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PokemonDetailUiState.Loading
    )
    private var caughtPokemon: CaughtPokemon? = null
    private val isCaught get() = caughtPokemon != null
    private var pokemon: PokemonModel? = null

    fun getDetails(id: Int) {
        _uiState.tryEmit(PokemonDetailUiState.Loading)
        viewModelScope.launch(ioDispatcher) {
            launch {
                getPokemonEvolutionChain(id)
            }
            launch {
                getPokemonDetail(id)
            }
            launch {
                getCaughtPokemon(id)
            }
        }
    }

    fun catchOrReleasePokemon() {
        val pokemonData = pokemon ?: return
        if (isCaught) {
            viewModelScope.launch {
                val result = pokemonDatabase.caughtPokemonDao().deletePokemon(caughtPokemon!!)
                if (result > 0) {
                    _uiState.emit(PokemonDetailUiState.PokemonReleased)
                    caughtPokemon = null
                } else {
                    _uiState.emit(PokemonDetailUiState.Error("Failed to Release Pokemon"))
                }
            }
        } else {
            viewModelScope.launch(ioDispatcher) {
                val data = CaughtPokemon(
                    id = pokemonData.id,
                    pokemonName = pokemonData.name,
                    givenName = ""
                )
                pokemonDatabase.caughtPokemonDao().insert(data)
                caughtPokemon = data
                _uiState.emit(PokemonDetailUiState.SuccessCatchPokemon)
            }
        }
    }

    private suspend fun getPokemonDetail(id: Int) {
        return when (val result = repository.getPokemonDetail(id)) {
            is ResponseResult.Success -> {
                val pokemonModel = result.data.toPokemonModel()
                pokemon = PokemonModel(pokemonModel.name, pokemonModel.id, pokemonModel.artwork)
                _uiState.emit(PokemonDetailUiState.SuccessGetDetail(data = pokemonModel))
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

    private suspend fun getCaughtPokemon(id: Int) {
        val result = pokemonDatabase.caughtPokemonDao().getCaughtPokemon(id)
        if (result.isNotEmpty()) {
            caughtPokemon = result.first()
            _uiState.emit(PokemonDetailUiState.PokemonAlreadyCaught)
        } else {
            caughtPokemon = null
        }
    }
}