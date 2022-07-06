package com.example.aston_courseproject_rickmorty.model.dto


data class LocationDto(
    val id: Int? = null,
    val name: String? = null,
    val type: String? = null,
    val dimension: String? = null,
    val residents: String? = null
)

data class LocationForListDto(
    val id: Int? = null,
    val name: String? = null,
    val type: String? = null,
    val dimension: String? = null,
)