package com.example.aston_courseproject_rickmorty.model

import android.util.Log
import com.example.aston_courseproject_rickmorty.retrofit.Common
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EpisodeModel {

    private val mService: RetrofitServices = Common.retrofitService
    var listOfEpisodes: MutableList<Episode> = mutableListOf()

    init {
        loadAllEpisodes()
    }

    private fun loadAllEpisodes() {
        mService.getEpisodeList().enqueue(object : Callback<AllEpisodes> {
            override fun onResponse(
                call: Call<AllEpisodes>,
                response: Response<AllEpisodes>
            ) {
                val resp = response.body() as AllEpisodes
                val list = resp.results.map { it.copy() }
                listOfEpisodes = list.toMutableList()
            }

            override fun onFailure(call: Call<AllEpisodes>, t: Throwable) {
                Log.e("LocationModel", t.toString())
            }

        })
    }

    fun getEpisodeList(): MutableList<Episode> {
        return listOfEpisodes
    }
}