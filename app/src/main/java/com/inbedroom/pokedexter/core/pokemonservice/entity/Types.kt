package com.inbedroom.pokedexter.core.pokemonservice.entity

import com.google.gson.annotations.SerializedName

data class Types(
    @SerializedName("slot") var slot: Int? = null,
    @SerializedName("type") var type: BasicData? = BasicData()
)
