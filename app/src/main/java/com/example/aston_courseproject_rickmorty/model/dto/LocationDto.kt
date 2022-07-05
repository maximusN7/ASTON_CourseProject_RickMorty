package com.example.aston_courseproject_rickmorty.model.dto

import com.example.aston_courseproject_rickmorty.model.Location
import com.example.aston_courseproject_rickmorty.model.database.ItemsDatabase
import com.example.aston_courseproject_rickmorty.model.database.LocationDb
import com.example.aston_courseproject_rickmorty.model.database.LocationForListDb

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
) {
    companion object {
        fun locationToForListDto(location: Location): LocationForListDto {
            return LocationForListDto(
                id = location.id,
                name = location.name,
                type = location.type,
                dimension = location.dimension
            )
        }

        fun locationToForListDto(location: LocationDb): LocationForListDto {
            return LocationForListDto(
                id = location.id,
                name = location.name,
                type = location.type,
                dimension = location.dimension
            )
        }

        fun locationToForListDto(location: LocationForListDb): LocationForListDto {
            return LocationForListDto(
                id = location.id,
                name = location.name,
                type = location.type,
                dimension = location.dimension
            )
        }

        fun locationToForListDto(locations: List<LocationDb>): MutableList<LocationForListDto> {
            val newMutableList = mutableListOf<LocationForListDto>()
            for (location in locations) {
                newMutableList.add(locationToForListDto(location))
            }
            return newMutableList
        }

        fun locationToForListDto(locations: List<Location>, network: Boolean = true): MutableList<LocationForListDto> {
            val newMutableList = mutableListOf<LocationForListDto>()
            for (location in locations) {
                newMutableList.add(locationToForListDto(location))
            }
            return newMutableList
        }

        fun locationToForListDto(locations: List<LocationForListDb>, db: Int = 1): MutableList<LocationForListDto> {
            val newMutableList = mutableListOf<LocationForListDto>()
            for (location in locations) {
                newMutableList.add(locationToForListDto(location))
            }
            return newMutableList
        }
    }
}