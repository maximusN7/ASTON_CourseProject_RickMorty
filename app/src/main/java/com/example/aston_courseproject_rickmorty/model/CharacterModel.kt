package com.example.aston_courseproject_rickmorty.model

import android.util.Log
import com.example.aston_courseproject_rickmorty.retrofit.Common
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterModel {

    private val mService: RetrofitServices = Common.retrofitService

    /*init {
        getAllCharacters()
    }*/

    private fun get1Character() {
        mService.getCharacter1().enqueue(object : Callback<Character> {
            override fun onResponse(
                call: Call<Character>,
                response: Response<Character>
            ) {
                val resp = response.body() as Character
                Log.e("AAA", resp.name ?: "null")
                //ListOfCharacters.add(response.body() as Character)
            }

            override fun onFailure(call: Call<Character>, t: Throwable) {
                Log.e("AAA", t.toString())
            }

        })
    }

    private fun getAllCharacters() {
        mService.getCharacterList().enqueue(object : Callback<AllCharacters> {
            override fun onResponse(
                call: Call<AllCharacters>,
                response: Response<AllCharacters>
            ) {
                val resp = response.body() as AllCharacters
                ListOfCharacters = resp.results
            }

            override fun onFailure(call: Call<AllCharacters>, t: Throwable) {
                Log.e("AAA", t.toString())
            }

        })
    }

    companion object {
        var ListOfCharacters = mutableListOf<Character>()
    }

//    val connectivityManager = context.getSystemService(
//        Context.CONNECTIVITY_SERVICE
//    ) as ConnectivityManager
//
//    init {
//        connectivityManager.activeNetworkInfo.isConnected
//    }


}