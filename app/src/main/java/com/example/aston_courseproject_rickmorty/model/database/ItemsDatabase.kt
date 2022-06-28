package com.example.aston_courseproject_rickmorty.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.aston_courseproject_rickmorty.model.CharacterForList
import com.example.aston_courseproject_rickmorty.model.EpisodeForList
import com.example.aston_courseproject_rickmorty.model.LocationForList

@Database(version = 1, entities = [CharacterForList::class, LocationForList::class, EpisodeForList::class, CharacterRemoteKey::class, LocationRemoteKey::class, EpisodeRemoteKey::class])
abstract class ItemsDatabase : RoomDatabase() {
    abstract fun getCharacterDao(): CharacterDao
    abstract fun getLocationDao(): LocationDao
    abstract fun getEpisodeDao(): EpisodeDao
    abstract fun getCharacterKeysDao(): CharacterRemoteKeyDao
    abstract fun getLocationKeysDao(): LocationRemoteKeyDao
    abstract fun getEpisodeKeysDao(): EpisodeRemoteKeyDao

    companion object {
        private var instance: ItemsDatabase? = null
        fun getDatabase(appContext: Context) : ItemsDatabase {
            if (instance == null) {
                synchronized(this) {
                    instance = Room.databaseBuilder(appContext,
                        ItemsDatabase::class.java,
                        "DataForLists").build()
                }
            }
            return instance!!
        }
    }
}