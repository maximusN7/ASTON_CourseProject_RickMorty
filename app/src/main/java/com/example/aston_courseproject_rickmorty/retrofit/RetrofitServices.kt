package com.example.aston_courseproject_rickmorty.retrofit

import com.example.aston_courseproject_rickmorty.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url


interface RetrofitServices {
    @GET("character")
    fun getCharacterList(): Call<AllCharacters>

    @GET("character")
    suspend fun getCharacterPagingList(@Query("page") query: Int): AllCharacters

    @GET("character/{id}")
    fun getOneCharacter(@Path("id") id: Int): Call<Character>

    @GET("character/{ids}")
    fun getSeveralCharacters(@Path("ids") ids: String): Call<MutableList<Character>>

    @GET("location")
    fun getLocationList(): Call<AllLocations>

    @GET("location")
    suspend fun getLocationPagingList(@Query("page") query: Int): AllLocations

    @GET("location/{id}")
    fun getOneLocation(@Path("id") id: Int): Call<Location>

    @GET
    fun getOneLocationByUrl(@Url url: String?): Call<Location>

    @GET("episode")
    fun getEpisodeList(): Call<AllEpisodes>

    @GET("episode")
    suspend fun getEpisodePagingList(@Query("page") query: Int): AllEpisodes

    @GET("episode/{id}")
    fun getOneEpisode(@Path("id") id: Int): Call<Episode>

    @GET("episode/{ids}")
    fun getSeveralEpisodes(@Path("ids") ids: String): Call<MutableList<Episode>>
}