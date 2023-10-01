package com.inbedroom.pokedexter.data.pokemonservice.entity

import com.google.gson.annotations.SerializedName

data class PokemonSpecies(
    @SerializedName("base_happiness") var baseHappiness: Int? = null,
    @SerializedName("capture_rate") var captureRate: Int? = null,
    @SerializedName("evolution_chain") var evolutionChain: BasicData? = BasicData(),
    @SerializedName("flavor_text_entries") var flavorTextEntries: ArrayList<FlavorTextEntry> = arrayListOf(),
    @SerializedName("form_descriptions") var formDescriptions: ArrayList<String> = arrayListOf(),
    @SerializedName("forms_switchable") var formsSwitchable: Boolean? = null,
    @SerializedName("has_gender_differences") var hasGenderDifferences: Boolean? = null,
    @SerializedName("hatch_counter") var hatchCounter: Int? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("is_baby") var isBaby: Boolean? = null,
    @SerializedName("is_legendary") var isLegendary: Boolean? = null,
    @SerializedName("is_mythical") var isMythical: Boolean? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("order") var order: Int? = null,
)
