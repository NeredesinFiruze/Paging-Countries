package com.msft.countries.feature.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.msft.countries.data.model.CountriesFilterModel
import com.msft.countries.data.model.SubRegion
import com.msft.countries.data.model.SortType
import com.msft.countries.data.model.listOfRegion
import com.msft.countries.data.remote.model.Country
import com.msft.countries.data.repository.CountriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CountriesRepository
) : ViewModel() {

    val searchQuery = mutableStateOf("")

    val sortType = mutableStateOf(SortType.ALPHABETICAL)

    private val _subRegionList = MutableStateFlow(listOfRegion)
    val subRegionList: StateFlow<List<SubRegion>> get() = _subRegionList

    private val _filterModel = MutableStateFlow(
        CountriesFilterModel(
            searchQuery.value,
            sortType.value,
            subRegionList.value
        )
    )

    private val _countryList = mutableStateOf<Flow<PagingData<Country>>>(emptyFlow())
    val countryList: State<Flow<PagingData<Country>>> = _countryList

    private var job: Job? = null

    init {
        getT()
    }

    fun getT() {
        viewModelScope.launch {
            _countryList.value = repository.getEuropeanCountries(_filterModel.value)
        }
    }

    fun search(query: String) {
        searchQuery.value = query
        emitFilter()

        job?.cancel()
        job = viewModelScope.launch {
            delay(200)
            getT()
        }
    }

    fun cleanSearch() {
        searchQuery.value = ""
    }

    fun filterSubRegions(subRegion: SubRegion) {
        val newList = _subRegionList.value.toMutableList()
        val index = _subRegionList.value.indexOf(subRegion)

        newList[index] = newList[index].copy(isClicked = !newList[index].isClicked)
        _subRegionList.value = newList
        emitFilter()
    }

    fun sort(sort: SortType) {
        sortType.value = sort
        emitFilter()
    }

    private fun emitFilter() {
        _filterModel.value = CountriesFilterModel(
            searchQuery.value,
            sortType.value,
            subRegionList.value
        )
    }
}