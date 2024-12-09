package com.check_weather.presentation.uiscreens.weatherscreen

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.POST_NOTIFICATIONS
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Looper
import android.provider.Settings
import android.view.Window
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCard
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.check_weather.presentation.allcomponents.AllComponents
import com.check_weather.presentation.navconstants.ManageCitiesScreen
import com.check_weather.presentation.weatherviewmodel.WeatherViewModel
import com.check_weather.weatherapp.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun WeatherScreen(
    navController: NavHostController,
    window: Window,
    weatherViewModel: WeatherViewModel = hiltViewModel(),
) {
    statusBarColorLogic(window)

    val currentCityTitle = rememberSaveable { mutableStateOf("Current Location") }
    val locationBarVisible = rememberSaveable { mutableStateOf(false) }
    val isInternetAvailable = rememberSaveable { mutableStateOf(false) }
    val shouldFetchWeather = rememberSaveable { mutableStateOf(false) }
    val isLocationEnabled = rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val showBottomSheet = rememberSaveable { mutableStateOf(false) }
    val showCustomDialog = rememberSaveable { mutableStateOf(false) }
    val isWeatherUIReady = rememberSaveable { mutableStateOf(false) }
    val showNotification = remember { mutableStateOf("") }
    val isPermissionGranted = rememberSaveable { mutableStateOf(false) } // New state
    val finalAlphaValue= remember{ mutableStateOf(0f) }
    WeatherScreenDataAnimation(alphaValue = {
        finalAlphaValue.value=it
    })
    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        containerColor = Color.White,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = currentCityTitle.value,
                        textAlign = TextAlign.Center,
                        fontSize = 30.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.SansSerif
                    )
                    Spacer(modifier = Modifier.height(5.5.dp))
                    if (locationBarVisible.value) {
                        Image(
                            imageVector = Icons.Rounded.LocationOn,
                            contentDescription = null,
                            alignment = Alignment.Center
                        )
                    } else {
                        CircularProgressIndicator(
                            modifier = Modifier.size(40.dp),
                            color = Color.Blue,
                            strokeCap = StrokeCap.Round,
                            strokeWidth = 5.dp
                        )
                    }
                }
                IconButton(
                    onClick = {
                        navController.navigate(ManageCitiesScreen)

                    }, modifier = Modifier
                        .wrapContentSize()
                        .padding(
                            start = 16.dp,
                            top = 16.dp, bottom = 20.dp
                        )
                        .align(Alignment.CenterVertically)
                        .shadow(
                            elevation = 24.dp,
                            ambientColor = Color(color = 0xff00b4d8),
                            spotColor = Color(color = 0xffcaf0f8),
                            shape = CircleShape.copy(all = CornerSize(12.dp))
                        )
                        .border(
                            width = 1.dp, brush = Brush.linearGradient(
                                listOf(Color(color = 0xff00b4d8), Color(color = 0xff0077b6))
                            ), shape = CircleShape.copy(all = CornerSize(12.dp))
                        ),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color(color = 0xff0077b6)
                    )
                ) {
                    Icon(
                        modifier = Modifier.wrapContentSize(),
                        imageVector = Icons.Rounded.AddCard, contentDescription = null
                    )
                }
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .graphicsLayer(alpha = finalAlphaValue.value)
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                LocationPermissionAndServiceHandler(
                    context = context, showBottomSheet = showBottomSheet,
                    isPermissionGranted, isLocationEnabled, isInternetAvailable,
                    shouldFetchWeather, showNotification, showCustomDialog
                )
            }
            item {
                if (isPermissionGranted.value) {
                    TrackLocationAndInternet(
                        isLocationEnabled = isLocationEnabled,
                        isInternetAvailable = isInternetAvailable,
                        shouldFetchWeather = shouldFetchWeather,
                        showNotification = showNotification,
                        context = context,
                        showCustomDialog = showCustomDialog,
                    )
                }
            }
            item {
                if (shouldFetchWeather.value) {
                    val fusedLocationProviderClient =
                        LocationServices.getFusedLocationProviderClient(context)
                    FetchLocation(
                        context = context,
                        fusedLocationProviderClient = fusedLocationProviderClient,
                        onLocationFetched = { locationName ->
                            if (locationName != "Location name not found" && locationName != "Error retrieving location name") {
                                currentCityTitle.value = locationName
                                weatherViewModel.fetchWeather(city = locationName)
                                locationBarVisible.value = true
                                isWeatherUIReady.value = true
                            } else {
                                Toast.makeText(
                                    context,
                                    "Unable to determine location",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        weatherViewModel
                    )
                }
            }
            item {
                if (isWeatherUIReady.value) {
                    WeatherDisplayUI(weatherViewModel = weatherViewModel)
                }
            }

        }
    }

}

