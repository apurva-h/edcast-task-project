package com.example.demoapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demoapp.adapter.MainAdapter
import com.example.demoapp.databinding.ActivityMainBinding
import com.example.demoapp.model.StarWarData
import com.example.demoapp.model.Status
import com.example.demoapp.network.ApiHelper
import com.example.demoapp.network.RetrofitBuilder
import com.example.demoapp.viewmodel.MainViewModel
import com.example.demoapp.viewmodel.ViewModelProvider

class MainActivity : AppCompatActivity(), MainAdapter.ItemClickListener {

    lateinit var binding: ActivityMainBinding
    lateinit var adapter: MainAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.createToolbar)
        supportActionBar?.title = "Home"
        setupAdapter()

        binding.tryagain.setOnClickListener(View.OnClickListener {
            //retry API on failure.
            setupObservers()
        })
    }

    private fun setupAdapter() {
        binding.recyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = MainAdapter(this)
        binding.recyclerview.adapter = adapter
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
        viewModel.getAPIData().observe(this, Observer {
            it?.let { resource ->
                Log.d("Response:", "status:" + resource.status)
                Log.d("Response:", "data:" + resource.data)
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        binding.txtloading.visibility = View.GONE
                        binding.tryagain.visibility = View.GONE
                        resource.data?.let { model ->
                            CharatresList(model)
                        }
                    }
                    Status.ERROR -> {
                        binding.progressBar.visibility = View.GONE
                        binding.txtloading.visibility = View.VISIBLE
                        binding.txtloading.text = "something went wrong please try again"
                        binding.tryagain.visibility = View.VISIBLE
                    }
                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun CharatresList(model: StarWarData) {
        adapter.setDataList(model.characters)
    }


    override fun onListItemClick(data: String) {
        Log.d("onListItemClick", "data:$data")
        val intent = Intent(this, DetailsActivity::class.java)
        val b = Bundle()
        b.putString("url", data)
        intent.putExtras(b)
        startActivity(intent)
    }
}