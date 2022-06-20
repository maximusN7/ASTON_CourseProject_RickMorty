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

    init {
        loadOneCharacter(characterID)
    }

    private fun loadOneCharacter(id: Int, url: String = "https://rickandmortyapi.com/api/character/") {
        val query = if (id != 0) "$url${id}" else url
        mService.getOneCharacter(query).enqueue(object :
            Callback<Character> {
            override fun onResponse(
                call: Call<Character>,
                response: Response<Character>
            ) {
                currentCharacter = response.body() as Character
            }

            override fun onFailure(call: Call<Character>, t: Throwable) {
                Log.e("CharacterModel", t.toString())
            }

        })
    }

    fun getOneCharacter(): Character {
        return currentCharacter
    }
}