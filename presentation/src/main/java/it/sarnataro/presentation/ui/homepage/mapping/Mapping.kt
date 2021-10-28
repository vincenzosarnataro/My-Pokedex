package it.sarnataro.presentation.ui.homepage.mapping

import it.sarnataro.domain.model.PokemonEntity
import it.sarnataro.presentation.ui.homepage.uimodel.UiPokemon

fun PokemonEntity.toUiPokemon() = UiPokemon(
    id = id,
    name = name.replaceFirstChar { it.uppercaseChar() },
    urlImage = getImageUrl(),
    stats = stats.map { UiPokemon.UiStat(it.name, it.value) },
    images = images,
    types = types,
)

fun PokemonEntity.getImageUrl(): String {
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
}