package it.sarnataro.presentation.ui.homepage.uimodel

import it.sarnataro.presentation.R

data class UiPokemon(
    val id: Int,
    val name: String,
    val urlImage: String,
    val stats: List<UiStat>,
    val images: List<String>,
    val types: List<String>
) {

    data class UiStat(val name: String, val value: Int)

    fun getFormatId() = String.format("#%03d", id)

    companion object {
        fun getTypeColor(type: String): Int {
            return when (type) {
                "fighting" -> R.color.fighting
                "flying" -> R.color.flying
                "poison" -> R.color.poison
                "ground" -> R.color.ground
                "rock" -> R.color.rock
                "bug" -> R.color.bug
                "ghost" -> R.color.ghost
                "steel" -> R.color.steel
                "fire" -> R.color.fire
                "water" -> R.color.water
                "grass" -> R.color.grass
                "electric" -> R.color.electric
                "psychic" -> R.color.psychic
                "ice" -> R.color.ice
                "dragon" -> R.color.dragon
                "fairy" -> R.color.fairy
                "dark" -> R.color.dark
                else -> R.color.gray_21
            }
        }
    }
}

