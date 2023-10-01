package com.inbedroom.pokedexter.data.pokemonservice.entity

import com.google.gson.annotations.SerializedName

data class PokemonDetail(
    @SerializedName("abilities") val abilities: ArrayList<Abilities> = arrayListOf(),
    @SerializedName("base_experience") val baseExperience: Int? = null,
    @SerializedName("forms") val forms: ArrayList<BasicData> = arrayListOf(),
    @SerializedName("height") val height: Int? = null,
    @SerializedName("id") val id: Int? = null,
    @SerializedName("is_default") val isDefault: Boolean? = null,
    @SerializedName("location_area_encounters") val locationAreaEncounters: String? = null,
    @SerializedName("moves") val moves: ArrayList<Moves> = arrayListOf(),
    @SerializedName("name") val name: String? = null,
    @SerializedName("order") val order: Int? = null,
    @SerializedName("species") val species: BasicData? = BasicData(),
    @SerializedName("sprites") val sprites: SpriteData? = SpriteData(),
    @SerializedName("stats") val stats: ArrayList<Stats> = arrayListOf(),
    @SerializedName("types") val types: ArrayList<Types> = arrayListOf(),
    @SerializedName("weight") val weight: Int? = null,
    var artwork: String? = null,
)
