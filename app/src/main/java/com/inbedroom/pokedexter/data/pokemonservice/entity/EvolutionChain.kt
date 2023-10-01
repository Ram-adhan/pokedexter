package com.inbedroom.pokedexter.data.pokemonservice.entity

import com.google.gson.annotations.SerializedName

data class EvolutionChain(
    @SerializedName("id") val id: Int,
    @SerializedName("baby_trigger_item") val babyTriggerItem: BasicData? = null,
    @SerializedName("chain") val chain: ChainLink
)
