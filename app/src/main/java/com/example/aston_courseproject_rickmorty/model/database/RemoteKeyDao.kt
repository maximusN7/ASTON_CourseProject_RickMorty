package com.example.aston_courseproject_rickmorty.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKey>)

    @Query("SELECT * FROM remote_keys WHERE characterId = :id")
    suspend fun remoteKeysCharacterId(id: String): RemoteKey?

    @Query("DELETE FROM remote_keys")
    suspend fun deleteAll()
}