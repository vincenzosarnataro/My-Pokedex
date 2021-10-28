package it.sarnataro.domain.di

import it.sarnataro.domain.usecase.GetPokemonDetail
import it.sarnataro.domain.usecase.GetPokemonList
import org.koin.dsl.module

val domainModule = module {
    single { GetPokemonList(get()) }
    single { GetPokemonDetail(get()) }
}