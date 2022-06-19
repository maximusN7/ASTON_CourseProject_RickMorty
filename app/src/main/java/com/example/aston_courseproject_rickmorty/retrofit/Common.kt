package com.example.aston_courseproject_rickmorty.retrofit

object Common {
    private val BASE_URL = "https://rickandmortyapi.com/api/"
    val retrofitService: RetrofitServices
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitServices::class.java)
}