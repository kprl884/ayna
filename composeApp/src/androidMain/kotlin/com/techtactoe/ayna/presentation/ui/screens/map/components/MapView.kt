package com.techtactoe.ayna.presentation.ui.screens.map.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.techtactoe.ayna.domain.model.LatLng
import com.techtactoe.ayna.domain.model.SalonMapPin
import com.google.android.gms.maps.model.LatLng as GoogleLatLng

@Composable
actual fun MapView(
    pins: List<SalonMapPin>,
    center: LatLng,
    onPinClick: (String) -> Unit,
    modifier: Modifier
) {
    val context = LocalContext.current
    
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            GoogleLatLng(center.latitude, center.longitude), 
            12f
        )
    }
    
    val mapProperties = remember {
        MapProperties(
            mapType = MapType.NORMAL,
            isMyLocationEnabled = false
        )
    }
    
    val uiSettings = remember {
        MapUiSettings(
            zoomControlsEnabled = false,
            myLocationButtonEnabled = false,
            mapToolbarEnabled = false
        )
    }
    
    LaunchedEffect(center) {
        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngZoom(
                GoogleLatLng(center.latitude, center.longitude),
                12f
            )
        )
    }
    
    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        uiSettings = uiSettings
    ) {
        pins.forEach { pin ->
            Marker(
                state = MarkerState(
                    position = GoogleLatLng(
                        pin.position.latitude,
                        pin.position.longitude
                    )
                ),
                title = pin.salonId,
                onClick = { 
                    onPinClick(pin.salonId)
                    true
                }
            )
        }
    }
}