fun statusBarColorLogic(window: Window) {
    window.statusBarColor = Color.White.toArgb()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        window.insetsController?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
    } else {
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars =
            true
    }
}
@Composable
fun WeatherScreenDataAnimation(alphaValue:(Float)->Unit) {
    var appear by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        appear = true
    }
    val transition = updateTransition(
        targetState = appear,
        label = "WeatherAppearAnimation"
    )
    val alpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 1000, easing = FastOutSlowInEasing) }, label = ""
    ) { appear ->
        if (appear) 2f else 0f
    }
    alphaValue.invoke(alpha)
}
@Composable
fun LocationPermissionAndServiceHandler(
    context: Context,
    showBottomSheet: MutableState<Boolean>,
    isPermissionGranted: MutableState<Boolean>,
    isLocationEnabled: MutableState<Boolean>,
    isInternetAvailable: MutableState<Boolean>,
    shouldFetchWeather: MutableState<Boolean>,
    showNotification: MutableState<String>,
    showCustomDialog: MutableState<Boolean>
) {
    val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        listOf(POST_NOTIFICATIONS, ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION)
    } else {
        listOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION)
    }

    val arePermissionsGranted = requiredPermissions.all { permission ->
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.values.all { it }) {
            showBottomSheet.value = false
            isPermissionGranted.value = true // Update state
        } else {
            showBottomSheet.value = true
        }
    }

    LaunchedEffect(Unit) {
        if (!arePermissionsGranted) {
            permissionLauncher.launch(requiredPermissions.toTypedArray())
        } else {
            isPermissionGranted.value = true // Update state if already granted
        }
    }

    if (showBottomSheet.value) {
        ModalPermissionBottomSheet(
            context = context, arePermissionsGranted,
            isLocationEnabled,
            isInternetAvailable, shouldFetchWeather, showNotification, showCustomDialog
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalPermissionBottomSheet(
    context: Context,
    arePermissionsGranted: Boolean,
    isLocationEnabled: MutableState<Boolean>,
    isInternetAvailable: MutableState<Boolean>,
    shouldFetchWeather: MutableState<Boolean>,
    showNotification: MutableState<String>,
    showCustomDialog: MutableState<Boolean>
) {
    val sheetState = rememberModalBottomSheetState()
    val isPermissionGranted = rememberSaveable { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val trackLocationAndInternetFunAgainCall = rememberSaveable { mutableStateOf(false) }
    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(), onDismissRequest = {
            scope.launch {
                sheetState.show()
            }
        }, sheetState = sheetState, containerColor = Color.White,
        contentColor = Color.Black,
        tonalElevation = 10.dp,
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = false
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                painter = painterResource(R.drawable.permission_sheet_face_icon),
                contentScale = ContentScale.Fit,
                contentDescription = "sheet_face_icon"
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                text = "All permission's required",
                fontSize = 25.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
            )
            val appendText = rememberSaveable {
                mutableStateOf(
                    "Requires location access permission to provide you\t\t" +
                            "latest weather update for your current location"
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 21.dp),
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Blue)) {
                        append("Weather\t")
                    }
                    append(
                        "${appendText.value}\tand also notification permission to notify about\t\t" +
                                "location update for your current location."
                    )
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center,
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 30.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = {
                    scope.launch {
                        sheetState.hide()
                    }
                    if (!arePermissionsGranted) {
                        Toast.makeText(
                            context,
                            "Weather Can't provide you a Weather " +
                                    "forecast & Notify you about\t" +
                                    "weather\nupdate",
                            Toast.LENGTH_SHORT
                        ).show()
                        isPermissionGranted.value = false
                    }

                }, modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ), border = BorderStroke(1.2.dp, Color.Red)
            ) {
                Text(
                    modifier = Modifier.wrapContentWidth(),
                    text = "Disagree",
                    fontSize = 15.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    letterSpacing = 1.1.sp
                )
            }
            FilledTonalButton(
                onClick = {
                    openSettings(context)
                    if (arePermissionsGranted) {
                        trackLocationAndInternetFunAgainCall.value = true
                    }
                    scope.launch {
                        sheetState.hide()
                    }
                }, modifier = Modifier
                    .padding(horizontal = 10.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color(color = 0xFF1B5E20),
                    contentColor = Color.White
                )
            ) {
                Text(
                    modifier = Modifier.wrapContentWidth(),
                    text = "Agree",
                    fontSize = 15.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    letterSpacing = 1.1.sp
                )
            }
            if (trackLocationAndInternetFunAgainCall.value) {
                TrackLocationAndInternet(
                    isLocationEnabled = isLocationEnabled,
                    isInternetAvailable = isInternetAvailable,
                    shouldFetchWeather = shouldFetchWeather,
                    showNotification = showNotification,
                    context = context,
                    showCustomDialog = showCustomDialog,
                )
            }
        }
    }

}

