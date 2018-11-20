package com.example.donski.leitner.database.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "flashcards", foreignKeys = arrayOf(
        ForeignKey(entity = CSet::class, onDelete = CASCADE, parentColumns = arrayOf("setId"), childColumns = arrayOf("cSet"))
))
data class Flashcard (
        @PrimaryKey(autoGenerate = true) var flashcardId: Int?,
        var term: String,
        var definition: String,
        var cSet: Int
){
}