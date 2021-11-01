package it.sarnataro.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import it.sarnataro.dbdatasource.dao.PokemonDao
import it.sarnataro.dbdatasource.model.DbPokemon
import it.sarnataro.dbdatasource.model.DbStat
import it.sarnataro.dbdatasource.model.PokemonWithStats
import it.sarnataro.di.dataModule
import it.sarnataro.domain.repository.PokemonRepo
import it.sarnataro.remotedatasource.PokedexRemoteService
import it.sarnataro.remotedatasource.model.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.`should equal`
import org.amshove.kluent.`should not be`
import org.junit.Rule
import org.junit.Test
import org.koin.core.logger.Level
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.mockito.BDDMockito.*
import org.mockito.Mockito

class PokemonRepoImplTest : KoinTest {
    private val repo: PokemonRepo by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger(Level.DEBUG)
        modules(dataModule)
    }

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        Mockito.mock(clazz.java)
    }


    @Test
    fun getPokemonListWithoutCache() = runBlocking {
        val remoteService = declareMock<PokedexRemoteService>()
        val dbService = declareMock<PokemonDao>()

        `when`(remoteService.fetchPokemonList(anyInt(), anyInt())).thenReturn(
            PokemonResponse(
                results = listOf(
                    Pokemon("Bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/")
                ), count = 12, next = null, previous = null
            )
        )

        `when`(dbService.getPokemonList(anyInt())).thenReturn(emptyList())

        val pokemons = repo.getPokemonList(0).first()
        pokemons.size `should equal` 1
        verify(remoteService, times(1)).fetchPokemonList(anyInt(), anyInt())
        verify(dbService, times(1)).getPokemonList(anyInt())

        return@runBlocking


    }

    @Test
    fun getPokemonListWithCache() = runBlocking {
        val remoteService = declareMock<PokedexRemoteService>()
        val dbService = declareMock<PokemonDao>()

        `when`(remoteService.fetchPokemonList(anyInt(), anyInt())).thenReturn(
            PokemonResponse(
                results = listOf(
                    Pokemon("Bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/")
                ), count = 12, next = null, previous = null
            )
        )

        `when`(dbService.getPokemonList(anyInt())).thenReturn(
            listOf(
                DbPokemon(
                    id = 2, name = "Venusaur",
                    listOf(), listOf()
                )
            )
        )
        val pokemons = repo.getPokemonList(0).first()

        pokemons.size `should equal` 1
        pokemons.first().id `should equal` 2
        verify(remoteService, times(0)).fetchPokemonList(anyInt(), anyInt())
        verify(dbService, times(1)).getPokemonList(anyInt())

        return@runBlocking


    }

    @Test
    fun getPokemonDetailWithoutCacheDetail() = runBlocking {
        val remoteService = declareMock<PokedexRemoteService>()
        val dbService = declareMock<PokemonDao>()
        val detailPokemon = PokemonDetail(
            id = 1,
            name = "Bulbasaur",
            sprites = Sprites(Other(Home("", "", "", null))),
            stats = listOf(RemoteStats(baseStat = 20, effort = 39, RemoteStat("Attack"))),
            types = listOf(TypeResponse(1, Type("Herb")))

        )

        `when`(remoteService.fetchPokemonInfo(anyInt())).thenReturn(
            detailPokemon
        )

        `when`(dbService.getPokemonDetail(anyInt())).thenReturn(
            PokemonWithStats(
                pokemon = DbPokemon(
                    name = "Bulbasaur",
                    id = 1,
                    images = emptyList(),
                    types = emptyList()
                ), stats = emptyList()
            )
        )
        val pokemon = repo.getPokemonDetail(1).first()
        pokemon `should not be` null
        pokemon?.id `should equal` 1
        verify(remoteService, times(1)).fetchPokemonInfo(anyInt())
        verify(dbService, times(1)).getPokemonDetail(anyInt())
        return@runBlocking
    }

    @Test
    fun getPokemonDetailWithCacheDetail() = runBlocking {
        val remoteService = declareMock<PokedexRemoteService>()
        val dbService = declareMock<PokemonDao>()
        val detailPokemon = PokemonDetail(
            id = 1,
            name = "Bulbasaur",
            sprites = Sprites(Other(Home("", "", "", null))),
            stats = listOf(RemoteStats(baseStat = 20, effort = 39, RemoteStat("Attack"))),
            types = listOf(TypeResponse(1, Type("Herb")))

        )

        `when`(remoteService.fetchPokemonInfo(anyInt())).thenReturn(
            detailPokemon
        )

        `when`(dbService.getPokemonDetail(anyInt())).thenReturn(
            PokemonWithStats(
                pokemon = DbPokemon(
                    name = "Bulbasaur",
                    id = 1,
                    images = emptyList(),
                    types = emptyList()
                ), stats = listOf(DbStat(pokemonId = 1,"",30))
            )
        )
        val pokemon = repo.getPokemonDetail(1).first()
        pokemon `should not be` null
        pokemon?.id `should equal` 1
        verify(remoteService, times(0)).fetchPokemonInfo(anyInt())
        verify(dbService, times(1)).getPokemonDetail(anyInt())
        return@runBlocking
    }
}