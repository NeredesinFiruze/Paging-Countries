package com.msft.countries.feature.detail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.msft.countries.data.remote.model.Country
import com.msft.countries.data.remote.model.ParcelizeCountry
import com.msft.countries.util.Constants.COUNTRY
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val countryData = mutableStateOf(Country())

    fun enterCountryData(country: Country){
        countryData.value = country
    }
}