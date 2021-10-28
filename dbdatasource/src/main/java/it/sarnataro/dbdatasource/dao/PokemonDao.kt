
package it.sarnataro.dbdatasource.dao

import androidx.room.*
import it.sarnataro.dbdatasource.model.DbPokemon
import it.sarnataro.dbdatasource.model.DbStat
import it.sarnataro.dbdatasource.model.PokemonWithStats


@Dao
interface PokemonDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPokemonList(pokemonList: List<DbPokemon>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPokemon(pokemon:DbPokemon)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertStats(stats:List<DbStat>)

  @Query("SELECT * FROM DbPokemon LIMIT 20 OFFSET :offset")
  suspend fun getPokemonList(offset: Int): List<DbPokemon>
  @Transaction
  @Query("SELECT * FROM DbPokemon WHERE id = :id")
  suspend fun getPokemonDetail(id: Int): PokemonWithStats?


}
