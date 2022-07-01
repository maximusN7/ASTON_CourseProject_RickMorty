package com.example.aston_courseproject_rickmorty.retrofit

import com.example.aston_courseproject_rickmorty.model.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface RetrofitServices {

    @GET("character")
    suspend fun getCharacterPagingList(@Query("page") query: Int): AllCharacters

    @GET("character/{id}")
    suspend fun getOneCharacter(@Path("id") id: Int): Character

    @GET("character/{ids}")
    suspend fun getSeveralCharacters(@Path("ids") ids: String): MutableList<Character>


    @GET("location")
    suspend fun getLocationPagingList(@Query("page") query: Int): AllLocations

    @GET("location/{id}")
    suspend fun getOneLocation(@Path("id") id: Int): Location


    @GET("episode")
    suspend fun getEpisodePagingList(@Query("page") query: Int): AllEpisodes

    @GET("episode/{id}")
    suspend fun getOneEpisode(@Path("id") id: Int): Episode

    @GET("episode/{ids}")
    suspend fun getSeveralEpisodes(@Path("ids") ids: String): MutableList<Episode>
}