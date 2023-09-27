package com.inbedroom.pokedexter.core.pokemonservice.entity

import com.google.gson.annotations.SerializedName

data class Stats(
    @SerializedName("base_stat") var baseStat: Int? = null,
    @SerializedName("effort") var effort: Int? = null,
    @SerializedName("stat") var stat: BasicData? = BasicData()
)
