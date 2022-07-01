package com.example.aston_courseproject_rickmorty.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.aston_courseproject_rickmorty.model.Episode

@Entity(tableName = "episodes_full_info")
data class EpisodeDb(
    @PrimaryKey val id: Int? = null,
    val name: String? = null,
    val air_date: String? = null,
    val episode: String? = null,
    val url: String? = null,
    val created: String? = null
) {
    companion object {
        fun episodeToDb(episode: Episode): EpisodeDb {
            return EpisodeDb(
                id = episode.id,
                name = episode.name,
                air_date = episode.air_date,
                episode = episode.episode,
                url = episode.url,
                created = episode.created
            )
        }

        fun episodeToDb(episodes: MutableList<Episode>): MutableList<EpisodeDb> {
            val newMutableList = mutableListOf<EpisodeDb>()
            for (episode in episodes) {
                newMutableList.add(episodeToDb(episode))
            }
            return newMutableList
        }
    }
}

data class EpisodeForListDb(
    val id: Int? = null,
    val name: String? = null,
    val air_date: String? = null,
    val episode: String? = null,
)