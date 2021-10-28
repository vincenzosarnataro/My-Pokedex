package it.sarnataro.domain.repository

import it.sarnataro.domain.model.PokemonEntity
import kotlinx.coroutines.flow.Flow

interface PokemonRepo {
    suspend fun getPokemonList(offset: Int): Flow<List<PokemonEntity>>
    suspend fun getPokemonDetail(id: Int): Flow<PokemonEntity?>
}