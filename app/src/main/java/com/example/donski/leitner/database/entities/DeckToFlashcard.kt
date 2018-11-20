package com.example.donski.leitner.database.entities

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "deck_to_flashcard", foreignKeys = arrayOf(
        ForeignKey(entity = Deck::class, onDelete = CASCADE, parentColumns = arrayOf("deckId"), childColumns = arrayOf("deck")),
        ForeignKey(entity = Flashcard::class, onDelete = CASCADE, parentColumns = arrayOf("flashcardId"), childColumns = arrayOf("flashcard"))
))
data class DeckToFlashcard (
        @PrimaryKey(autoGenerate = true) var id: Int?,
        var deck: Int,
        var flashcard: Int,
        var box: Int
){
}