package com.example.ppapb_uas.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDao {
    @Query("SELECT * FROM note_table")
    fun getAllFilm(): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFilm(film: List<Note>)

    @Delete
    fun deleteFilm(film: Note)

    @Query("DELETE FROM note_table")
    fun deleteAllFilm()

}