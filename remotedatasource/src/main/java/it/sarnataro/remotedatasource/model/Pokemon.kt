package it.sarnataro.remotedatasource.model

import android.os.Parcelable
import com.squareup.moshi.Json

data class Pokemon(
    @Json(name = "name") val name: String,
    @Json(name = "url") val url: String
) {

    fun getImageUrl(): String {
        val index = url.split("/".toRegex()).dropLast(1).last()
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$index.png"
    }
}