package com.inbedroom.pokedexter.utils.adapter.pokemonlist

import androidx.recyclerview.widget.DiffUtil

data class PokemonModel(
    val name: String,
    val id: Int,
    val sprite: String,
)

class ModelDiffUtil: DiffUtil.ItemCallback<PokemonModel>() {
    override fun areItemsTheSame(oldItem: PokemonModel, newItem: PokemonModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PokemonModel, newItem: PokemonModel): Boolean {
        return oldItem == newItem
    }
}