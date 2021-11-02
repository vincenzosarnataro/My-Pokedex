package it.sarnataro.presentation.ui.homepage

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
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
import it.sarnataro.domain.usecase.GetPokemonList
import it.sarnataro.presentation.di.presentationModule
import it.sarnataro.presentation.ui.homepage.uimodel.UiHomeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should equal to`
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

@ExperimentalCoroutinesApi
class HomeViewModelTest : KoinTest {
    private val viewModel: HomeViewModel by inject()
    val getPokemonListUseCase: GetPokemonList by inject()
    lateinit var view: TestViewObserver



    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger(Level.DEBUG)
        declareMock<PokemonRepo>()
        modules(presentationModule+ domainModule)


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
    fun checkStateLoading() {
        viewModel.checkState(LoadState.Loading)
        view.verifySequence(
            UIState.Loading,
            UIState.Loading)
    }
    @Test
    fun checkStateFail() {
        viewModel.checkState(LoadState.Error(NullPointerException()))
        view.verifySequence(
            UIState.Loading,
            UIState.Failed())
    }
    @ExperimentalPagingApi
    @Test
    fun loadPokemonList() = runBlocking{
        val entityPokemon = PokemonEntity(
            id = 1, "Bulbasaur", stats = listOf(Stat("Attack", 20)),
            images = listOf("", ""), types = listOf("")
        )
        BDDMockito.`when`(getPokemonListUseCase(BDDMockito.anyInt())).thenReturn(
            listOf(listOf(entityPokemon)).asFlow()
        )
        viewModel.loadPokemonList()
        view.statesCount `should equal to` 2
        view.states.values.first() `should be`  UIState.Loading
        view.states.values[1] `should be instance of` UiHomeModel::class.java


    }
}