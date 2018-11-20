package com.example.donski.leitner.database.DAO

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.example.donski.leitner.database.entities.DeckToFlashcard

@Dao
interface DeckToFlashcardDao {

    @Query("SELECT * from deck_to_flashcard")
    fun getAll(): List<DeckToFlashcard>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(deckToFlashcard: DeckToFlashcard)

    @Query("DELETE from deck_to_flashcard")
    fun deleteAll()

}