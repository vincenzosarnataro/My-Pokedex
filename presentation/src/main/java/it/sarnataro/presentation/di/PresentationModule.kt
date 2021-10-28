package it.sarnataro.presentation.di

import it.sarnataro.presentation.ui.homepage.HomeViewModel
import it.sarnataro.presentation.ui.pokemondetail.DetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel { HomeViewModel(get()) }
    viewModel{ DetailViewModel(get())}
}