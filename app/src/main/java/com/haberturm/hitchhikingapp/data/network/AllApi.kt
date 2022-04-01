package com.haberturm.hitchhikingapp.data.network

object AllApi {

    const val BASE_URL_GOOGLE = "https://maps.googleapis.com/maps/api/"
    private const val JSON_TYPE = "json"

    private const val GEOCODE_PATH = "geocode/"
    const val GEOCODE_LOCATION = GEOCODE_PATH + JSON_TYPE
    const val REVERSE_GEOCODE_LOCATION = GEOCODE_PATH + JSON_TYPE

    private const val DIRECTIONS_PATH = "directions/"
    const val DIRECTIONS = DIRECTIONS_PATH + JSON_TYPE


//    https://touhidapps.com/api/demo/jsondemoapi.php?option=3

}
