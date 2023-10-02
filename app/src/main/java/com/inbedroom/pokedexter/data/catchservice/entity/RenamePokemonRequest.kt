package com.inbedroom.pokedexter.data.catchservice.entity

import com.google.gson.annotations.SerializedName

data class RenamePokemonRequest(
    val name: String,
    @SerializedName("rename_count") val renameCount: Int,
)
