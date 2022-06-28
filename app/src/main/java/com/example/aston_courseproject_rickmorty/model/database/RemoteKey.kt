package com.example.aston_courseproject_rickmorty.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey val characterId: String,
    val prevKey: Int?,
    val nextKey: Int?
)
