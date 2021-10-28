package it.sarnataro.di

import it.sarnataro.data.repository.PokemonRepoImpl
import it.sarnataro.dbdatasource.di.dbDataSourceModule
import it.sarnataro.domain.repository.PokemonRepo
import it.sarnataro.remotedatasource.di.remoteDataSourceModule
import org.koin.dsl.module


private val repositoryModule= module {
    single<PokemonRepo> {PokemonRepoImpl(get(),get()) }
}
val dataModule = remoteDataSourceModule + dbDataSourceModule + repositoryModule
