package com.inbedroom.pokedexter.core.pokemonservice.entity

import com.google.gson.annotations.SerializedName

data class PokemonDetail(
    @SerializedName("abilities") var abilities: ArrayList<Abilities> = arrayListOf(),
    @SerializedName("base_experience") var baseExperience: Int? = null,
    @SerializedName("forms") var forms: ArrayList<BasicData> = arrayListOf(),
    @SerializedName("height") var height: Int? = null,
    @SerializedName("held_items") var heldItems: ArrayList<String> = arrayListOf(),
    @SerializedName("id") var id: Int? = null,
    @SerializedName("is_default") var isDefault: Boolean? = null,
    @SerializedName("location_area_encounters") var locationAreaEncounters: String? = null,
    @SerializedName("moves") var moves: ArrayList<Moves> = arrayListOf(),
    @SerializedName("name") var name: String? = null,
    @SerializedName("order") var order: Int? = null,
    @SerializedName("past_types") var pastTypes: ArrayList<String> = arrayListOf(),
    @SerializedName("species") var species: BasicData? = BasicData(),
    @SerializedName("sprites") var sprites: SpriteData? = SpriteData(),
    @SerializedName("stats") var stats: ArrayList<Stats> = arrayListOf(),
    @SerializedName("types") var types: ArrayList<Types> = arrayListOf(),
    @SerializedName("weight") var weight: Int? = null

)
