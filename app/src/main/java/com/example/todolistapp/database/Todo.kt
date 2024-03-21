package com.example.todolistapp.database

import java.util.*
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "todo",
)
data class Todo (
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "uuid") var id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "title") var title: String? = null,
    @ColumnInfo(name = "detail") var detail: String? = null,
    @ColumnInfo(name = "date") var date: Date = Date(),
    @ColumnInfo(name = "isComplete") var isComplete:Boolean = false)

{

    val photoFilename: String
        get() = "img_$id.jpg"

}