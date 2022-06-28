package com.example.aston_courseproject_rickmorty.model

import androidx.room.Entity
import androidx.room.PrimaryKey


data class Character(
    val id: Int?,
    val name: String?,
    val status: String?,
    val species: String?,
    val type: String?,
    val gender: String?,
    val origin: LocationForCharacter,
    val location: LocationForCharacter,
    val image: String?,
    val episode: Array<String?>,
    val url: String?,
    val created: String?
) {
    companion object {
        fun convertCharacterForList(characters: MutableList<Character>): MutableList<CharacterForList> {
            val newMutableList = mutableListOf<CharacterForList>()
            for (character in characters) {
                newMutableList.add(CharacterForList(character.id, character.name, character.status, character.species,
                    character.gender, character.image))
            }
            return newMutableList
        }
    }
}

@Entity(tableName = "characters")
data class CharacterForList(
    @PrimaryKey val id: Int?,
    val name: String?,
    val status: String?,
    val species: String?,
    val gender: String?,
    val image: String?
)

class LocationForCharacter(val name: String?, val url: String?)

class AllCharacters(val info: Info, val results: MutableList<Character>)

class Info(val count: Int, val pages: Int, val next: String?, val prev: String?)
