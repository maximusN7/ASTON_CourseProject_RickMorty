package com.example.aston_courseproject_rickmorty.model

import android.util.Log
import com.example.aston_courseproject_rickmorty.retrofit.Common
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterDetailsModel(characterID: Int) {

    private val mService: RetrofitServices = Common.retrofitService
    lateinit var currentCharacter: Character
    lateinit var currentOrigin: Location
    lateinit var currentLocation: Location
    lateinit var episodeList: MutableList<Episode>

    init {
        loadOneCharacter(characterID)
    }

    private fun loadOneCharacter(id: Int) {
        mService.getOneCharacter(id).enqueue(object :
            Callback<Character> {
            override fun onResponse(
                call: Call<Character>,
                response: Response<Character>
            ) {
                currentCharacter = response.body() as Character
                loadOneOrigin(currentCharacter.origin.url!!)
                loadOneLocation(currentCharacter.location.url!!)
            }

            override fun onFailure(call: Call<Character>, t: Throwable) {
                Log.e("CharacterDetailsModel", t.toString())
            }

        })
    }

    private fun loadOneLocation(url: String) {
        mService.getOneLocationByUrl(url).enqueue(object :
            Callback<Location> {
            override fun onResponse(
                call: Call<Location>,
                response: Response<Location>
            ) {
                currentLocation = response.body() as Location
            }

            override fun onFailure(call: Call<Location>, t: Throwable) {
                Log.e("CharacterDetailsModel", t.toString())
            }

        })
    }

    private fun loadOneOrigin(url: String) {
        mService.getOneLocationByUrl(url).enqueue(object :
            Callback<Location> {
            override fun onResponse(
                call: Call<Location>,
                response: Response<Location>
            ) {
                currentOrigin = response.body() as Location
            }

            override fun onFailure(call: Call<Location>, t: Throwable) {
                Log.e("CharacterDetailsModel", t.toString())
            }

        })
    }

    fun getOneCharacter(): Character {
        return currentCharacter
    }

    fun getOrigin(): Location {
        return currentOrigin
    }

    fun getLocation(): Location {
        return currentLocation
    }
}