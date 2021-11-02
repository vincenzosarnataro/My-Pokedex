package it.sarnataro.remotedatasource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import it.sarnataro.remotedatasource.di.mockRemoteDataSourceModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.amshove.kluent.*
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.koin.core.logger.Level
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject

@ExperimentalCoroutinesApi
class PokedexRemoteServiceTest : KoinTest {

    private val mockWebServer by inject<MockWebServer>()
    private val service by inject<PokedexRemoteService>()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger(Level.DEBUG)
        modules(mockRemoteDataSourceModule)
    }

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()



    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    fun fetchPokemonList() = runBlocking {
        mockWebServer.enqueueFromFile("pokemon_list.json")
      val response =  service.fetchPokemonList(20, 0)

        response `should not be`  null
        response?.results?.size shouldEqual  20

       val  request = mockWebServer.takeRequestWithTimeout()
        request?.requestUrl `should not be` null
        request?.requestUrl?.queryParameter("limit") `should equal` "20"
        request?.requestUrl?.queryParameter("offset") `should equal` "0"
    }

    @Test
    fun fetchPokemonInfo() = runBlocking {
        mockWebServer.enqueueFromFile("pokemon_info.json")
        val response =  service.fetchPokemonInfo(1)

        response `should not be`  null
        response?.id `should equal` 1

        val  request = mockWebServer.takeRequestWithTimeout()
        request?.requestUrl `should not be` null
        request?.requestUrl?.encodedPathSegments?.last() `should equal`  "1"
    }

}