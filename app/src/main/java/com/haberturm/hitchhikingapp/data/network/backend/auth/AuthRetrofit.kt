package com.haberturm.hitchhikingapp.data.network.backend.auth

import com.google.gson.GsonBuilder
import com.haberturm.hitchhikingapp.data.network.AllApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object AuthRetrofit {
    private const val TIME_OUT: Long = 120

    private val gson = GsonBuilder().setLenient().create()

    private val okHttpClient = OkHttpClient.Builder()
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val resp = chain.proceed(chain.request())
            // Deal with the response code
            if (resp.code == 200) {
                try {
                    val myJson = resp.peekBody(2048).string() // peekBody() will not close the response
                    println(myJson)
                } catch (e: Exception) {
                    println("Error parse json from intercept..............")
                }
            } else {
                println(resp)
            }
            resp
        }.build()

    val authRetrofit: AuthApi by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(AllApi.BASE_URL_AUTH)
            .client(okHttpClient)
            .build().create(AuthApi::class.java)
    }
}