fun openSettings(context: Context) {
    val permissionSettings = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(permissionSettings)
}

@Composable
fun TrackLocationAndInternet(
    isLocationEnabled: MutableState<Boolean>,
    isInternetAvailable: MutableState<Boolean>,
    shouldFetchWeather: MutableState<Boolean>,
    showNotification: MutableState<String>,
    context: Context,
    showCustomDialog: MutableState<Boolean>,
) {
    val receiver = remember {
        LocationAndInternetReceiver(
            context = context,
            isLocationEnabled = isLocationEnabled,
            isInternetAvailable = isInternetAvailable,
            shouldFetchWeather = shouldFetchWeather,
            showNotification = showNotification,
            showCustomDialog = showCustomDialog
        )
    }

    DisposableEffect(Unit) {
        val filter = IntentFilter().apply {
            addAction(LocationManager.PROVIDERS_CHANGED_ACTION)
            addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        }
        context.registerReceiver(receiver, filter)
        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

    if (showNotification.value.isNotEmpty()) {
        Toast.makeText(context, showNotification.value, Toast.LENGTH_SHORT).show()
    }

    if (showCustomDialog.value) {
        LocationServiceEnableDialog(
            showDialog = showCustomDialog,
            shouldFetchWeather = shouldFetchWeather
        )
    }
}

class LocationAndInternetReceiver(
    private val context: Context,
    private val isLocationEnabled: MutableState<Boolean>,
    private val isInternetAvailable: MutableState<Boolean>,
    private val shouldFetchWeather: MutableState<Boolean>,
    private val showNotification: MutableState<String>,
    private val showCustomDialog: MutableState<Boolean>
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val locationStatus = checkLocationServiceStatus(context!!)
        val internetStatus = checkInternetAvailability(context)

        isLocationEnabled.value = locationStatus
        isInternetAvailable.value = internetStatus
        shouldFetchWeather.value = locationStatus && internetStatus

        when {
            !locationStatus && !internetStatus -> {
                showNotification.value = "Please enable Location and Internet services."
                showCustomDialog.value = true
            }

            !locationStatus -> {
                showNotification.value = "Please enable Location services."
                showCustomDialog.value = true
            }

            !internetStatus -> {
                showNotification.value = "Please enable Internet connectivity."
            }

            else -> {
                showNotification.value = ""
            }
        }
    }
}

