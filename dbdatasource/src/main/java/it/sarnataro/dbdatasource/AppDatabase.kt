package it.sarnataro.dbdatasource

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import it.sarnataro.dbdatasource.dao.PokemonDao
import it.sarnataro.dbdatasource.model.DbPokemon
import it.sarnataro.dbdatasource.model.DbStat

@Database(entities = [DbPokemon::class,DbStat::class],version = 1,)
@TypeConverters(StringListConverter::class)

abstract class AppDatabase: RoomDatabase() {
    abstract fun  pokemonDao():PokemonDao
}

class StringListConverter  {

    @TypeConverter
    fun fromString(stringListString: String): List<String> {
        return stringListString.split("&").map { it }
    }

    @TypeConverter
    fun toString(stringList: List<String>): String {
        return stringList.joinToString(separator = "&")
    }
}