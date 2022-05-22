package com.example.demoapp.network

import com.example.demoapp.model.CharactesDetails
import com.example.demoapp.model.StarWarData
import retrofit2.http.GET
import retrofit2.http.Url


interface ApiService {

    @GET("films/1/")
    suspend fun getCharatersAPI(): StarWarData

    @GET
    suspend fun getDetailsAPI(@Url url: String): CharactesDetails

}