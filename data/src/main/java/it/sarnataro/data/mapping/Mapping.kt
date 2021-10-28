package it.sarnataro.data.mapping

import it.sarnataro.dbdatasource.model.DbPokemon
import it.sarnataro.dbdatasource.model.DbStat
import it.sarnataro.dbdatasource.model.PokemonWithStats
import it.sarnataro.domain.model.PokemonEntity
import it.sarnataro.domain.model.Stat
import it.sarnataro.remotedatasource.model.Pokemon
import it.sarnataro.remotedatasource.model.PokemonDetail


fun Pokemon.toPokemonEntity(): PokemonEntity? {
    val id = this.id
    id ?: return null
    return PokemonEntity(
        id = id,
        name = name.orEmpty(),
        stats = emptyList(),
        images = emptyList(),
        types = emptyList()
    )
}

fun DbPokemon.toPokemonEntity(): PokemonEntity {

    return PokemonEntity(
        id = id,
        name = name,
        stats = emptyList(),
        images = images,
        types = types
    )
}
fun PokemonWithStats.toPokemonEntity() = pokemon.toPokemonEntity().copy(stats = stats.map { Stat(it.name, it.value) })

fun PokemonDetail.toPokemonEntity(): PokemonEntity? {
    val id = this.id
    id ?: return null

    return PokemonEntity(
        id,
        name.orEmpty(),
        stats = stats.map { Stat(name = it.stat?.name.orEmpty(), value = it.baseStat ?: 0) },
        images = sprites?.other?.home?.toList().orEmpty(),
        types = getTypesList()
    )
}
fun PokemonEntity.toListDbStats():List<DbStat> = stats.map { DbStat(name = it.name, value = it.value, pokemonId = id) }
fun PokemonEntity.toDbPokemon(): DbPokemon {
    return DbPokemon(
        id = id,
        name = name,
        images = images,
        types = types
    )
}