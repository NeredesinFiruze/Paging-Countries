package com.msft.countries.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.msft.countries.data.model.CountriesFilterModel
import com.msft.countries.data.paging.CountriesPagingSource
import com.msft.countries.data.remote.CountriesService
import com.msft.countries.data.remote.model.Country
import com.msft.countries.util.Constants.NETWORK_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CountriesRepositoryImpl @Inject constructor(
    private val apiService: CountriesService
) : CountriesRepository {

    override fun getEuropeanCountries(
        countriesFilterModel: CountriesFilterModel?
    ): Flow<PagingData<Country>> {

        val config = PagingConfig(
            pageSize = NETWORK_PAGE_SIZE,
            enablePlaceholders = false,
            initialLoadSize = 10,
            prefetchDistance = 5
        )
        return Pager(
            config = config,
            pagingSourceFactory = {
                CountriesPagingSource(apiService, countriesFilterModel)
            }
        ).flow
    }
}