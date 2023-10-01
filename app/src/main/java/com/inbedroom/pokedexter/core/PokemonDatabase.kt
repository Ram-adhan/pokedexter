package com.inbedroom.pokedexter.core

import androidx.room.Database
import androidx.room.RoomDatabase
import com.inbedroom.pokedexter.data.catchpokemon.CaughtPokemonDao
import com.inbedroom.pokedexter.data.catchpokemon.entity.CaughtPokemon

@Database(
    entities = [CaughtPokemon::class],
    version = 1,
)
abstract class PokemonDatabase: RoomDatabase() {
    abstract fun caughtPokemonDao(): CaughtPokemonDao
}