package it.sarnataro.remotedatasource.model

import com.squareup.moshi.Json

data class PokemonDetail(
    @Json(name = "id") val id: Int?,
    @Json(name = "name") val name: String?,
    @Json(name = "sprites") val sprites: Sprites?,
    @Json(name = "stats") var stats: List<RemoteStats> = emptyList(),
    @Json(name = "types") val types: List<TypeResponse> = emptyList(),

    ){
   fun getTypesList() = types.mapNotNull { it.type?.name }
}

data class TypeResponse(
    @Json(name = "slot") val slot: Int?,
    @Json(name = "type") val type: Type?
)

data class Type(
    @Json(name = "name") val name: String?
)
data class Other(@Json(name = "home") val home: Home?)

data class Sprites(@Json(name = "other") val other: Other?)
data class Home(
    @Json(name = "front_default") val front_default: String?,
    @Json(name = "front_female") val front_female: String?,
    @Json(name = "front_shiny") val front_shiny: String?,
    @Json(name = "front_shiny_female") val front_shiny_female: String?
) {
    fun toList(): List<String> = listOfNotNull(front_default, front_female, front_shiny, front_shiny_female)
}

data class RemoteStat(

    @Json(name = "name") var name: String?
)

data class RemoteStats(

    @Json(name = "base_stat") var baseStat: Int?,
    @Json(name = "effort") var effort: Int?,
    @Json(name = "stat") var stat: RemoteStat?

)