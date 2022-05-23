package com.example.demoapp.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitBuilder {
    const val BASE_URL = "https://swapi.dev/api/"


    val httpClient = OkHttpClient.Builder().apply {
        callTimeout(2, TimeUnit.MINUTES)
        connectTimeout(240, TimeUnit.SECONDS)
        readTimeout(240, TimeUnit.SECONDS)
        addInterceptor(HttpLoggingInterceptor(
            HttpLoggingInterceptor.Logger { message ->
                println("LOG-APP: $message")
            }).apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
    }


    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
        //Doesn't require the adapter
    }

    //About Us  API
    val API_SERVICE: ApiService = getRetrofit().create(ApiService::class.java)

}