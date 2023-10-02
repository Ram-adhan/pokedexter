package com.inbedroom.pokedexter

import android.app.Application
import com.inbedroom.pokedexter.core.DatabaseClient
import com.inbedroom.pokedexter.utils.AppModule

class BaseApplication: Application() {
    private lateinit var _repositoryModule: AppModule
    val pokemonRepository get() = _repositoryModule.getPokemonRepository()
    val catchRepository get() = _repositoryModule.getCatchRepository()

    override fun onCreate() {
        super.onCreate()
        _repositoryModule = AppModule()
        DatabaseClient.initialize(this)
    }
}