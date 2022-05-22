package com.example.demoapp.network


class ApiHelper(private val apiService: ApiService) {

    suspend fun getAPI() = apiService.getCharatersAPI()
    suspend fun getAPIDetails(url: String) = apiService.getDetailsAPI(url)
}