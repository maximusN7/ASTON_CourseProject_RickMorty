package com.example.aston_courseproject_rickmorty.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocationRemoteKeyDao  {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<LocationRemoteKey>)

    @Query("SELECT * FROM remote_keys_location WHERE locationId = :id")
    suspend fun remoteKeysLocationId(id: String): LocationRemoteKey?

    @Query("DELETE FROM remote_keys_location")
    suspend fun deleteAll()
}