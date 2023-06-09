package com.haberturm.hitchhikingapp.ui.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.haberturm.hitchhikingapp.data.network.googleApi.pojo.geocode.GeocodeLocationResponse
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import com.haberturm.hitchhikingapp.R
import com.haberturm.hitchhikingapp.data.network.googleApi.pojo.reverseGeocode.ReverseGeocodeResponse
import com.haberturm.hitchhikingapp.data.repositories.auth.AuthRepository
import com.haberturm.hitchhikingapp.ui.screens.auth.login.PhoneErrors
import com.haberturm.hitchhikingapp.ui.screens.auth.reg.RegErrors
import com.haberturm.hitchhikingapp.ui.model.GeocodeUiModel
import com.haberturm.hitchhikingapp.ui.screens.home.HomeRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

object Util {

    const val bMarkerDark = R.drawable.b_marker_dark
    const val aMarkerDark = R.drawable.a_marker_dark
    const val bMarkerLight = R.drawable.b_marker_light
    const val aMarkerLight = R.drawable.a_marker_light

    const val startRadius: Double = 5000.0

    const val defaultZoom: Float = 16f


    fun GeocodeLocationResponse.toUiModel(): GeocodeUiModel {
        return GeocodeUiModel(
            formattedAddress = results[0].formattedAddress,
            location = LatLng(
                results[0].geometry.location.lat,
                results[0].geometry.location.lng
            ),
        )
    }


    fun ReverseGeocodeResponse.toUiModel(): GeocodeUiModel {
        return GeocodeUiModel(
            formattedAddress = results[0].formattedAddress,
            location = LatLng(
                results[0].geometry.location.lat,
                results[0].geometry.location.lng
            ),
        )
    }


    fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                Color.BLUE,
                BlendModeCompat.SRC_ATOP
            )
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    fun moveCamera(
        location: LatLng,
        cameraPositionState: CameraPositionState,
        coroutineScope: CoroutineScope,
        isAnimated: Boolean = true
    ) {
        if (isAnimated) {
            coroutineScope.launch {
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(
                        location,
                        defaultZoom
                    )
                )
            }
        } else {
            cameraPositionState.move(
                CameraUpdateFactory.newLatLngZoom(
                    location,
                    defaultZoom
                )
            )
        }
    }

    fun moveCameraBetweenBounds(
        bounds: LatLngBounds,
        cameraPositionState: CameraPositionState,
        coroutineScope: CoroutineScope
    ) {
        coroutineScope.launch {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngBounds(bounds, 300)
            )
        }
    }

    fun isInRadius(
        center: LatLng,
        point: LatLng,
        radius: Double
    ): Boolean {
        return getDistanceBetweenPoints(
            center.latitude,
            center.longitude,
            point.latitude,
            point.longitude
        ) <= radius
    }

    fun getDistanceBetweenPoints(
        startLat: Double,
        startLng: Double,
        endLat: Double,
        endLng: Double
    ): Double {
        val startPoint = Location("startPoint")
        startPoint.apply {
            latitude = startLat
            longitude = startLng
        }
        val endPoint = Location("endPoint")
        endPoint.apply {
            latitude = endLat
            longitude = endLng
        }
        return startPoint.distanceTo(endPoint).toDouble()
    }

    fun setRightBound(source: LatLng, destination: LatLng): LatLngBounds {
        return if (source.latitude > destination.latitude &&
            source.longitude > destination.longitude
        ) {
            LatLngBounds(destination, source)
        } else if (source.longitude > destination.longitude) {
            LatLngBounds(
                LatLng(source.latitude, destination.longitude),
                LatLng(destination.latitude, source.longitude)
            )
        } else if (source.latitude > destination.latitude) {
            LatLngBounds(
                LatLng(destination.latitude, source.longitude),
                LatLng(source.latitude, destination.longitude)
            );
        } else {
            LatLngBounds(source, destination);
        }
    }

    fun checkNumber(number: String): TextFieldState {
        if (number.length !in 6..13) {
            return TextFieldState.Failure(PhoneErrors.LENGTH_ERR)
        }
        if (number[0] != '+') {
            return TextFieldState.Failure(PhoneErrors.PLUS_START_ERR)
        }
        if (
            number.filter {
                it == '+'
            }.length > 1
        ) {
            return TextFieldState.Failure(PhoneErrors.WRONG_FORMAT_ERR)
        }
        return TextFieldState.Success
    }

    fun checkPassword(password: String): TextFieldState {
        if (password.length !in 8..16) {
            return TextFieldState.Failure(RegErrors.PASSWORD_ERR)
        }
        if (password.none { it.isDigit() }) {
            return TextFieldState.Failure(RegErrors.PASSWORD_ERR)
        }
        return TextFieldState.Success
    }

    fun repeatPasswordCheck(password: String, repeatPassword: String): TextFieldState {
        if (password != repeatPassword) {
            return TextFieldState.Failure(RegErrors.PASSWORDS_DONT_MATCH)
        } else {
            return TextFieldState.Success
        }
    }

    fun checkEmail(email: String): TextFieldState {
        if (email.none { it == '@' }) {
            return TextFieldState.Failure(RegErrors.WRONG_FORMAT)
        } else {
            return TextFieldState.Success
        }
    }

    fun checkDate(date: String): TextFieldState {
        if (
            date.length != 10 ||
            date[2] != '.' ||
            date[5] != '.'
                ){
            return TextFieldState.Failure(RegErrors.EMAIL_ERR)
        }else{
            return TextFieldState.Success
        }
    }

    fun checkName(name: String):TextFieldState{
        return if(name.isBlank())
            TextFieldState.Failure(RegErrors.NAME_ERR)
        else
            TextFieldState.Success
    }


    sealed class TextFieldState {
        data class Failure(val error: String) : TextFieldState()
        object Success : TextFieldState()
        object None : TextFieldState()
    }

    fun login(
        repository: AuthRepository,
        number: String,
        password: String,
        action: () -> Unit,
        coroutineScope: CoroutineScope
    ){
        repository.checkPasswordInDB(
            number = number,
            password = password
        )
            .onEach { token ->
                if (token.isSuccessful){
                    ModelPreferencesManager.put(token.body(), Constants.ACCESS_TOKEN)
                    action()
                }
            }
            .launchIn(coroutineScope)
    }


}

