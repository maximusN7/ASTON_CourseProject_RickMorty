package com.example.aston_courseproject_rickmorty.retrofit

import com.example.aston_courseproject_rickmorty.model.AllCharacters
import com.example.aston_courseproject_rickmorty.model.Character
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitServices {
    @GET("character")
    fun getCharacterList(): Call<AllCharacters>

    @GET("character/1")
    fun getCharacter1(): Call<Character>
}