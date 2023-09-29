package com.inbedroom.pokedexter

import android.app.Application
import com.inbedroom.pokedexter.utils.AppModule

class BaseApplication: Application() {
    private lateinit var _repositoryModule: AppModule
    val repositoryModule get() = _repositoryModule.getPokemonRepository()

    override fun onCreate() {
        super.onCreate()
        _repositoryModule = AppModule()
    }
}