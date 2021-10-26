package it.sarnataro.remotedatasource.di

import it.sarnataro.remotedatasource.PokedexRemoteService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


val remoteDataSourceModule = module {
    single { createOkHttpClient() }
    single { createWebService<PokedexRemoteService>(get(), getProperty(SERVER_URL)) }
}
const val SERVER_URL = "https://pokeapi.co/api/v2/"



fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
    val builder = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
    return builder.build()
}

inline fun <reified T> createWebService(okHttpClient: OkHttpClient, url: String): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    return retrofit.create(T::class.java)
}
