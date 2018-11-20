package com.example.donski.leitner.database.DAO

import android.arch.persistence.room.*
import com.example.donski.leitner.database.entities.Flashcard

@Dao
interface FlashcardDao {

    @Query("SELECT * from flashcards")
    fun getAll(): List<Flashcard>

    @Query("SELECT * from flashcards where cSet = :setId")
    fun getAllBySetId(setId: Int): List<Flashcard>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(flashcard: Flashcard)

    @Query("DELETE from flashcards")
    fun deleteAll()

    @Delete
    fun delete(flashcard: Flashcard)

    @Delete
    fun delete(vararg flashcards: Flashcard)
}