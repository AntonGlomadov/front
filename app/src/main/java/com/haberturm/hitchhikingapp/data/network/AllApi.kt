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

    //auth
    private const val BASE_KEYCLOAK_URL_AUTH = "http://185.93.111.89:32602/auth/realms/companion/protocol/openid-connect/"
    private const val TOKEN_PATH = "token/"
    const val GET_TOKEN = BASE_KEYCLOAK_URL_AUTH + TOKEN_PATH

    const val BASE_URL_AUTH = "http://185.93.111.89:30225/"

    private const val SIGNUP_PATH = "registration/"
    const val SIGNUP = BASE_URL_AUTH + SIGNUP_PATH

    private const val CHECK_PATH = "check/"
    const val CHECK = BASE_URL_AUTH + CHECK_PATH



}
