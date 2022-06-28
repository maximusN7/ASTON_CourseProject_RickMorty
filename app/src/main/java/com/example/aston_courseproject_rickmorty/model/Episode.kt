package com.example.aston_courseproject_rickmorty.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Episode(
    val id: Int,
    val name: String,
    val air_date: String,
    val episode: String,
    val characters: Array<String>,
    val url: String,
    val created: String
) {
    companion object {
        fun convertEpisodeForList(locations: MutableList<Episode>): MutableList<EpisodeForList> {
            val newMutableList = mutableListOf<EpisodeForList>()
            for (location in locations) {
                newMutableList.add(EpisodeForList(location.id, location.name, location.air_date, location.episode))
            }
            return newMutableList
        }
    }
}

@Entity(tableName = "episodes")
data class EpisodeForList(
    @PrimaryKey val id: Int?,
    val name: String?,
    val air_date: String?,
    val episode: String?,
)

class AllEpisodes(val info: Info, val results: MutableList<Episode>)