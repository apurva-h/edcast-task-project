package com.example.demoapp.view

import android.R
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demoapp.adapter.MainAdapter
import com.example.demoapp.databinding.ActivityMainBinding
import com.example.demoapp.model.StarWarData
import com.example.demoapp.model.Status
import com.example.demoapp.network.ApiHelper
import com.example.demoapp.network.RetrofitBuilder
import com.example.demoapp.receiver.ConnectionReceiver
import com.example.demoapp.viewmodel.MainViewModel
import com.example.demoapp.viewmodel.ViewModelProvider
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity(), MainAdapter.ItemClickListener,
    ConnectionReceiver.ReceiverListener {

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
            binding.txtloading.visibility = View.GONE
            setupObservers()
        })
    }

    override fun onResume() {
        super.onResume()
        checkConnection()
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

    private fun checkConnection() {
        IntentFilter().apply {
            this.addAction("android.new.conn.CONNECTIVITY_CHANGE")
            registerReceiver(ConnectionReceiver(), this)
        }
        ConnectionReceiver.Listener = this
        // Initialize connectivity manager
        val manager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // Initialize network info
        val networkInfo = manager.activeNetworkInfo
        // get connection status
        val isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting
        showToast(isConnected)
    }

    private fun showToast(isConnected: Boolean) {
        val message: String
        // check condition
        if (isConnected) {
            // when internet is connected set message
            message = "Connected to Internet"
            binding.txtloading.text = message
        } else {
            // when internet is disconnected
            message = "Not Connected to Internet"
            binding.txtloading.text = message
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onNetworkChange(isConnected: Boolean) {
        // display snack bar
        showToast(isConnected)
    }
}