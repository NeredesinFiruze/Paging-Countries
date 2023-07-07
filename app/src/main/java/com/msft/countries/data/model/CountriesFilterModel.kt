package com.msft.countries.data.model

data class CountriesFilterModel(
    val searchQuery:String? = null,
    val sortType: SortType = SortType.ALPHABETICAL,
    val subRegions: List<SubRegion> = emptyList()
)

data class SubRegion(val name: String, var isClicked: Boolean = false)

val listOfRegion = listOf(
    SubRegion("Northern Europe"),
    SubRegion("Western Europe"),
    SubRegion("Southern Europe"),
    SubRegion("Southeast Europe"),
    SubRegion("Central Europe"),
    SubRegion("Eastern Europe")
)

enum class SortType(val res: String) {
    ALPHABETICAL("Alphabetical order"),
    POPULATION("Population")
}