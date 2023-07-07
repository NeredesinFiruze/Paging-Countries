package com.msft.countries.feature.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.msft.countries.feature.detail.DetailViewModel
import com.msft.countries.util.Constants.Destinations.HOME

fun NavGraphBuilder.homeGraph(
    navController: NavController,
    detailViewModel: DetailViewModel
) {
    composable(HOME){
        HomeScreen(navController, detailViewModel = detailViewModel)
    }
}