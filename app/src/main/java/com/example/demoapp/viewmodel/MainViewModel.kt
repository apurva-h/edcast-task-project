package com.example.demoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.demoapp.model.Resource
import com.example.demoapp.network.Repository
import kotlinx.coroutines.Dispatchers

class MainViewModel(private val respository: Repository) : ViewModel() {

    fun getAPIData() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = respository.getAPIData()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getAPIDetailsData(url: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = respository.getAPIDetailsData(url)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}