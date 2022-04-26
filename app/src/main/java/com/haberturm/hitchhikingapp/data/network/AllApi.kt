package com.haberturm.hitchhikingapp.data.network

object AllApi {
    const val BASE_URL_GOOGLE = "https://maps.googleapis.com/maps/api/"
    private const val JSON_TYPE = "json"

    private const val GEOCODE_PATH = "geocode/"
    const val GEOCODE_LOCATION = GEOCODE_PATH + JSON_TYPE
    const val REVERSE_GEOCODE_LOCATION = GEOCODE_PATH + JSON_TYPE

    private const val DIRECTIONS_PATH = "directions/"
    const val DIRECTIONS = DIRECTIONS_PATH + JSON_TYPE

    const val BASE_URL_COMPANION = "http://localhost:1257/"
    private const val COMPANION_PATH = "companion/find/"
    const val COMPANION_FIND = COMPANION_PATH

    const val BASE_URL_DRIVER = "http://localhost:1256/"
    private const val DRIVER_PATH = "companion/createDrive/"
    const val DRIVE_CREATE = DRIVER_PATH

    const val BASE_URL_SIGNUP = "http://localhost:1212/"
    private const val SIGNUP_PATH = "registration/"
    const val SIGNUP = SIGNUP_PATH
}
