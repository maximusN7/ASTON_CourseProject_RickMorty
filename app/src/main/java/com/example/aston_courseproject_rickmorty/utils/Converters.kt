package com.example.aston_courseproject_rickmorty.utils

import com.example.aston_courseproject_rickmorty.model.Character
import com.example.aston_courseproject_rickmorty.model.Episode
import com.example.aston_courseproject_rickmorty.model.Location
import com.example.aston_courseproject_rickmorty.model.database.CharacterEpisodeJoin
import com.example.aston_courseproject_rickmorty.model.database.EpisodeCharacterJoin
import com.example.aston_courseproject_rickmorty.model.database.LocationCharacterJoin

class Converters {
    companion object {

        fun convertToCEJoin(characters: MutableList<Character>): MutableList<CharacterEpisodeJoin> {
            val listToDb: MutableList<CharacterEpisodeJoin> = mutableListOf()
            for (character in characters) {
                for (i in 0 until character.episode?.size!!) {
                    val episodeId = Separators.separateIdFromUrlEpisode(character.episode[i])
                    val ceJoin = CharacterEpisodeJoin(character.id!!, episodeId)
                    listToDb.add(ceJoin)
                }
            }
            return listToDb
        }

        fun convertToECJoin(episodes: MutableList<Episode>): MutableList<EpisodeCharacterJoin> {
            val listToDb: MutableList<EpisodeCharacterJoin> = mutableListOf()
            for (episode in episodes) {
                for (i in 0 until episode.characters?.size!!) {
                    val characterId = Separators.separateIdFromUrlCharacter(episode.characters[i])
                    val ceJoin = EpisodeCharacterJoin(episode.id!!, characterId)
                    listToDb.add(ceJoin)
                }
            }
            return listToDb
        }

        fun convertToLCJoin(locations: MutableList<Location>): MutableList<LocationCharacterJoin> {
            val listToDb: MutableList<LocationCharacterJoin> = mutableListOf()
            for (location in locations) {
                for (i in 0 until location.residents?.size!!) {
                    val characterId = Separators.separateIdFromUrlCharacter(location.residents[i])
                    val ceJoin = LocationCharacterJoin(location.id!!, characterId)
                    listToDb.add(ceJoin)
                }
            }
            return listToDb
        }
    }
}