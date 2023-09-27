package com.inbedroom.pokedexter.core.pokemonservice.entity

import com.google.gson.annotations.SerializedName

data class VersionGroupDetails(
    @SerializedName("level_learned_at") var levelLearnedAt: Int? = null,
    @SerializedName("move_learn_method") var moveLearnMethod: BasicData? = BasicData(),
    @SerializedName("version_group") var versionGroup: BasicData? = BasicData()
)
