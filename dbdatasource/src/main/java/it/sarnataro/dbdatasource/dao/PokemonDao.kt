
package it.sarnataro.dbdatasource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import it.sarnataro.dbdatasource.model.DbPokemon


@Dao
interface PokemonDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPokemonList(pokemonList: List<DbPokemon>)

  @Query("SELECT * FROM DbPokemon LIMIT 20 OFFSET :offset")
  suspend fun getPokemonList(offset: Int): List<DbPokemon>


}
