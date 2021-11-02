package it.sarnataro.presentation.ui.homepage.mapping

import it.sarnataro.domain.model.PokemonEntity
import it.sarnataro.domain.model.Stat
import it.sarnataro.presentation.ui.homepage.uimodel.UiPokemon
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should not be`
import org.amshove.kluent.shouldEqual
import org.junit.Assert.*

import org.junit.Test

class MappingKtTest {

    @Test
    fun pokemonEntityToUiPokemon() {
        val entityPokemon = PokemonEntity(
            id = 1, "Bulbasaur", stats = listOf(Stat("Attack", 20)),
            images = listOf("", ""), types = listOf("")
        )
        val uiPokemon = entityPokemon.toUiPokemon()
        uiPokemon `should not be` null
        uiPokemon.id shouldEqual 1
        uiPokemon.stats shouldEqual listOf(UiPokemon.UiStat("ATTACK",20))
    }

    @Test
    fun getImageUrl() {
        val entityPokemon = PokemonEntity(
            id = 1, "Bulbasaur", stats = listOf(Stat("Attack", 20)),
            images = listOf("", ""), types = listOf("")
        )
        val urlImage = entityPokemon.getImageUrl()
        urlImage shouldEqual "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png"
    }
}