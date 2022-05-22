package com.example.demoapp.network


class Repository(private val apiHelper: ApiHelper) {

    suspend fun getAPIData() = apiHelper.getAPI()
    suspend fun getAPIDetailsData(url: String) = apiHelper.getAPIDetails(url)

}