package com.msft.countries.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.msft.countries.feature.detail.DetailViewModel
import com.msft.countries.feature.detail.detailGraph
import com.msft.countries.feature.home.homeGraph
import com.msft.countries.util.Constants.Destinations.HOME

@Composable
fun NavSetup(
    navController: NavHostController,
    detailViewModel: DetailViewModel = hiltViewModel()
) {
    NavHost(
        navController = navController,
        startDestination = HOME
    ) {
        homeGraph(navController, detailViewModel)
        detailGraph(navController, detailViewModel)
    }
}