package com.example.aston_courseproject_rickmorty.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.aston_courseproject_rickmorty.model.Character

@Entity(tableName = "characters_full_info")
data class CharacterDb(
    @PrimaryKey val id: Int? = null,
    val name: String? = null,
    val status: String? = null,
    val species: String? = null,
    val type: String? = null,
    val gender: String? = null,
    val origin_name: String? = null,
    val origin_url: String? = null,
    val location_name: String? = null,
    val location_url: String? = null,
    val image: String? = null,
    val url: String? = null,
    val created: String? = null
) {
    companion object {
        fun characterToDb(character: Character): CharacterDb {
            return CharacterDb(
                id = character.id,
                name = character.name,
                status = character.status,
                species = character.species,
                type = character.type,
                gender = character.gender,
                origin_name = character.origin?.name,
                origin_url = character.origin?.url,
                location_name = character.location?.name,
                location_url = character.location?.url,
                image = character.image,
                url = character.url,
                created = character.created
            )
        }

        fun characterToDb(characters: MutableList<Character>): MutableList<CharacterDb> {
            val newMutableList = mutableListOf<CharacterDb>()
            for (character in characters) {
                newMutableList.add(characterToDb(character))
            }
            return newMutableList
        }
    }
}

data class CharacterForListDb(
    val id: Int? = null,
    val name: String? = null,
    val status: String? = null,
    val species: String? = null,
    val gender: String? = null,
    val image: String? = null
)