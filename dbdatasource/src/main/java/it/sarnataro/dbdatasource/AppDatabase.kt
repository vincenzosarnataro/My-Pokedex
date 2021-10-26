package it.sarnataro.dbdatasource

import androidx.room.Database
import androidx.room.RoomDatabase
import it.sarnataro.dbdatasource.dao.PokemonDao
import it.sarnataro.dbdatasource.model.DbPokemon

@Database(entities = [DbPokemon::class],version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun  pokemonDao():PokemonDao
}