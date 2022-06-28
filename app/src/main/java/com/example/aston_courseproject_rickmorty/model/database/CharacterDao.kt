package com.example.aston_courseproject_rickmorty.model.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aston_courseproject_rickmorty.model.Character
import com.example.aston_courseproject_rickmorty.model.CharacterForList

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterForList>)

    @Query("SELECT * FROM characters")
    fun getAll(): PagingSource<Int, CharacterForList>

    @Query("DELETE FROM characters")
    suspend fun deleteAll()
}