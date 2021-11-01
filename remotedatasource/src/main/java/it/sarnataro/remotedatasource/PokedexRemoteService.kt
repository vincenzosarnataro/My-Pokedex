
package it.sarnataro.remotedatasource


import it.sarnataro.remotedatasource.model.PokemonDetail
import it.sarnataro.remotedatasource.model.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokedexRemoteService {

  @GET("pokemon")
  suspend fun fetchPokemonList(
    @Query("limit") limit: Int = 20,
    @Query("offset") offset: Int = 0
  ): PokemonResponse?

  @GET("pokemon/{id}")
  suspend fun fetchPokemonInfo(@Path("id") id: Int):PokemonDetail?
}
