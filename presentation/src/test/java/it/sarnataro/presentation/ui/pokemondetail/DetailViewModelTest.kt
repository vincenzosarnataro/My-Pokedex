package it.sarnataro.presentation.ui.pokemondetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.uniflow.android.test.TestViewObserver
import io.uniflow.android.test.createTestObserver
import io.uniflow.core.flow.data.UIState
import io.uniflow.core.logger.DebugMessageLogger
import io.uniflow.core.logger.UniFlowLogger
import io.uniflow.test.rule.UniflowTestDispatchersRule
import it.sarnataro.domain.di.domainModule
import it.sarnataro.domain.model.PokemonEntity
import it.sarnataro.domain.model.Stat
import it.sarnataro.domain.repository.PokemonRepo
import it.sarnataro.domain.usecase.GetPokemonDetail
import it.sarnataro.presentation.di.presentationModule
import it.sarnataro.presentation.ui.homepage.mapping.toUiPokemon
import it.sarnataro.presentation.ui.pokemondetail.uimodel.UiDetailModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
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
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.doThrow
import java.lang.Exception
import java.lang.IllegalStateException
import java.lang.RuntimeException


@ExperimentalCoroutinesApi
class DetailViewModelTest : KoinTest {
    private val viewModel: DetailViewModel by inject()
    val getPokemonDetailUseCase: GetPokemonDetail by inject()
    lateinit var view: TestViewObserver


    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger(Level.DEBUG)
        declareMock<PokemonRepo>()
        modules(presentationModule + domainModule)


    }

    @Before
    fun before() {
        Dispatchers.setMain(testDispatcherRule.testCoroutineDispatcher)
        view = viewModel.createTestObserver()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testDispatcherRule = UniflowTestDispatchersRule()

    init {
        UniFlowLogger.init(DebugMessageLogger())
    }


    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        Mockito.mock(clazz.java)
    }

    @Test
    fun getPokemonInfo() = runBlocking {
        val entityPokemon = PokemonEntity(
            id = 1, "Bulbasaur", stats = listOf(Stat("Attack", 20)),
            images = listOf("", ""), types = listOf("")
        )
        BDDMockito.`when`(getPokemonDetailUseCase(BDDMockito.anyInt())).thenReturn(
            listOf(entityPokemon).asFlow()
        )
        viewModel.getPokemonInfo(1)
        view.verifySequence(
            UIState.Loading,
            UIState.Loading,
            UiDetailModel(uiPokemon = entityPokemon.toUiPokemon())
        )
    }

    @Test
    fun getPokemonInfoNull() = runBlocking {

        BDDMockito.`when`(getPokemonDetailUseCase(BDDMockito.anyInt())).thenReturn(
            listOf(null).asFlow()
        )
        viewModel.getPokemonInfo(1)
        view.verifySequence(
            UIState.Loading,
            UIState.Loading,
            UIState.Failed()
        )
    }
    @Test
    fun getPokemonInfoFail() = runBlocking {
        val error = RuntimeException("PokemonFail")
        BDDMockito.`when`(getPokemonDetailUseCase(BDDMockito.anyInt())).thenThrow(error)

        viewModel.getPokemonInfo(1)
        view.verifySequence(
            UIState.Loading,
            UIState.Loading,
            UIState.Failed()

            )
    }
}