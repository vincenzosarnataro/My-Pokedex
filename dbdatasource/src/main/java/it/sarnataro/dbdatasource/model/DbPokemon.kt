package it.sarnataro.dbdatasource.model

import androidx.room.*

@Entity
data class DbPokemon(
    @PrimaryKey val id: Int,
    val name: String,
    val images: List<String>,
    val types:List<String>
)


@Entity(primaryKeys = ["pokemonId","name"])
data class DbStat(
   val pokemonId: Int,
   val name: String, val value: Int
)

data class PokemonWithStats(
    @Embedded val pokemon: DbPokemon,
    @Relation(
        parentColumn = "id",
        entityColumn = "pokemonId"
    )
    val stats: List<DbStat>
)