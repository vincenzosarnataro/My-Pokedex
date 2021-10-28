package it.sarnataro.remotedatasource.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import it.sarnataro.remotedatasource.PokedexRemoteService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


val remoteDataSourceModule = module {
    single { createOkHttpClient() }
    single { createMoshi() }
    single { createWebService<PokedexRemoteService>(get(),get() ,SERVER_URL) }
}
const val SERVER_URL = "https://pokeapi.co/api/v2/"


fun createMoshi(): Moshi {
   return Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
}
fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
    val builder = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
    return builder.build()
}

inline fun <reified T> createWebService(okHttpClient: OkHttpClient,moshi: Moshi ,url: String): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
    return retrofit.create(T::class.java)
}
