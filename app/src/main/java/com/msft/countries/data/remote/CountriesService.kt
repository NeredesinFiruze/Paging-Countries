package com.msft.countries.data.remote

import com.msft.countries.data.remote.model.Country
import retrofit2.http.GET

interface CountriesService {

    @GET("region/europe")
    suspend fun getEuropeanCountries(): List<Country>
}