package it.sarnataro.dbdatasource.util

import it.sarnataro.dbdatasource.model.DbPokemon
import it.sarnataro.dbdatasource.model.DbStat

fun createPokemon(id: Int): DbPokemon = DbPokemon(
    id = id,
    "Bulbasaur",
    listOf("testUrlImage1", "testUrlImage2"),
    listOf("erba", "fuoco")
)

fun createStat(id: Int,name:String): DbStat = DbStat(pokemonId = id, name = name, 40)