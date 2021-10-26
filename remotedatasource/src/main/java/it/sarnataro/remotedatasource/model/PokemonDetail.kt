package it.sarnataro.remotedatasource.model

import com.squareup.moshi.Json

data class PokemonDetail(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "height") val height: Int,
    @Json(name = "weight") val weight: Int,
    @Json(name = "base_experience") val experience: Int,
    @Json(name = "types") val types: List<TypeResponse>,

    )

data class TypeResponse(
    @Json(name = "slot") val slot: Int,
    @Json(name = "type") val type: Type
)

data class Type(
    @Json(name = "name") val name: String
)