package com.inbedroom.pokedexter.features.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.lifecycleScope
import com.inbedroom.pokedexter.BaseApplication
import com.inbedroom.pokedexter.databinding.ActivitySplashBinding
import com.inbedroom.pokedexter.features.pokemonlist.ListPokemonActivity
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        lifecycleScope.launch(Dispatchers.IO) {
            (application as BaseApplication).repositoryModule.getAllPokemon()
            delay(2000)
            withContext(Dispatchers.Default) {
                startActivity(
                    Intent(
                        this@SplashActivity,
                        ListPokemonActivity::class.java
                    )
                )
                finish()
            }
        }
    }
}