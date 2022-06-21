package com.example.aston_courseproject_rickmorty.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

data class Character(
    val id: Int?,
    val name: String?,
    val status: String?,
    val species: String?,
    val type: String?,
    val gender: String?,
    val origin: LocationForCharacter,
    val location: LocationForCharacter,
    val image: String?,
    val episode: Array<String?>,
    val url: String?,
    val created: String?
)

class LocationForCharacter(val name: String?, val url: String?)

class AllCharacters(val info: Info, val results: MutableList<Character>)

class Info(val count: Int, val pages: Int, val next: String?, val prev: String?)