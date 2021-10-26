package it.sarnataro.dbdatasource.di

import android.app.Application
import androidx.room.Room
import it.sarnataro.dbdatasource.AppDatabase
import it.sarnataro.dbdatasource.dao.PokemonDao
import org.koin.dsl.module

val dbDataSourceModule = module {
    single { createAppDatabase(get()) }
    single { createPokemonDao(get()) }
}

fun createAppDatabase(
    application: Application
): AppDatabase {
    return Room
        .databaseBuilder(application, AppDatabase::class.java, "MyPokedex.db")
        .fallbackToDestructiveMigration()
        .build()
}


fun createPokemonDao(appDatabase: AppDatabase): PokemonDao {
    return appDatabase.pokemonDao()
}