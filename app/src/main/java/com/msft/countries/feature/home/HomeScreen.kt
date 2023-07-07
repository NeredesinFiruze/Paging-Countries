package com.msft.countries.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.msft.countries.R
import com.msft.countries.data.model.SortType
import com.msft.countries.data.remote.model.Country
import com.msft.countries.feature.detail.DetailViewModel
import com.msft.countries.ui.theme.ChoosedColor
import com.msft.countries.ui.theme.PurpleGrey80
import com.msft.countries.util.Constants.Destinations.DETAIL
import com.msft.countries.util.Constants.Errors.NOT_FOUND_NAME
import com.msft.countries.util.Constants.Errors.NOT_FOUND_REGION
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
    detailViewModel: DetailViewModel
) {
    val countryList = viewModel.countryList.value.collectAsLazyPagingItems()
    val bottomState = rememberModalBottomSheetState()

    if (bottomState.isVisible){
        BottomSheet(bottomState, viewModel)
    }

    Column(Modifier.fillMaxSize()) {
        Search(viewModel, bottomState)
        LazyColumn {
            items(countryList) {
                it?.let {
                    CountryItem(it){
                        detailViewModel.enterCountryData(it)
                        navController.navigate(DETAIL)
                    }
                }
            }
        }
    }
}

@Composable
fun CountryItem(
    country: Country,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 16.dp, end = 16.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(text = country.name?.common ?: NOT_FOUND_NAME, fontSize = 20.sp)
            Text(text = country.subregion ?: NOT_FOUND_REGION, fontSize = 16.sp, color = Color.Gray)
            Text(text = country.capital.first(), fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(viewModel: HomeViewModel, bottomState: SheetState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val scope = rememberCoroutineScope()

        OutlinedTextField(
            value = viewModel.searchQuery.value,
            onValueChange = viewModel::search,
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            },
            trailingIcon = {
                if (viewModel.searchQuery.value.isNotEmpty()) {
                    IconButton(onClick = { viewModel.cleanSearch() }) {
                        Icon(imageVector = Icons.Default.Cancel, contentDescription = null)
                    }
                }
            },
            placeholder = { Text(text = stringResource(id = R.string.search)) },
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp, end = 8.dp, top = 8.dp),
            shape = CircleShape
        )
        IconButton(
            onClick = { scope.launch { bottomState.show() } },
            modifier = Modifier
                .border(1.dp, Color.Black, CircleShape)
                .size(38.dp)
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)
@Composable
fun BottomSheet(
    bottomState: SheetState,
    viewModel: HomeViewModel
) {
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = {
            scope.launch {
                bottomState.hide()
            }
        },
        sheetState = bottomState,
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        windowInsets = WindowInsets(bottom = 40.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.sort_and_filter),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                fontSize = 22.sp,
                textAlign = TextAlign.Center
            )
            Text(text = stringResource(id = R.string.sort_by))
            FlowRow(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                SortType.values().asList().forEach {
                    ChooseButton(
                        text = it.res,
                        isChoosed = viewModel.sortType.value == it
                    ) {
                        viewModel.sort(it)
                    }
                }
            }
            Divider(Modifier.padding(8.dp))

            Text(stringResource(id = R.string.sub_regions))
            FlowRow(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                val choosedRegions by viewModel.subRegionList.collectAsState()

                choosedRegions.forEachIndexed { index, subRegion ->
                    ChooseButton(
                        text = subRegion.name,
                        isChoosed = choosedRegions[index].isClicked
                    ) {
                        viewModel.filterSubRegions(subRegion)
                    }
                }
            }
            Divider(Modifier.padding(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Rest All")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = { viewModel.getT() }) {
                    Text(text = "Apply")
                }
            }
        }
    }
}

@Composable
fun ChooseButton(
    text: String,
    shape: RoundedCornerShape = RoundedCornerShape(4.dp),
    isChoosed: Boolean,
    onCLick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(4.dp)
            .clip(shape)
            .border(1.dp, if (isChoosed) Color.Black else Color.LightGray, shape)
            .background(if (isChoosed) ChoosedColor else PurpleGrey80)
            .clickable { onCLick() },
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isChoosed) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        }
        Text(text, modifier = Modifier.padding(4.dp))
    }
}
