package com.example.aston_courseproject_rickmorty.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Location(
    val id: Int? = null,
    val name: String? = null,
    val type: String? = null,
    val dimension: String? = null,
    val residents: Array<String>? = null,
    val url: String? = null,
    val created: String? = null
)

@Entity(tableName = "locations")
data class LocationForList(
    @PrimaryKey val id: Int? = null,
    val name: String? = null,
    val type: String? = null,
    val dimension: String? = null,
) {
    companion object {
        fun convertLocationForList(locations: MutableList<Location>): MutableList<LocationForList> {
            val newMutableList = mutableListOf<LocationForList>()
            for (location in locations) {
                newMutableList.add(
                    LocationForList(
                        location.id,
                        location.name,
                        location.type,
                        location.dimension
                    )
                )
            }
            return newMutableList
        }

        fun convertLocationForList(location: Location?): LocationForList {
            return if (location == null) LocationForList() else
                LocationForList(
                    location.id,
                    location.name,
                    location.type,
                    location.dimension
                )
        }
    }
}

class AllLocations(val info: Info, val results: MutableList<Location>)