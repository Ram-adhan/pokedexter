package com.inbedroom.pokedexter.data.catchpokemon

import androidx.room.*
import com.inbedroom.pokedexter.data.catchpokemon.entity.CaughtPokemon

@Dao
interface CaughtPokemonDao {
    @Insert
    suspend fun insert(pokemon: CaughtPokemon)

    @Update
    suspend fun update(pokemon: CaughtPokemon)

    @Query("SELECT * FROM pokemon")
    suspend fun getAllCaughtPokemon(): List<CaughtPokemon>

    @Query("SELECT * FROM pokemon WHERE id = :id")
    suspend fun getCaughtPokemon(id: Int): List<CaughtPokemon>

    @Delete
    suspend fun deletePokemon(pokemon: CaughtPokemon): Int
}