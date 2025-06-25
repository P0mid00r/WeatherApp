package ru.pomidorka.weatherapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.pomidorka.weatherapp.data.WeatherViewModel
import ru.pomidorka.weatherapp.core.api.weatherapi.entity.current.SearchData
import ru.pomidorka.weatherapp.ui.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorCityScreen(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = viewModel(),
    navController: NavController = rememberNavController(),
) {
    val scope = rememberCoroutineScope()
    var isExpanded by remember { mutableStateOf(false) }
    val textFieldState = rememberTextFieldState()
    var searchData: List<SearchData>? by remember { mutableStateOf(null) }

    val animatedPadding by animateDpAsState(
        if (isExpanded) 0.dp else 25.dp,
        label = "padding"
    )

    Column(modifier = modifier.background(MaterialTheme.colorScheme.background)) {
        CenterAlignedTopAppBar(
            title = { Text("Выбор города") },
            navigationIcon = {
                IconButton(
                    onClick = {
                        if (isExpanded) {
                            isExpanded = false
                            textFieldState.clearText()
                        } else {
                            navController.navigate(Routes.MainScreen.route)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        )

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(animatedPadding, 0.dp)
        ) {
            SearchBar(
                modifier = modifier.fillMaxWidth(),
                inputField = {
                    SearchBarDefaults.InputField(
                        state = textFieldState,
                        onSearch = {
                            scope.launch {
                                searchData = viewModel.searchCities(textFieldState.text.toString())
                            }
                        },
                        expanded = isExpanded,
                        onExpandedChange = { isExpanded = it },
                        enabled = true,
                        placeholder = { Text("Введите название города") },
                        leadingIcon = {
                            IconButton(
                                enabled = isExpanded,
                                onClick = {
                                    scope.launch {
                                        searchData = viewModel.searchCities(textFieldState.text.toString())
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Search, "")
                            }
                        },
                        trailingIcon = {
                            if (textFieldState.text.isNotEmpty()) {
                                IconButton(onClick = { textFieldState.clearText() }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear")
                                }
                            }
                        },
//                    trailingIcon = { Icon(Icons.Default.MoreVert, "") },
//                    colors = colors.inputFieldColors,
//                    interactionSource = interactionSource,
                    )
                },
                expanded = isExpanded,
                onExpandedChange = { isExpanded = it }
            ) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    searchData?.let {
                        items(it) { item ->
                            val cityName = item.name

                            Row {
                                ListItem(
                                    headlineContent = { Text(cityName) },
                                    supportingContent = { Text("${item.country}, ${item.region}") },
                                    leadingContent = { Icon(Icons.Filled.LocationCity, contentDescription = null) },
                                    colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                                    modifier = Modifier.clickable {
                                        textFieldState.clearText()
                                        viewModel.addCityToFavorites(item)
                                        searchData = emptyList()
                                        isExpanded = false
                                    }
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.padding(0.dp, 5.dp))

            LazyColumn {
                items(viewModel.favoritesCity, key = { it.id }) {
                    RowFavoriteCityName(
                        modifier = Modifier.padding(0.dp, 5.dp),
                        searchData = it,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
private fun RowFavoriteCityName(
    modifier: Modifier = Modifier,
    searchData: SearchData,
    viewModel: WeatherViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()

    var isRemove by remember { mutableStateOf(false) }

    val animatedRemove by animateDpAsState(
        if (isRemove) { 0.dp } else { 105.dp },
        label = "padding"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (viewModel.selectedCity == searchData) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        label = "backgroundColor",
        animationSpec = tween(durationMillis = 300)
    )

    val foregroundColor by animateColorAsState(
        targetValue = if (viewModel.selectedCity == searchData) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSurface
        },
        label = "foregroundColor"
    )

    AnimatedVisibility(
        visible = !isRemove,
        exit = scaleOut()
    ) {
        Row(
            modifier = modifier
                .height(animatedRemove)
                .clip(RoundedCornerShape(25.dp))
                .clickable { viewModel.setCity(searchData) }
                .background(backgroundColor)
                .padding(25.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier
                    .weight(1f)
                    .height(IntrinsicSize.Max)
            ) {
                Text(
                    text = searchData.name,
                    overflow = TextOverflow.Ellipsis,
                    color = foregroundColor,
                )
                Text(
                    text = "${searchData.country}, ${searchData.region}",
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    color = foregroundColor,
                )
            }

            Row(horizontalArrangement = Arrangement.End) {
                IconButton(
                    modifier = modifier.width(50.dp),
                    onClick = {
                        scope.launch {
                            isRemove = true
                            delay(500)
                            viewModel.removeFavoritesCity(searchData)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "",
                        modifier = Modifier.size(35.dp),
                        tint = foregroundColor
                    )
                }
            }
        }
    }
}