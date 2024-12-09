package com.check_weather.presentation.uiscreens.searchcitiesscreen

import android.view.Window
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.check_weather.presentation.navconstants.SpecificWeatherCityScreen
import com.check_weather.presentation.uiscreens.weatherscreen.statusBarColorLogic
import com.check_weather.presentation.weatherviewmodel.SpecificCityNameViewModel
import com.check_weather.weatherapp.ui.theme.CardColor
import kotlinx.coroutines.launch

@Composable
fun ManageCitiesScreen(
    navController: NavHostController,
    window: Window,
    cityNameViewModel: SpecificCityNameViewModel = hiltViewModel(),
) {
    statusBarColorLogic(window)
    val searchCityTitle = rememberSaveable { mutableStateOf("Search cities") }
    val isTextFieldReadOnly = rememberSaveable { mutableStateOf(true) }
    val searchInput = rememberSaveable { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth().padding(vertical = 50.dp, horizontal = 50.dp)
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        navController.navigateUp()
                    }, modifier = Modifier
                        .wrapContentSize()
                        .shadow(
                            elevation = 24.dp,
                            ambientColor = Color(color = 0xFF000000),
                            spotColor = Color(color = 0xffe5e5e5),
                            shape = CircleShape.copy(all = CornerSize(12.dp))
                        )
                        .border(
                            width = 1.5.dp, brush = Brush.linearGradient(
                                listOf(Color(color = 0xFF000000), Color(color = 0xff14213d))
                            ), shape = CircleShape.copy(all = CornerSize(12.dp))
                        ),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color(color = 0xFF000000)
                    )
                ) {
                    Icon(
                        modifier = Modifier.wrapContentSize(),
                        imageVector = Icons.Rounded.ArrowBackIosNew, contentDescription = null
                    )
                }
                Text(
                    text = searchCityTitle.value,
                    fontSize = 25.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.SansSerif
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(40.dp),
                onClick = {
                    focusRequester.requestFocus()
                    isTextFieldReadOnly.value = false
                },
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomEnd = 50.dp,
                    bottomStart = 50.dp
                ),
                containerColor = CardColor,
                contentColor = Color(color = 0xFF000000)
            ) {
                Icon(
                    modifier = Modifier.size(40.dp),
                    imageVector = Icons.Rounded.Add, contentDescription = null
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            ) {
                Snackbar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    containerColor = Color(color = 0xFF386641),
                    contentColor = Color(color = 0xffdee2e6),
                    shape = RoundedCornerShape(size = 20.dp),
                    snackbarData = it
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            item {
                OutlinedTextField(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .focusRequester(focusRequester = focusRequester),
                    value = searchInput.value,
                    onValueChange = {
                        if (it.all { char -> char.isLetter() || char.isWhitespace() }) {
                            searchInput.value = it.replaceFirstChar {
                                it.uppercaseChar()
                            }
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(color = 0xffdee2e6),
                        focusedContainerColor = Color(color = 0xffdee2e6),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        unfocusedLeadingIconColor = Color.DarkGray,
                        unfocusedPlaceholderColor = Color.DarkGray,
                        focusedPlaceholderColor = Color.DarkGray,
                        cursorColor = Color(color = 0xFF000000) ,
                        focusedTrailingIconColor = Color.White,
                        focusedTextColor = Color(color = 0xFF000000),
                        focusedLeadingIconColor = Color(color = 0xFF000000)
                    ),
                    shape = RoundedCornerShape(size = 50.dp),
                    readOnly = isTextFieldReadOnly.value,
                    maxLines = 1,
                    leadingIcon = {
                        Icon(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(start = 5.dp),
                            imageVector = Icons.Rounded.Search,
                            contentDescription = null,
                        )
                    },
                    trailingIcon = {
                        if (searchInput.value.isNotEmpty()) {
                            IconButton(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .padding(end = 5.dp),
                                onClick = {
                                    searchInput.value = ""
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = Color.DarkGray
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Clear,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    textStyle = TextStyle(
                        fontSize = 23.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.SansSerif,
                    ),
                    placeholder = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Search",
                            fontSize = 23.sp,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.ExtraBold,
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        autoCorrectEnabled = true,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                        showKeyboardOnFocus = true,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (searchInput.value.isNotEmpty()) {
                                //saving the city name in preference data store using viewModel
                                cityNameViewModel.saveSpecificCityName(cityName = searchInput.value)
                                searchInput.value=""
                                navController.navigate(SpecificWeatherCityScreen)
                            } else {
                                focusManager.clearFocus(force = true)
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Please Enter City/town Name",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        }
                    )
                )
                val backCount= rememberSaveable{ mutableIntStateOf(2) }
                BackHandler(enabled = true, onBack = {
                  if(backCount.intValue>=3){
                     focusRequester.requestFocus()
                  } else{
                      navController.navigateUp()
                  }
                })
            }

        }

    }
}