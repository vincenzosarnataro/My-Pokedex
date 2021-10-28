package it.sarnataro.data.repository

import androidx.annotation.WorkerThread
import it.sarnataro.data.mapping.toDbPokemon
import it.sarnataro.data.mapping.toListDbStats
import it.sarnataro.data.mapping.toPokemonEntity
import it.sarnataro.dbdatasource.dao.PokemonDao
import it.sarnataro.domain.model.PokemonEntity
import it.sarnataro.domain.repository.PokemonRepo
import it.sarnataro.remotedatasource.PokedexRemoteService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PokemonRepoImpl(
    private val remoteDataSource: PokedexRemoteService,
    private val pokemonDao: PokemonDao
) : PokemonRepo {

    @WorkerThread
    override suspend fun getPokemonList(offset: Int): Flow<List<PokemonEntity>> = flow {
        val list = pokemonDao.getPokemonList(offset)
        if (list.isEmpty()) {
            val listPokemonEntity =
                remoteDataSource.fetchPokemonList(offset = offset)?.results?.mapNotNull { it.toPokemonEntity() }
                    .orEmpty()
            pokemonDao.insertPokemonList(listPokemonEntity.map { it.toDbPokemon() })
            emit(listPokemonEntity)
        } else
            emit(list.map { it.toPokemonEntity() })
    }

    override suspend fun getPokemonDetail(id: Int): Flow<PokemonEntity?> = flow {
        val pokemon = pokemonDao.getPokemonDetail(id)
        if (pokemon?.stats.isNullOrEmpty()) {

            val pokemonEntity =
                remoteDataSource.fetchPokemonInfo(id)?.toPokemonEntity()
            pokemonEntity?.let {
                pokemonDao.insertPokemon(pokemonEntity.toDbPokemon())
                pokemonDao.insertStats(pokemonEntity.toListDbStats())

            }

            emit(pokemonEntity)

        } else
            emit(pokemon?.toPokemonEntity())
    }
}