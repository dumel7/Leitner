package com.example.donski.leitner.database.entities

import android.arch.persistence.room.*
import android.arch.persistence.room.ForeignKey.CASCADE
import com.example.donski.leitner.database.converters.DateTypeConverter
import java.util.*

@Entity(tableName = "flashcards", foreignKeys = arrayOf(
        ForeignKey(entity = CSet::class, onDelete = CASCADE, parentColumns = arrayOf("setId"), childColumns = arrayOf("cSet"))
))
data class Flashcard (
        @PrimaryKey(autoGenerate = true) var flashcardId: Int?,
        var term: String,
        var definition: String,
        var cSet: Int,
        var box: Int,
        @TypeConverters(DateTypeConverter::class)
        @ColumnInfo(name="last_learn") var lastLearn: Date
){
        fun setBoxAndLastLearn(box: Int, lastLearn: Date){
                this.lastLearn=lastLearn
                this.box=box
        }
}