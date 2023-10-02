package com.inbedroom.pokedexter.data.catchpokemon.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class CaughtPokemon(
    @PrimaryKey val id: Int,
    val pokemonName: String,
    val givenName: String,
    val renameCount: Int = 0,
    val prefix: String = ""
)
