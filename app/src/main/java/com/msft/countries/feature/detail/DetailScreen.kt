package com.msft.countries.feature.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.msft.countries.R
import com.msft.countries.data.remote.model.Country
import com.msft.countries.util.Constants.Errors.NOT_FOUND_NAME
import java.util.ArrayList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    viewModel: DetailViewModel
) {
    val countryData by viewModel.countryData
    Column(Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(text = stringResource(id = R.string.details))
            },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            }
        )
        AsyncImage(
            model = countryData.flags?.pngImage,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        CountryData(countryData)
    }
}

@Composable
private fun CountryData(country: Country) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        val modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)

        InfoLine(modifier, stringResource(id = R.string.capital)) {
            Text(text = country.capital.joinToString(","))
        }
        if (country.subregion != null){
            InfoLine(modifier, stringResource(id = R.string.sub_region)) {
                Text(text = country.subregion!!)
            }
        }
        InfoLine(modifier, stringResource(id = R.string.languages)) {
            Text(text = country.languages.values.last())
        }
        if (country.population != null){
            InfoLine(modifier, stringResource(id = R.string.population)) {
                Text(text = country.population!!.formatNumber())
            }
        }
        if (country.area != null){
            InfoLine(modifier, stringResource(id = R.string.area)) {
                Text(text = "${country.area}km²")
            }
        }
        InfoLine(modifier, stringResource(id = R.string.borders)) {
            Text(text = country.borders.joinToString(", "))
        }
    }
}

@Composable
fun InfoLine(
    modifier: Modifier,
    title: String,
    titleColor: Color = Color.Blue,
    onInfo: @Composable ()-> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(end = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = titleColor,
            modifier = modifier
        )
        onInfo()
    }
}

fun Int.formatNumber(): String {
    val formatter = java.text.DecimalFormat("#,###")
    return formatter.format(this)
}