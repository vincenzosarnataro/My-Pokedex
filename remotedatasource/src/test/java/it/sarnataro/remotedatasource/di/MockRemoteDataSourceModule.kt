package it.sarnataro.remotedatasource.di

import com.squareup.moshi.Moshi
import it.sarnataro.remotedatasource.PokedexRemoteService
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


val mockRemoteDataSourceModule = module {
    single { createMockWebService() }
    single { createOkHttpClient() }

    single { createMoshi() }
    single { createWebServiceMock<PokedexRemoteService>(get(),get(), get()) }
}


fun createMockWebService(): MockWebServer {
    return MockWebServer()
}

inline fun <reified T> createWebServiceMock(okHttpClient: OkHttpClient, mockWebServer: MockWebServer, moshi: Moshi): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
    return retrofit.create(T::class.java)
}

