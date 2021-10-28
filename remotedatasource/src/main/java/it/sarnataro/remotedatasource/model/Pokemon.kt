package it.sarnataro.remotedatasource.model

import android.os.Parcelable
import com.squareup.moshi.Json

data class Pokemon(
    @Json(name = "name") val name: String?,
    @Json(name = "url") val url: String?
) {
    val id = url?.split("/".toRegex())?.dropLast(1)?.last()?.toInt()


}