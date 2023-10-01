package com.inbedroom.pokedexter.data.pokemonservice.entity

import com.google.gson.annotations.SerializedName

class FlavorTextEntry(
    @SerializedName("flavor_text") var flavorText: String? = null,
    @SerializedName("language") var language: BasicData? = BasicData(),
    @SerializedName("version") var version: BasicData? = BasicData()
)