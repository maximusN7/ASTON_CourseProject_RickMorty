package com.example.aston_courseproject_rickmorty.model

import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.example.aston_courseproject_rickmorty.retrofit.Common
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterModel {

    private val mService: RetrofitServices = Common.retrofitService
    var listOfCharacters: MutableList<Character> = mutableListOf()


    init {
        loadAllCharacters()
    }

    private fun loadOneCharacter() {
        mService.getCharacter1().enqueue(object : Callback<Character> {
            override fun onResponse(
                call: Call<Character>,
                response: Response<Character>
            ) {
                val resp = response.body() as Character
                //ListOfCharacters.add(response.body() as Character)
            }

            override fun onFailure(call: Call<Character>, t: Throwable) {
                Log.e("CharacterModel", t.toString())
            }

        })
    }

    private fun loadAllCharacters() {
        mService.getCharacterList().enqueue(object : Callback<AllCharacters> {
            override fun onResponse(
                call: Call<AllCharacters>,
                response: Response<AllCharacters>
            ) {
                val resp = response.body() as AllCharacters
                val list = resp.results.map { it.copy() }
                listOfCharacters = list.toMutableList()
            }

            override fun onFailure(call: Call<AllCharacters>, t: Throwable) {
                Log.e("CharacterModel", t.toString())
            }

        })
    }

    fun getCharacterList(): MutableList<Character> {
        return listOfCharacters
    }

//    val connectivityManager = context.getSystemService(
//        Context.CONNECTIVITY_SERVICE
//    ) as ConnectivityManager
//
//    init {
//        connectivityManager.activeNetworkInfo.isConnected
//    }


}