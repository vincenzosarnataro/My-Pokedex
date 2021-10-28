package it.sarnataro.domain.usecase

import it.sarnataro.domain.model.PokemonEntity
import it.sarnataro.domain.repository.PokemonRepo
import kotlinx.coroutines.flow.Flow

class GetPokemonDetail(private val pokemonRepo: PokemonRepo) {
    suspend operator fun invoke(id:Int):Flow<PokemonEntity?> = pokemonRepo.getPokemonDetail(id)
}