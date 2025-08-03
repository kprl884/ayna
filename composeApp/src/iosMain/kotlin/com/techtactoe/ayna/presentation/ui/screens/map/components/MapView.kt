package com.techtactoe.ayna.presentation.ui.screens.map.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import com.techtactoe.ayna.domain.model.LatLng
import com.techtactoe.ayna.domain.model.SalonMapPin
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.MapKit.MKAnnotationView
import platform.MapKit.MKCoordinateRegionMakeWithDistance
import platform.MapKit.MKMapView
import platform.MapKit.MKMapViewDelegateProtocol
import platform.MapKit.MKPointAnnotation
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun MapView(
    pins: List<SalonMapPin>,
    center: LatLng,
    onPinClick: (String) -> Unit,
    modifier: Modifier
) {
    val mapView = remember { MKMapView() }

    UIKitView(
        factory = {
            mapView.apply {
                // Set initial region
                val centerCoordinate = CLLocationCoordinate2DMake(
                    center.latitude,
                    center.longitude
                )
                val region = MKCoordinateRegionMakeWithDistance(
                    centerCoordinate,
                    10000.0, // 10km radius
                    10000.0
                )
                setRegion(region, animated = false)

                // Add pins
                pins.forEach { pin ->
                    val annotation = MKPointAnnotation().apply {
                        setCoordinate(
                            CLLocationCoordinate2DMake(
                                pin.position.latitude,
                                pin.position.longitude
                            )
                        )
                        setTitle(pin.salonId)
                    }
                    addAnnotation(annotation)
                }

                // Set delegate for pin tap handling
                delegate = MapViewDelegate { salonId ->
                    onPinClick(salonId)
                }
            }
            mapView
        },
        modifier = modifier.fillMaxSize(),
        update = { view ->
            // Update pins when they change
            view.removeAnnotations(view.annotations)
            pins.forEach { pin ->
                val annotation = MKPointAnnotation().apply {
                    setCoordinate(
                        CLLocationCoordinate2DMake(
                            pin.position.latitude,
                            pin.position.longitude
                        )
                    )
                    setTitle(pin.salonId)
                }
                view.addAnnotation(annotation)
            }
        }
    )
}

@OptIn(ExperimentalForeignApi::class)
private class MapViewDelegate(
    private val onPinClick: (String) -> Unit
) : NSObject(), MKMapViewDelegateProtocol {
    override fun mapView(mapView: MKMapView, didSelectAnnotationView: MKAnnotationView) {
        didSelectAnnotationView.annotation?.title?.let { salonId ->
            onPinClick(salonId)
        }
    }
}