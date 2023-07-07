package com.msft.countries.util

object Constants {
    const val BASE_URL = "https://restcountries.com/v3.1/"
    const val NETWORK_PAGE_SIZE = 10
    const val STARTING_PAGE_INDEX = 0
    const val COUNTRY = "country"

    object Destinations{
        const val HOME = "home"
        const val DETAIL = "detail"
    }

    object Errors{
        const val NOT_FOUND_NAME = "NOT_FOUND_NAME"
        const val NOT_FOUND_REGION = "NOT_FOUND_REGION"
    }
}