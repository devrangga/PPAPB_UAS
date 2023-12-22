package com.example.ppapb_uas.database

import android.support.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    var id: Int=0,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "author")
    var author: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "imageUrl")
    var imageUrl: String
)
{
    // Add a no-argument constructor for Firebase deserialization
    constructor() : this(0, "", "","","")
}