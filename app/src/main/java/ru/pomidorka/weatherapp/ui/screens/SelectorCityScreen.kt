package ru.pomidorka.weatherapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.search.City
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.search.aboutCity
import ru.pomidorka.weatherapp.data.WeatherViewModel
import ru.pomidorka.weatherapp.ui.components.SimpleLoadingIndicator
import ru.pomidorka.weatherapp.ui.components.WindowInsetsNotPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorCityScreen(
    viewModel: WeatherViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val mainScreenState by viewModel.mainScreenState.collectAsState()
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var isNavigating by rememberSaveable { mutableStateOf(false) }
    val textFieldState = rememberTextFieldState()
    var isLoadingSearchedCites by rememberSaveable { mutableStateOf(false) }
    var lastQuery by rememberSaveable { mutableStateOf("") }
    var searchedCityList: List<City> by rememberSaveable { mutableStateOf(emptyList()) }

    val animatedPadding by animateDpAsState(
        if (isExpanded) 0.dp else 25.dp,
        label = "padding"
    )

    fun searchClick() {
        val query = textFieldState.text.toString()
        if (!isLoadingSearchedCites && lastQuery != query) {
            scope.launch(Dispatchers.IO) {
                if (query.count() >= 3) {
                    isLoadingSearchedCites = true
                    lastQuery = query
                    searchedCityList = viewModel.searchCities(query)
                    isLoadingSearchedCites = false
                } else {
                    viewModel.showToast("Введите название от 3 символов")
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Выбор города") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (isExpanded) {
                                isExpanded = false
                                textFieldState.clearText()
                            } else {
                                if (!isNavigating) {
                                    isNavigating = true
                                    navController.popBackStack()
                                }
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
        },
        contentWindowInsets = WindowInsets.statusBars
    ) { paddings ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddings)
                .padding(animatedPadding, 0.dp)
        ) {
            SearchBar(
                modifier = modifier.fillMaxWidth(),
                shadowElevation = 4.dp,
                windowInsets = WindowInsetsNotPadding(),
                inputField = {
                    SearchBarDefaults.InputField(
                        state = textFieldState,
                        onSearch = {
                            searchClick()
                        },
                        expanded = isExpanded,
                        onExpandedChange = {
                            isExpanded = it
                            lastQuery = ""
                            searchedCityList = emptyList()
                        },
                        enabled = true,
                        placeholder = { Text("Введите название города") },
                        leadingIcon = {
                            IconButton(
                                enabled = isExpanded,
                                onClick = ::searchClick
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
                    )
                },
                expanded = isExpanded,
                onExpandedChange = { isExpanded = it }
            ) {
                if (isLoadingSearchedCites) {
                    Box(modifier = modifier.fillMaxSize()) {
                        SimpleLoadingIndicator(
                            modifier = modifier.align(Alignment.Center),
                            size = DpSize(120.dp, 120.dp),
                        )
                    }
                } else {
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        if (searchedCityList.isNotEmpty()) {
                            items(searchedCityList, key = { it.id }) { city ->
                                Row {
                                    ListItem(
                                        headlineContent = { Text(city.name) },
                                        supportingContent = { Text(city.aboutCity) },
                                        leadingContent = { Icon(Icons.Filled.LocationCity, contentDescription = null) },
                                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                                        modifier = Modifier.clickable {
                                            scope.launch {
                                                textFieldState.clearText()
                                                if (!viewModel.isFavoriteCityContains(city)) {
                                                    viewModel.addCityToFavorites(city)
                                                }
                                                searchedCityList = emptyList()
                                                isExpanded = false
                                            }
                                        }
                                            .fillMaxWidth()
                                            .padding(
                                                horizontal = 16.dp,
                                                vertical = 4.dp
                                            )
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.padding(vertical = 5.dp))

            LazyColumn {
                items(viewModel.favoritesCity, key = { it.id }) {
                    RowFavoriteCity(
                        modifier = Modifier.padding(vertical = 5.dp),
                        isSelected = mainScreenState.selectedCity == it,
                        city = it,
                        onSelectClick = {
                            viewModel.setFavoriteCity(it)
                        },
                        onRemoveClick = {
                            viewModel.removeFavoriteCity(it)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun RowFavoriteCity(
    modifier: Modifier = Modifier,
    city: City,
    isSelected: Boolean,
    onSelectClick: (City) -> Unit,
    onRemoveClick: (City) -> Unit,
) {
    val scope = rememberCoroutineScope()
    var isRemove by rememberSaveable { mutableStateOf(false) }

    val animatedRemove by animateDpAsState(
        if (isRemove) { 0.dp } else { 105.dp },
        label = "padding"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        label = "backgroundColor",
        animationSpec = tween(durationMillis = 300)
    )

    val foregroundColor by animateColorAsState(
        targetValue = if (isSelected) {
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
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(25.dp)
                )
                .height(animatedRemove)
                .clip(RoundedCornerShape(25.dp))
                .clickable(enabled = !isSelected) {
                    onSelectClick(city)
                }
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
                    text = city.name,
                    overflow = TextOverflow.Ellipsis,
                    color = foregroundColor,
                )
                Text(
                    text = city.aboutCity,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    color = foregroundColor,
                )
            }

            Row(horizontalArrangement = Arrangement.End) {
                IconButton(
                    modifier = modifier.width(50.dp),
                    enabled = !isSelected,
                    onClick = {
                        scope.launch {
                            isRemove = true
                            delay(500)
                            onRemoveClick(city)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(35.dp),
                        tint = foregroundColor
                    )
                }
            }
        }
    }
}