// Function to check internet availability
fun checkInternetAvailability(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

// Function to check if location services are enabled
fun checkLocationServiceStatus(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}

@Composable
fun LocationServiceEnableDialog(
    showDialog: MutableState<Boolean>,
    shouldFetchWeather: MutableState<Boolean>,
) {
    val context = LocalContext.current
    AlertDialog(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp),
        containerColor = Color.White,
        titleContentColor = Color.Black,
        textContentColor = Color.Black,
        tonalElevation = 10.dp,
        icon = {
            Image(
                modifier = Modifier.size(40.dp),
                painter = painterResource(R.drawable.location_icon),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
        },
        onDismissRequest = { showDialog.value = true },
        dismissButton = {
            OutlinedButton(
                onClick = {
                    showDialog.value = false
                    Toast.makeText(
                        context, "Weather Unable to provide weather",
                        Toast.LENGTH_SHORT
                    ).show()
                    shouldFetchWeather.value = false
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.Black
                ),
                border = BorderStroke(width = 1.dp, color = Color.Red)
            ) {
                Text(
                    "Cancel", fontSize = 17.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        confirmButton = {
            FilledTonalButton(
                onClick = {
                    context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    showDialog.value = false
                    shouldFetchWeather.value = true
                }, colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = Color.Green,
                    contentColor = Color.White
                )
            ) {
                Text(
                    "Enable", fontSize = 17.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        ),
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Location Service Disabled",
                fontSize = 20.sp, fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center,
            )
        },
        text = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Enable location services to fetch weather updates.",
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Serif
            )
        }
    )
}

@Composable
@SuppressLint("MissingPermission") // Ensure permissions are handled before calling this function
fun FetchLocation(
    context: Context,
    fusedLocationProviderClient: FusedLocationProviderClient,
    onLocationFetched: (String) -> Unit,
    weatherViewModel: WeatherViewModel
) {
    val scope = rememberCoroutineScope()

    // Try to get the last known location
    fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
        if (location != null) {
            // If location is available, fetch the location name
            scope.launch {
                try {
                    val locationName = getLocationNameFromCoordinates(
                        context,
                        location.latitude,
                        location.longitude
                    )
                    onLocationFetched(locationName)
                    weatherViewModel.fetchAQI(
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // Request a new location if the last known location is unavailable
            requestFreshLocation(
                context = context,
                fusedLocationProviderClient = fusedLocationProviderClient,
                onLocationFetched = onLocationFetched
            )
        }
    }.addOnFailureListener {
        Toast.makeText(context, "Failed to get location", Toast.LENGTH_SHORT).show()
    }
}

@SuppressLint("MissingPermission")
private fun requestFreshLocation(
    context: Context,
    fusedLocationProviderClient: FusedLocationProviderClient,
    onLocationFetched: (String) -> Unit
) {
    val scope = CoroutineScope(Dispatchers.Main)

    val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY, 2000 // Request location updates every second
    ).apply {
        setGranularity(Granularity.GRANULARITY_FINE)
        setMinUpdateIntervalMillis(1000)
        setWaitForAccurateLocation(false)
        setMaxUpdates(1) // Stop after receiving one accurate location
    }.build()

    fusedLocationProviderClient.requestLocationUpdates(
        locationRequest,
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                fusedLocationProviderClient.removeLocationUpdates(this)
                locationResult.locations.firstOrNull()?.let { freshLocation ->
                    scope.launch {
                        try {
                            val locationName = getLocationNameFromCoordinates(
                                context,
                                freshLocation.latitude,
                                freshLocation.longitude
                            )
                            onLocationFetched(locationName)
                        } catch (e: Exception) {
                            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        },
        Looper.getMainLooper()
    )
}

suspend fun getLocationNameFromCoordinates(
    context: Context,
    latitude: Double,
    longitude: Double
): String = withContext(Dispatchers.IO) {
    val geocoder = Geocoder(context, Locale.getDefault())
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        suspendCoroutine { continuation ->
            geocoder.getFromLocation(latitude, longitude, 1, object : Geocoder.GeocodeListener {
                override fun onGeocode(addresses: List<android.location.Address>) {
                    if (addresses.isNotEmpty()) {
                        continuation.resume(addresses[0].locality ?: "Location name not found")
                    } else {
                        continuation.resume("Location name not found")
                    }
                }

                override fun onError(errorMessage: String?) {
                    continuation.resume("Error retrieving location name")
                }
            })
        }
    } else {
        try {
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses?.isNotEmpty() == true) {
                addresses[0].locality ?: "Location name not found"
            } else {
                "Location name not found"
            }
        } catch (e: Exception) {
            "Error retrieving location name"
        }
    }
}


@Composable
fun WeatherDisplayUI(weatherViewModel: WeatherViewModel) {
    val context = LocalContext.current
    val weatherResult by weatherViewModel.weatherData.collectAsStateWithLifecycle()
    val aqiResult by weatherViewModel.aqiData.collectAsStateWithLifecycle()

    weatherResult?.let { result ->
        when {
            result.isSuccess -> {
                val weatherData = result.getOrNull()
                weatherData?.let {
                    AllComponents.TemperatureDisplayTitle(weatherData)
                    //doing this code implementation becuase to align AQI Data after temp. display card
                    aqiResult?.let { aqiResult ->
                        val aqiData = aqiResult.getOrNull()
                        aqiData?.let {
                            AllComponents.AQIDataDisplayCard(aqiData)
                        }
                    }
                    AllComponents.SunriseSunSetDisplayCard(weatherData)
                    AllComponents.OtherWeatherData(weatherData)
                }
            }

            result.isFailure -> {
                val errorMessage = result.exceptionOrNull()?.message ?: "Unknown error occurred"
                LaunchedEffect(errorMessage) {
                    Toast.makeText(
                        context,
                        "Error: Unable to show weather data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            else -> {
            }
        }
    }
}