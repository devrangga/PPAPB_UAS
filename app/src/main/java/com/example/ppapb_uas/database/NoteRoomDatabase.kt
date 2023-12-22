package com.example.ppapb_uas.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteRoomDatabase : RoomDatabase() {
    abstract fun dao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NoteRoomDatabase? = null

        fun getDatabase(context: Context): NoteRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteRoomDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}