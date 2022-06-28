package com.example.aston_courseproject_rickmorty.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.aston_courseproject_rickmorty.model.CharacterForList

@Database(version = 1, entities = [CharacterForList::class, RemoteKey::class])
abstract class CharacterDatabase : RoomDatabase() {
    abstract fun getCharacterDao(): CharacterDao
    abstract fun getKeysDao(): RemoteKeyDao

    companion object {
        private var instance: CharacterDatabase? = null
        fun getDatabase(appContext: Context) : CharacterDatabase {
            if (instance == null) {
                synchronized(this) {
                    instance = Room.databaseBuilder(appContext,
                        CharacterDatabase::class.java,
                        "CharactersForList").build()
                }
            }
            return instance!!
        }
    }
}