package com.haberturm.hitchhikingapp.data.network
import com.google.gson.GsonBuilder
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Retrofit {
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

    val retrofit: GeocodeApi by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(AllApi.BASE_URL_GEOCODE)
            .client(okHttpClient)
            .build().create(GeocodeApi::class.java)
    }
}
