package com.inbedroom.pokedexter.core.pokemonservice.entity

import com.google.gson.annotations.SerializedName

data class Moves(
    @SerializedName("move") var move: BasicData? = BasicData(),
    @SerializedName("version_group_details") var versionGroupDetails: ArrayList<VersionGroupDetails> = arrayListOf()
)
