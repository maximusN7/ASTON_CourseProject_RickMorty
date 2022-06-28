package com.example.aston_courseproject_rickmorty.model.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aston_courseproject_rickmorty.model.LocationForList

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(locations: List<LocationForList>)

    @Query("SELECT * FROM locations")
    fun getAll(): PagingSource<Int, LocationForList>

    @Query("DELETE FROM locations")
    suspend fun deleteAll()
}