package com.example.aston_courseproject_rickmorty.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Episode(
    val id: Int? = null,
    val name: String? = null,
    val air_date: String? = null,
    val episode: String? = null,
    val characters: Array<String>? = null,
    val url: String? = null,
    val created: String? = null
)

@Entity(tableName = "episodes")
data class EpisodeForList(
    @PrimaryKey val id: Int?,
    val name: String?,
    val air_date: String?,
    val episode: String?,
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

class AllEpisodes(val info: Info, val results: MutableList<Episode>)