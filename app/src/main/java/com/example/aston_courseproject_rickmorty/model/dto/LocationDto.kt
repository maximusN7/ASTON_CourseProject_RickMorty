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
    val residents: Array<String>? = null
) {
    companion object {
        fun locationToDto(location: Location): LocationDto {
            return LocationDto(
                id = location.id,
                name = location.name,
                type = location.type,
                dimension = location.dimension,
                residents = location.residents
            )
        }
        fun locationToDto(location: LocationDb, database: ItemsDatabase): LocationDto {
            val array: Array<Int> =
                database.getLocationCharacterJoinDao().getCharactersIdForLocation(location.id!!)
            val listForObj: MutableList<String> = mutableListOf()
            val url = "https://rickandmortyapi.com/api/character/"
            for (i in array) {
                listForObj.add("${url}$i")
            }
            val arrayForObj = listForObj.toTypedArray()
            return LocationDto(
                id = location.id,
                name = location.name,
                type = location.type,
                dimension = location.dimension,
                residents = arrayForObj
            )
        }
    }

}

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