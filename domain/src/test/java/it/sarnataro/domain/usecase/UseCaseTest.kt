package it.sarnataro.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import it.sarnataro.domain.di.domainModule
import it.sarnataro.domain.model.PokemonEntity
import it.sarnataro.domain.model.Stat
import it.sarnataro.domain.repository.PokemonRepo
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should not be`
import org.junit.Rule
import org.junit.Test
import org.koin.core.logger.Level
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.mockito.BDDMockito
import org.mockito.Mockito

class UseCaseTest : KoinTest {
    val getPokemonDetailUseCase: GetPokemonDetail by inject()
    val getPokemonListUseCase: GetPokemonList by inject()


    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger(Level.DEBUG)
        modules(domainModule)
        declareMock<PokemonRepo>()
    }

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        Mockito.mock(clazz.java)
    }

    @Test
    fun getPokemonListUseCaseTest() = runBlocking {
        val entityPokemon = PokemonEntity(
            id = 1, "Bulbasaur", stats = listOf(Stat("Attack", 20)),
            images = listOf("", ""), types = listOf("")
        )
        BDDMockito.`when`(getPokemonListUseCase(BDDMockito.anyInt())).thenReturn(
            listOf(listOf(entityPokemon)).asFlow()
        )
        val result = getPokemonListUseCase(0).first()
        result `should not be` null
        result.first() `should be` entityPokemon


    }

    @Test
    fun getPokemonDetailUseCaseTest() = runBlocking {
        val entityPokemon = PokemonEntity(
            id = 1, "Bulbasaur", stats = listOf(Stat("Attack", 20)),
            images = listOf("", ""), types = listOf("")
        )
        BDDMockito.`when`(getPokemonDetailUseCase(BDDMockito.anyInt())).thenReturn(
            listOf(entityPokemon).asFlow()
        )
        val result = getPokemonDetailUseCase(0).first()
        result `should not be` null
        result `should be` entityPokemon


    }
}