package com.msft.countries.feature.detail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.msft.countries.data.remote.model.ParcelizeCountry
import com.msft.countries.util.Constants.COUNTRY
import com.msft.countries.util.Constants.Destinations.DETAIL

fun NavGraphBuilder.detailGraph(navController: NavController, detailViewModel: DetailViewModel) {
    composable(DETAIL) {
        DetailScreen(navController, detailViewModel)
    }
}