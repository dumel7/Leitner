package com.example.donski.leitner.database.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "deck_to_set", foreignKeys = arrayOf(
        ForeignKey(entity = Deck::class, onDelete = CASCADE, parentColumns = arrayOf("deckId"), childColumns = arrayOf("deck")),
        ForeignKey(entity = CSet::class, onDelete = CASCADE, parentColumns = arrayOf("setId"), childColumns = arrayOf("cSet"))))
data class DeckToSet (
        @PrimaryKey(autoGenerate = true) var id: Int?,
        var deck: Int,
        var cSet: Int
){
}