package it.sarnataro.dbdatasource.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import it.sarnataro.dbdatasource.AppDatabase
import it.sarnataro.dbdatasource.model.DbPokemon
import it.sarnataro.dbdatasource.util.createPokemon
import it.sarnataro.dbdatasource.util.createStat
import kotlinx.coroutines.runBlocking

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@SmallTest
class PokemonDaoTesting {

    private lateinit var pokemonDao: PokemonDao
    private lateinit var db: AppDatabase

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        pokemonDao = db.pokemonDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writePokemonAndStatsAndReadInfo() = runBlocking {
        val pokemonId = 1
        val pokemon: DbPokemon = createPokemon(pokemonId)
        pokemonDao.insertPokemon(pokemon)
        val stats = listOf(createStat(pokemonId,"attack"), createStat(pokemonId,"hp"))
        pokemonDao.insertStats(stats)
        val pokemonDetail = pokemonDao.getPokemonDetail(pokemonId)

        assertThat(pokemonDetail?.pokemon?.id, equalTo(pokemonId))
        assertThat(pokemonDetail?.stats?.size, equalTo(2))
    }

    @Test
    @Throws(Exception::class)
    fun writeListPokemonAndReadList() = runBlocking {
        val pokemonIds = listOf(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21)
        pokemonDao.insertPokemonList(pokemonList = pokemonIds.map { createPokemon(it) })
        val pokemons: List<DbPokemon> = pokemonDao.getPokemonList(0)


        assertThat(pokemons.size, equalTo(20))

        val pokemons2 =  pokemonDao.getPokemonList(20 )

        assertThat(pokemons2.size, equalTo(1))

    }

}

