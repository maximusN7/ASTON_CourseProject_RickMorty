package com.example.aston_courseproject_rickmorty.model

data class Location(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: Array<String>,
    val url: String,
    val created: String
) {
}

class AllLocations(val info: LocationsInfo, val results: MutableList<Location>)

class LocationsInfo(val count: Int, val pages: Int, val next: String?, val prev: String?)