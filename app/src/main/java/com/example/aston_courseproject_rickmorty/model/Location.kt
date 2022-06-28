package com.example.aston_courseproject_rickmorty.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Location(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: Array<String>,
    val url: String,
    val created: String
) {
    companion object {
        fun convertLocationForList(locations: MutableList<Location>): MutableList<LocationForList> {
            val newMutableList = mutableListOf<LocationForList>()
            for (location in locations) {
                newMutableList.add(LocationForList(location.id, location.name, location.type, location.dimension))
            }
            return newMutableList
        }
    }
}

@Entity(tableName = "locations")
data class LocationForList(
    @PrimaryKey val id: Int?,
    val name: String?,
    val type: String?,
    val dimension: String?,
)

class AllLocations(val info: Info, val results: MutableList<Location>)