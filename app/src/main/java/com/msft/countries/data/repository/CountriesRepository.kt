package com.msft.countries.data.repository

import androidx.paging.PagingData
import com.msft.countries.data.model.CountriesFilterModel
import com.msft.countries.data.remote.model.Country
import kotlinx.coroutines.flow.Flow

interface CountriesRepository {

    fun getEuropeanCountries(
        countriesFilterModel: CountriesFilterModel? = null
    ): Flow<PagingData<Country>>
}