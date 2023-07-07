package com.msft.countries.data.remote.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class Country(
    var name: Name? = Name(),
    var capital: ArrayList<String> = arrayListOf(),
    var subregion: String? = null,
    var languages: Map<String, String> = mapOf(),
    var borders: ArrayList<String> = arrayListOf(),
    var area: Float? = null,
    var flags: Flags? = null,
    var population: Int? = null,
){
    fun makeParcelize(): ParcelizeCountry{
        return ParcelizeCountry(
            common = name?.common,
            official = name?.official,
            capital, subregion, languages, borders, area,
            pngImage = flags?.pngImage,
            svgImage = flags?.svgImage,
            population
        )
    }
}

data class Name(
    var common: String? = null,
    var official: String? = null,
)

data class Flags(
    @SerializedName("png")
    var pngImage: String? = null,
    @SerializedName("svg")
    var svgImage: String? = null
)

@Parcelize
data class ParcelizeCountry(
    var common: String? = null,
    var official: String? = null,
    var capital: ArrayList<String> = arrayListOf(),
    var subregion: String? = null,
    var languages: Map<String, String> = mapOf(),
    var borders: ArrayList<String> = arrayListOf(),
    var area: Float? = null,
    var pngImage: String? = null,
    var svgImage: String? = null,
    var population: Int? = null,
): Parcelable