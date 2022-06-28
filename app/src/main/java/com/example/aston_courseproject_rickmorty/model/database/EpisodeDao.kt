package com.example.aston_courseproject_rickmorty.model.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aston_courseproject_rickmorty.model.EpisodeForList

@Dao
interface EpisodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(episodes: List<EpisodeForList>)

    @Query("SELECT * FROM episodes")
    fun getAll(): PagingSource<Int, EpisodeForList>

    @Query("DELETE FROM episodes")
    suspend fun deleteAll()
}
