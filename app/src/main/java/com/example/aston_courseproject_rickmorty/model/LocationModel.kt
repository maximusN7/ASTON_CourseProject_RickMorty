package com.example.aston_courseproject_rickmorty.model

import android.util.Log
import com.example.aston_courseproject_rickmorty.retrofit.Common
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationModel {

    private val mService: RetrofitServices = Common.retrofitService
    var listOfLocations: MutableList<Location> = mutableListOf()

    init {
        loadAllLocations()
    }

    private fun loadAllLocations() {
        mService.getLocationList().enqueue(object : Callback<AllLocations> {
            override fun onResponse(
                call: Call<AllLocations>,
                response: Response<AllLocations>
            ) {
                val resp = response.body() as AllLocations
                val list = resp.results.map { it.copy() }
                listOfLocations = list.toMutableList()
            }

            override fun onFailure(call: Call<AllLocations>, t: Throwable) {
                Log.e("LocationModel", t.toString())
            }

        })
    }

    fun getLocationList(): MutableList<Location> {
        return listOfLocations
    }
}