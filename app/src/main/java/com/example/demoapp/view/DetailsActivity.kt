package com.example.demoapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.demoapp.databinding.ActivityDetailsBinding
import com.example.demoapp.model.CharactesDetails
import com.example.demoapp.model.Status
import com.example.demoapp.network.ApiHelper
import com.example.demoapp.network.RetrofitBuilder
import com.example.demoapp.viewmodel.MainViewModel
import com.example.demoapp.viewmodel.ViewModelProvider

class DetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailsBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.createToolbar)
        supportActionBar?.title = "Details"
        setupViewModel()
        setupObservers()
    }


    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelProvider(
                ApiHelper(
                    RetrofitBuilder.API_SERVICE
                )
            )
        ).get(MainViewModel::class.java)
    }

    private fun setupObservers() {
        val url = intent.extras?.getString("url")
        url?.let {
            viewModel.getAPIDetailsData(it).observe(this, Observer {
                it?.let { resource ->
                    Log.d("Details Response:", "status:" + resource.status)
                    Log.d("Details Response:", "data:" + resource.data)
                    when (resource.status) {
                        Status.SUCCESS -> {
                            binding.progressBar.visibility = View.GONE
                            resource.data?.let { model ->
                                setDetailsPage(model)
                            }
                        }
                        Status.ERROR -> {
                            binding.progressBar.visibility = View.GONE
                            binding.txtloading.text = "something went wrong please try again"
                        }
                        Status.LOADING -> {
                            binding.progressBar.visibility = View.VISIBLE

                        }
                    }
                }
            })
        }
    }


    private fun setDetailsPage(model: CharactesDetails) {
        binding.name.text = "Name is:" + model.name
        binding.massWeight.text = "Mass weight is:" + model.mass
        binding.hight.text = "Hight is:" + model.height
        binding.createdDate.text = "Created date is:" + model.created
    }

}