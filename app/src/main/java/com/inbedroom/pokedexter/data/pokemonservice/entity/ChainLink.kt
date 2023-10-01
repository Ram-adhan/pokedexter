package com.inbedroom.pokedexter.data.pokemonservice.entity

import com.google.gson.annotations.SerializedName

data class ChainLink(
    @SerializedName("evolution_details") var evolutionDetails: ArrayList<EvolutionDetail> = arrayListOf(),
    @SerializedName("evolves_to") var evolvesTo: ArrayList<ChainLink> = arrayListOf(),
    @SerializedName("is_baby") var isBaby: Boolean? = null,
    @SerializedName("species") var species: BasicData? = BasicData()
)
