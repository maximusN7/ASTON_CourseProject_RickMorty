package com.example.aston_courseproject_rickmorty.model

import android.util.Log
import com.example.aston_courseproject_rickmorty.retrofit.Common
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationDetailsModel(locationID: Int) {

    private val mService: RetrofitServices = Common.retrofitService
    lateinit var currentLocation: Location
    lateinit var characterList: MutableList<Character>

    init {
        loadOneLocation(locationID)
    }

    private fun loadOneLocation(id: Int) {
        mService.getOneLocation(id).enqueue(object :
            Callback<Location> {
            override fun onResponse(
                call: Call<Location>,
                response: Response<Location>
            ) {
                currentLocation = response.body() as Location
                loadSeveralCharacters(currentLocation.residents)
            }

            override fun onFailure(call: Call<Location>, t: Throwable) {
                Log.e("LocationDetailsModel", t.toString())
            }

        })
    }

    private fun loadSeveralCharacters(urls: Array<String>) {
        mService.getSeveralCharacters(separateIdFromUrl(urls)).enqueue(object :
            Callback<MutableList<Character>> {
            override fun onResponse(
                call: Call<MutableList<Character>>,
                response: Response<MutableList<Character>>
            ) {
                characterList = response.body() as MutableList<Character>
            }

            override fun onFailure(call: Call<MutableList<Character>>, t: Throwable) {
                Log.e("LocationDetailsModel", t.toString())
            }

        })
    }

    fun getOneLocation(): Location {
        return currentLocation
    }

    fun getSeveralCharacters(): MutableList<Character> {
        return characterList
    }

    fun separateIdFromUrl(urlArray: Array<String>): String{
        var str = ""
        for (url in urlArray) {
            val baseUrl = "https://rickandmortyapi.com/api/character/"
            str += "${url.substring(baseUrl.length)},"
        }

        return str.dropLast(1)
    }
}