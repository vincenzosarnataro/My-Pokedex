package it.sarnataro.mypokedex

import android.app.Application
import it.sarnataro.di.dataModule
import it.sarnataro.domain.di.domainModule
import it.sarnataro.presentation.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class PokemonApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger()
            androidContext(this@PokemonApplication)
            modules(dataModule+domainModule+presentationModule)
        }
    }
}