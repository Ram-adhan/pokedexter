package com.inbedroom.pokedexter.core.pokemonservice

import androidx.core.net.toUri
import com.inbedroom.pokedexter.core.ErrorCode
import com.inbedroom.pokedexter.core.ResponseResult
import com.inbedroom.pokedexter.core.pokemonservice.PokemonService.Companion.PAGE_SIZE
import com.inbedroom.pokedexter.core.pokemonservice.entity.PaginationBase
import com.inbedroom.pokedexter.core.pokemonservice.entity.Pokemon
import com.inbedroom.pokedexter.core.pokemonservice.entity.PokemonDetail
import java.io.IOException

class PokemonRepository(private val service: PokemonService) {
    companion object {
        private const val UNKNOWN_ERROR = "Unknown Error"
    }

    suspend fun getAllPokemons(): ResponseResult<List<Pokemon>> {
        return try {
            val initData = service.getPokemonList()
            var count = 0
            if (initData.isSuccessful && initData.body() != null) {
                count = initData.body()!!.count ?: 0
            }
            if (count > 0) {
                val allData = service.getPokemonList(limit = count)
                if (allData.isSuccessful && allData.body() != null) {
                    val result = allData.body()!!.let {
                        it.results?.map { data ->
                            val id = getIdFromLink(data.url)
                            Pokemon(
                                id = id,
                                name = data.name ?: "",
                                sprites = getDefaultSpriteLink(id)
                            )
                        }?.filterNot { pokemon -> pokemon.name.isBlank() } ?: listOf()
                    }
                    if (result.isNotEmpty()) {
                        ResponseResult.Success(result)
                    } else {
                        ResponseResult.Error(message = "", code = ErrorCode.EMPTY_DATA)
                    }
                } else {
                    ResponseResult.Error(message = "", code = ErrorCode.EMPTY_DATA)
                }
            } else {
                ResponseResult.Error(message = "", code = ErrorCode.EMPTY_DATA)
            }
        } catch (e: IOException) {
            ResponseResult.Error(message = e.localizedMessage ?: UNKNOWN_ERROR)
        }
    }

    suspend fun getPokemonList(page: Int = 0): ResponseResult<PaginationBase<List<Pokemon>>> {
        val offset = page * PAGE_SIZE
        return try {
            val response = service.getPokemonList(offset = offset)
            if (response.isSuccessful && response.body() != null) {
                val result = response.body()!!.let {
                    PaginationBase(
                        count = it.count,
                        next = it.next,
                        previous = it.previous,
                        results = it.results?.map { data ->
                            val id = getIdFromLink(data.url)
                            Pokemon(
                                id = id,
                                name = data.name ?: "",
                                sprites = getDefaultSpriteLink(id)
                            )
                        }?.filterNot { pokemon -> pokemon.name.isBlank() }
                    )
                }
                ResponseResult.Success(result)
            } else {
                ResponseResult.Error(message = UNKNOWN_ERROR)
            }
        } catch (e: IOException) {
            ResponseResult.Error(message = e.localizedMessage ?: UNKNOWN_ERROR)
        }
    }

    private fun getIdFromLink(link: String?): Int {
        return link?.toUri()?.pathSegments?.lastOrNull()?.toIntOrNull() ?: 0
    }

    private fun getDefaultSpriteLink(id: Int): String = """https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"""

    suspend fun getPokemonDetail(id: Int): ResponseResult<PokemonDetail> {
        return try {
            val response = service.getPokemonDetail(id)
            if (response.isSuccessful && response.body() != null) {
                ResponseResult.Success(data = response.body()!!)
            } else {
                ResponseResult.Error(message = UNKNOWN_ERROR)
            }
        } catch (e: IOException) {
            ResponseResult.Error(message = e.localizedMessage ?: UNKNOWN_ERROR)
        }
    }
}