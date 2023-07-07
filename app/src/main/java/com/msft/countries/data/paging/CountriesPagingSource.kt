package com.msft.countries.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.msft.countries.data.model.CountriesFilterModel
import com.msft.countries.data.model.SortType
import com.msft.countries.data.remote.CountriesService
import com.msft.countries.data.remote.model.Country
import com.msft.countries.util.Constants.NETWORK_PAGE_SIZE
import com.msft.countries.util.Constants.STARTING_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

class CountriesPagingSource(
    private val apiService: CountriesService,
    private val countriesFilterModel: CountriesFilterModel?
) : PagingSource<Int, Country>() {

    private var cachedCountries = listOf<Country>()
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Country> {
        val pageIndexKey = params.key ?: STARTING_PAGE_INDEX

        return try {
            println("geldi")
            if (cachedCountries.isEmpty()) {
                cachedCountries = apiService.getEuropeanCountries()
                    .filter { country ->
                        if (countriesFilterModel?.searchQuery != null)
                            country.name?.common?.contains(
                                countriesFilterModel.searchQuery,
                                ignoreCase = true
                            ) == true
                        else true
                    }.filter { country ->
                        if (countriesFilterModel?.subRegions?.all { !it.isClicked } == true) {
                            if (countriesFilterModel.subRegions.isNotEmpty()) {
                                countriesFilterModel.subRegions
                                    .map { subregion -> subregion.name }
                                    .contains(country.subregion)
                            } else true
                        } else {
                            if (countriesFilterModel?.subRegions?.isNotEmpty() == true) {
                                countriesFilterModel.subRegions
                                    .filter { subregion -> subregion.isClicked }
                                    .map { subregion -> subregion.name }
                                    .contains(country.subregion)
                            } else true
                        }
                    }.sortedWith(
                        comparator = compareBy {
                            when (countriesFilterModel?.sortType) {
                                SortType.ALPHABETICAL -> it.name?.common
                                SortType.POPULATION -> it.population
                                else -> 0
                            }
                        }
                    )
            }

            val fromIndex = STARTING_PAGE_INDEX + pageIndexKey * params.loadSize
            var toIndex = STARTING_PAGE_INDEX + (pageIndexKey + 1) * params.loadSize

            if (toIndex > cachedCountries.size) toIndex = cachedCountries.size
            val countries =
                if (fromIndex >= cachedCountries.size) listOf()
                else cachedCountries.subList(fromIndex, toIndex)

            val nextKey =
                if (countries.isEmpty()) null
                else pageIndexKey + (params.loadSize / NETWORK_PAGE_SIZE)

            val prevKey = if (pageIndexKey == STARTING_PAGE_INDEX) null else pageIndexKey

            LoadResult.Page(
                data = countries,
                prevKey = prevKey,
                nextKey = nextKey
            )

        } catch (e: IOException) {
            println("error1")
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            println("error2")
            return LoadResult.Error(e)
        } catch (e: UnknownHostException) {
            println("error3")
            return LoadResult.Error(e)
        } catch (e: Exception) {
            println("error4")
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Country>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}