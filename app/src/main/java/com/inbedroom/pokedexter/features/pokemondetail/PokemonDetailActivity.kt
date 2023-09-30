package com.inbedroom.pokedexter.features.pokemondetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.inbedroom.pokedexter.databinding.ActivityPokemonDetailBinding

class PokemonDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPokemonDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}