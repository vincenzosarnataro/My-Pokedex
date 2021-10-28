package it.sarnataro.domain.usecase

import it.sarnataro.domain.model.PokemonEntity
import it.sarnataro.domain.repository.PokemonRepo
import kotlinx.coroutines.flow.Flow

class GetPokemonList(private val pokemonRepo: PokemonRepo) {
    suspend operator fun invoke(offset:Int):Flow<List<PokemonEntity>> = pokemonRepo.getPokemonList(offset)
}