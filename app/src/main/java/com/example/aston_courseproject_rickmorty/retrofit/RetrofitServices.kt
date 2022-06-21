package com.example.aston_courseproject_rickmorty.retrofit

import com.example.aston_courseproject_rickmorty.model.AllCharacters
import com.example.aston_courseproject_rickmorty.model.AllLocations
import com.example.aston_courseproject_rickmorty.model.Character
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url


interface RetrofitServices {
    @GET("character")
    fun getCharacterList(): Call<AllCharacters>

    @GET("location")
    fun getLocationList(): Call<AllLocations>

    @GET
    fun getOneCharacter(@Url url: String?): Call<Character>
}