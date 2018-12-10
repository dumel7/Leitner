package com.example.donski.leitner.database.DAO

import android.arch.persistence.room.*
import com.example.donski.leitner.database.entities.Flashcard

@Dao
interface FlashcardDao {

    @Query("SELECT * from flashcards")
    fun getAll(): List<Flashcard>

    @Query("SELECT * from flashcards where cSet = :setId")
    fun getAllBySetId(setId: Int): List<Flashcard>

    @Query("SELECT * FROM flashcards where cSet in (SELECT cSet from deck_to_set where deck = :deckId)")
    fun getAllByDeckId(deckId: Int): List<Flashcard>

    @Update
    fun update(flashcard: Flashcard)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(flashcard: Flashcard)

    @Query("DELETE from flashcards")
    fun deleteAll()

    @Delete
    fun delete(flashcard: Flashcard)

    @Delete
    fun delete(vararg flashcards: Flashcard)

    @Query("UPDATE flashcards SET box = 0 where cSet in (SELECT cSet from deck_to_set where deck = :deckId)")
    fun resetBoxes(deckId: Int)

    @Query("SELECT count(*) FROM flashcards where cSet in (SELECT cSet from deck_to_set where deck = :deckId)")
    fun getCountByDeckId(deckId: Int): Int

    @Query("SELECT count(*) FROM flashcards where cSet = :setId")
    fun getCountBySetId(setId: Int): Int

}