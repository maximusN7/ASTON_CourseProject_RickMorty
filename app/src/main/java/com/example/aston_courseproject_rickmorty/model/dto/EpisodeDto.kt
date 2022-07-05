package com.example.aston_courseproject_rickmorty.model.dto

import com.example.aston_courseproject_rickmorty.model.Episode
import com.example.aston_courseproject_rickmorty.model.database.EpisodeDb
import com.example.aston_courseproject_rickmorty.model.database.EpisodeForListDb
import com.example.aston_courseproject_rickmorty.model.database.ItemsDatabase

data class EpisodeDto(
    val id: Int? = null,
    val name: String? = null,
    val air_date: String? = null,
    val episode: String? = null,
    val characters: String? = null
)

data class EpisodeForListDto(
    val id: Int? = null,
    val name: String? = null,
    val air_date: String? = null,
    val episode: String? = null,
) {
    companion object {
        fun episodeToForListDto(episode: Episode): EpisodeForListDto {
            return EpisodeForListDto(
                id = episode.id,
                name = episode.name,
                air_date = episode.air_date,
                episode = episode.episode
            )
        }

        fun episodeToForListDto(episode: EpisodeDb): EpisodeForListDto {
            return EpisodeForListDto(
                id = episode.id,
                name = episode.name,
                air_date = episode.air_date,
                episode = episode.episode
            )
        }

        fun episodeToForListDto(episodes: List<EpisodeDb>): MutableList<EpisodeForListDto> {
            val newMutableList = mutableListOf<EpisodeForListDto>()
            for (episode in episodes) {
                newMutableList.add(episodeToForListDto(episode))
            }
            return newMutableList
        }

        fun episodeToForListDto(episodes: List<Episode>, network: Boolean = true): MutableList<EpisodeForListDto> {
            val newMutableList = mutableListOf<EpisodeForListDto>()
            for (episode in episodes) {
                newMutableList.add(episodeToForListDto(episode))
            }
            return newMutableList
        }

        fun episodeToForListDto(episode: EpisodeForListDb): EpisodeForListDto {
            return EpisodeForListDto(
                id = episode.id,
                name = episode.name,
                air_date = episode.air_date,
                episode = episode.episode
            )
        }

        fun episodeToForListDto(episodes: List<EpisodeForListDb>, db: Int = 1): MutableList<EpisodeForListDto> {
            val newMutableList = mutableListOf<EpisodeForListDto>()
            for (episode in episodes) {
                newMutableList.add(episodeToForListDto(episode))
            }
            return newMutableList
        }
    }
}