package com.example.aston_courseproject_rickmorty.model.dto

import android.util.Log
import com.example.aston_courseproject_rickmorty.model.Character
import com.example.aston_courseproject_rickmorty.model.LocationForCharacter
import com.example.aston_courseproject_rickmorty.model.database.CharacterDb
import com.example.aston_courseproject_rickmorty.model.database.CharacterForListDb
import com.example.aston_courseproject_rickmorty.model.database.EpisodeForListDb
import com.example.aston_courseproject_rickmorty.model.database.ItemsDatabase

data class CharacterDto(
    val id: Int? = null,
    val name: String? = null,
    val status: String? = null,
    val species: String? = null,
    val type: String? = null,
    val gender: String? = null,
    val origin: LocationForCharacter? = null,
    val location: LocationForCharacter?  = null,
    val image: String? = null,
    val episode: Array<String>?  = null
) {
    companion object {
        fun characterToDto(character: Character): CharacterDto {
            return CharacterDto(
                id = character.id,
                name = character.name,
                status = character.status,
                species = character.species,
                type = character.type,
                gender = character.gender,
                origin = character.origin,
                location = character.location,
                image = character.image,
                episode = character.episode
            )
        }
        fun characterToDto(character: CharacterDb, database: ItemsDatabase): CharacterDto {
            val array: Array<Int> =
                database.getCharacterEpisodeJoinDao().getEpisodesIdForCharacter(character.id!!)
            val listForObj: MutableList<String> = mutableListOf()
            val url = "https://rickandmortyapi.com/api/episode/"
            for (i in array) {
                listForObj.add("${url}$i")
            }
            val arrayForObj = listForObj.toTypedArray()
            return CharacterDto(
                id = character.id,
                name = character.name,
                status = character.status,
                species = character.species,
                type = character.type,
                gender = character.gender,
                origin = LocationForCharacter(character.origin_name, character.origin_url),
                location = LocationForCharacter(character.location_name, character.location_url),
                image = character.image,
                episode = arrayForObj
            )
        }
    }
}

data class CharacterForListDto(
    val id: Int?,
    val name: String?,
    val status: String?,
    val species: String?,
    val gender: String?,
    val image: String?
) {
    companion object {
        fun characterToForListDto(character: Character): CharacterForListDto {
            return CharacterForListDto(
                id = character.id,
                name = character.name,
                status = character.status,
                species = character.species,
                gender = character.gender,
                image = character.image
            )
        }
        fun characterToForListDto(character: CharacterDb): CharacterForListDto {
            return CharacterForListDto(
                id = character.id,
                name = character.name,
                status = character.status,
                species = character.species,
                gender = character.gender,
                image = character.image
            )
        }
        fun characterToForListDto(characters: List<Character>, network: Boolean = true): MutableList<CharacterForListDto> {
            val newMutableList = mutableListOf<CharacterForListDto>()
            for (character in characters) {
                newMutableList.add(characterToForListDto(character))
            }
            return newMutableList
        }
        fun characterToForListDto(characters: List<CharacterDb>): MutableList<CharacterForListDto> {
            val newMutableList = mutableListOf<CharacterForListDto>()
            for (character in characters) {
                newMutableList.add(characterToForListDto(character))
            }
            return newMutableList
        }

        fun characterToForListDto(character: CharacterForListDb): CharacterForListDto {
            return CharacterForListDto(
                id = character.id,
                name = character.name,
                status = character.status,
                species = character.species,
                gender = character.gender,
                image = character.image
            )
        }

        fun characterToForListDto(characters: List<CharacterForListDb>, db: Int = 1): MutableList<CharacterForListDto> {
            val newMutableList = mutableListOf<CharacterForListDto>()
            for (character in characters) {
                if (character != null) {
                    newMutableList.add(characterToForListDto(character))
                }
            }
            return newMutableList
        }
    }
}