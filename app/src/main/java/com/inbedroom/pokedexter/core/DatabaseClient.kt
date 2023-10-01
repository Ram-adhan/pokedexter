package com.inbedroom.pokedexter.core

import android.content.Context
import androidx.room.Room

object DatabaseClient {
    private lateinit var pokemonDatabase: PokemonDatabase

    fun initialize(context: Context) {
        pokemonDatabase =
            Room
                .databaseBuilder(context, PokemonDatabase::class.java, "PokemonDb")
                .build()
    }

    fun getDB(): PokemonDatabase = pokemonDatabase
}