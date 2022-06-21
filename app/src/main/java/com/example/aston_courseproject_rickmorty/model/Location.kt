package com.example.aston_courseproject_rickmorty.model

data class Location(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: Array<String>,
    val url: String,
    val created: String
)

class AllLocations(val info: Info, val results: MutableList<Location>)