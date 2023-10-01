package com.inbedroom.pokedexter.data.pokemonservice.entity

import com.google.gson.annotations.SerializedName

data class EvolutionDetail(
    @SerializedName("min_affection") var minAffection: String? = null,
    @SerializedName("min_beauty") var minBeauty: String? = null,
    @SerializedName("min_happiness") var minHappiness: String? = null,
    @SerializedName("min_level") var minLevel: Int? = null,
    @SerializedName("needs_overworld_rain") var needsOverworldRain: Boolean? = null,
    @SerializedName("trade_species") var tradeSpecies: BasicData? = BasicData(),
    @SerializedName("trigger") var trigger: BasicData? = BasicData(),
    @SerializedName("turn_upside_down") var turnUpsideDown: Boolean? = null,
    @SerializedName("location") var location: BasicData? = null,
)