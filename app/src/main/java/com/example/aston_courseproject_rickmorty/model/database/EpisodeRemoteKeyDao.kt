package com.example.aston_courseproject_rickmorty.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EpisodeRemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<EpisodeRemoteKey>)

    @Query("SELECT * FROM remote_keys_episode WHERE episodeId = :id")
    suspend fun remoteKeysEpisodeId(id: String): EpisodeRemoteKey?

    @Query("DELETE FROM remote_keys_episode")
    suspend fun deleteAll()
}