package com.inbedroom.pokedexter.features.model

data class EvolutionData(
    val name: String,
    val sprite: String,
    val evolutionType: EvolutionType?,
)

sealed class EvolutionType {
    companion object {
        const val LevelUpKey = "level-up"
    }
    data class LevelUp(val at: String, val specialTrigger: Boolean = false): EvolutionType()
    data class WithItem(val item: String): EvolutionType()
    data class Other(val detail: String): EvolutionType()
    object Trade: EvolutionType()
}
