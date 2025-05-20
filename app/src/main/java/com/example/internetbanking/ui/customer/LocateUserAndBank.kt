package com.example.internetbanking.ui.customer

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.internetbanking.R
import com.example.internetbanking.ui.theme.GradientColors
import com.example.internetbanking.ui.theme.custom_light_green1
import com.example.internetbanking.ui.theme.custom_mint_green
import com.example.internetbanking.viewmodels.CustomerViewModel
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import java.net.URL
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocateUserAndBankScreen(
    customerViewModel: CustomerViewModel,
    navController: NavHostController
) {

    val context = LocalContext.current
    val branches = mutableListOf<GeoPoint>()
    stringArrayResource(R.array.branch).apply {
        forEach {
            val data = it.split(',')
            branches.add(
                GeoPoint(
                    data[0].toDouble(),
                    data[1].toDouble()
                )
            )
        }
    }
    var deviceLocation by remember { mutableStateOf<GeoPoint?>(null) }
    val mapViewState = remember { mutableStateOf<MapView>(MapView(context)) }
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
    }

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
    LaunchedEffect(hasPermission) {
        val fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                deviceLocation = GeoPoint(it.latitude, it.longitude)
            }
        }
    }
    val nearestBranch = deviceLocation?.let { location ->
        branches.minByOrNull { branch ->
            haversineDistance(location, branch)
        }
    }
    var isFinding by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.sub_background1),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Location",
                            fontWeight = FontWeight.Bold,
                            color = custom_mint_green
                        )
                    },
                    navigationIcon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate Back",
                            tint = custom_mint_green,
                            modifier = Modifier
                                .size(30.dp)
                                .clickable {
                                    navController.navigateUp()
                                }
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .background(
                            brush = GradientColors.Green_DarkToLight
                        )
                )
            },
            bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(Color.White)
                ) {

                    ElevatedButton(
                        onClick = {
                            isFinding = true
                            drawRoute(
                                mapViewState.value,
                                start = deviceLocation!!,
                                end = nearestBranch!!,
                                onFinish = {
                                    isFinding = false
                                }
                            )
                        },
                        modifier = Modifier
                            .padding(vertical = 5.dp, horizontal = 10.dp)
                            .fillMaxSize(),
                        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = custom_light_green1
                        )
                    ) {
                        if (isFinding) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        } else {

                            Text(
                                "Find Nearest Branch",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                }
            },
            modifier = Modifier.systemBarsPadding()
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(10.dp)
            ) {
                if (deviceLocation != null) {
                    Box {
                        AndroidView(
                            factory = {
                                Configuration.getInstance()
                                    .load(context, context.getSharedPreferences("osmdroid", 0))
                                mapViewState.value.apply {
                                    setMultiTouchControls(true)
                                    controller.setZoom(15.0)
                                    controller.setCenter(deviceLocation)
                                    val userMaker = Marker(this)
                                    userMaker.icon = ContextCompat.getDrawable(
                                        context,
                                        R.drawable.my_location
                                    )

                                    userMaker.position = deviceLocation
                                    userMaker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                    userMaker.title = "Your location"
                                    overlays.add(userMaker)

                                    branches.forEachIndexed { index, branch ->
                                        val marker = Marker(this)
                                        marker.position = branch
                                        marker.icon = ContextCompat.getDrawable(
                                            context,
                                            R.drawable.location_maker
                                        )
                                        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                        marker.title = "Branch ${index + 1}"
                                        overlays.add(marker)
                                    }
                                    mapViewState.value = this
                                }
                            })
                        IconButton(
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color.White
                            ),
                            modifier = Modifier
                                .align(alignment = Alignment.BottomEnd)
                                .padding(20.dp),
                            onClick = {
                                mapViewState.value.let { mapView ->
                                    mapView.controller.setZoom(18.0)
                                    mapView.controller.setCenter(deviceLocation)
                                }
                            }) {

                            Icon(Icons.Default.GpsFixed, contentDescription = null)
                        }
                    }
                }

            }
        }
    }


}


fun drawRoute(
    mapView: MapView,
    start: GeoPoint,
    end: GeoPoint,
    onFinish: () -> Unit
) {
    CoroutineScope(
        Dispatchers.IO
    ).launch {
        try {

            val url =
                "https://router.project-osrm.org/route/v1/driving/${start.longitude},${start.latitude};${end.longitude},${end.latitude}?overview=full&geometries=geojson"
            val response = URL(url).readText()
            val json = JSONObject(response)
            val coordinates = json
                .getJSONArray("routes")
                .getJSONObject(0)
                .getJSONObject("geometry")
                .getJSONArray("coordinates")

            val geoPoints = mutableListOf<GeoPoint>()
            for (i in 0 until coordinates.length()) {
                val coord = coordinates.getJSONArray(i)
                geoPoints.add(GeoPoint(coord.getDouble(1), coord.getDouble(0)))
            }
            withContext(Dispatchers.Main) {
                Polyline().apply {
                    setPoints(geoPoints)
                    mapView.overlays.add(this)
                    mapView.invalidate()

                }
                onFinish()

            }
        } catch (
            e: Exception
        ) {
            e.printStackTrace()
            onFinish()
        }

    }
}


fun haversineDistance(p1: GeoPoint, p2: GeoPoint): Double {
    val r = 6371e3 // Earth radius in meters
    val lat1 = Math.toRadians(p1.latitude)
    val lat2 = Math.toRadians(p2.latitude)
    val dLat = Math.toRadians(p2.latitude - p1.latitude)
    val dLon = Math.toRadians(p2.longitude - p1.longitude)

    val a = sin(dLat / 2).pow(2.0) +
            cos(lat1) * cos(lat2) *
            sin(dLon / 2).pow(2.0)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return r * c
}


@Preview(
    showBackground = true
)
@Composable
fun LocateUserAndBankScreenPreview() {
    val fakeViewModel: CustomerViewModel = viewModel()
    val fakeNavController: NavHostController = rememberNavController()

    LocateUserAndBankScreen(fakeViewModel, fakeNavController)
}