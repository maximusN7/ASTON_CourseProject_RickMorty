package com.example.aston_courseproject_rickmorty.model.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aston_courseproject_rickmorty.model.database.CharacterDb
import com.example.aston_courseproject_rickmorty.model.database.CharacterForListDb
import com.example.aston_courseproject_rickmorty.model.dto.CharacterForListDto

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterDb>)

    @Query("SELECT id, name, status, species, gender, image FROM characters_full_info")
    fun getAll(): PagingSource<Int, CharacterForListDto>

    @Query("SELECT * FROM characters_full_info WHERE id = :characterId")
    suspend fun getOneById(characterId: Int): CharacterDb

    @Query("SELECT id, name, status, species, gender, image FROM characters_full_info WHERE id = :characterId")
    suspend fun getOneForListById(characterId: Int): CharacterForListDb

    @Query("DELETE FROM characters_full_info")
    suspend fun deleteAll()
}