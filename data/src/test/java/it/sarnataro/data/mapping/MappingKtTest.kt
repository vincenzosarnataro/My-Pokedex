package it.sarnataro.data.mapping

import it.sarnataro.dbdatasource.model.DbPokemon
import it.sarnataro.dbdatasource.model.DbStat
import it.sarnataro.dbdatasource.model.PokemonWithStats
import it.sarnataro.domain.model.PokemonEntity
import it.sarnataro.domain.model.Stat
import it.sarnataro.remotedatasource.model.*
import org.amshove.kluent.`should equal`
import org.amshove.kluent.`should not be`
import org.junit.Test

class MappingKtTest {

    @Test
    fun remotePokemonToPokemonEntity() {
        val remotePokemon = Pokemon(name = "Bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/")
        val entityPokemon = remotePokemon.toPokemonEntity()
        entityPokemon `should not be` null
        entityPokemon?.id `should equal` 1
        entityPokemon?.name `should equal` "Bulbasaur"
        entityPokemon?.images?.size `should equal` 0
        entityPokemon?.types?.size `should equal` 0
        entityPokemon?.stats?.size `should equal` 0

    }

    @Test
    fun dbPokemonToPokemonEntity() {
        val dbPokemon =
            DbPokemon(id = 1, name = "Bulbasaur", images = listOf("1", "2"), types = listOf("3"))
        val entityPokemon = dbPokemon.toPokemonEntity()
        entityPokemon `should not be` null
        entityPokemon.id `should equal` 1
        entityPokemon.name `should equal` "Bulbasaur"
        entityPokemon.images.size `should equal` 2
        entityPokemon.types.size `should equal` 1
        entityPokemon.stats.size `should equal` 0

    }

    @Test
    fun pokemonWithStatsToPokemonEntity() {
        val pokemonWithStats = PokemonWithStats(
            pokemon = DbPokemon(
                id = 1,
                name = "Bulbasaur",
                images = listOf("1", "2"),
                types = listOf("3")
            ), stats = listOf(DbStat(1, "attack", 30))
        )
        val entityPokemon = pokemonWithStats.toPokemonEntity()
        entityPokemon `should not be` null
        entityPokemon.id `should equal` 1
        entityPokemon.name `should equal` "Bulbasaur"
        entityPokemon.images.size `should equal` 2
        entityPokemon.types.size `should equal` 1
        entityPokemon.stats.size `should equal` 1
    }

    @Test
    fun pokemonDetailToPokemonEntity() {
        val detailPokemon = PokemonDetail(
            id = 1,
            name = "Bulbasaur",
            sprites = Sprites(Other(Home("", "", "", null))),
            stats = listOf(RemoteStats(baseStat = 20,effort = 39, RemoteStat("Attack"))),
            types = listOf(TypeResponse(1, Type("Herb")))

        )
        val entityPokemon = detailPokemon.toPokemonEntity()
        entityPokemon `should not be` null
        entityPokemon?.id `should equal` 1
        entityPokemon?.name `should equal` "Bulbasaur"
        entityPokemon?.images?.size `should equal` 3
        entityPokemon?.types?.size `should equal` 1
        entityPokemon?.stats?.size `should equal` 1
        entityPokemon?.types?.first() `should equal` "Herb"
        entityPokemon?.stats?.first() `should equal` Stat("Attack",20)
    }

    @Test
    fun pokemonEntityToListDbStats() {
        val entityPokemon = PokemonEntity(id = 1,"Bulbasaur",stats = listOf(Stat("Attack",20)),
            emptyList(), emptyList())
        val stats = entityPokemon.toListDbStats()
        stats.size `should equal` 1
        stats.first() `should equal` DbStat(1,"Attack",20)
    }

    @Test
    fun pokemonEntityToDbPokemon() {
        val entityPokemon = PokemonEntity(id = 1,"Bulbasaur",stats = listOf(Stat("Attack",20)),
           images= listOf("",""),types= listOf(""))
        val dbPokemon = entityPokemon.toDbPokemon()
        dbPokemon `should not be` null
        dbPokemon.id `should equal` 1
        dbPokemon.name `should equal` "Bulbasaur"
        dbPokemon.images.size `should equal` 2
        dbPokemon.types.size `should equal` 1
    }
}