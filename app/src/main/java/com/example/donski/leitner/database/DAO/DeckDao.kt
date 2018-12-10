package com.example.donski.leitner.database.DAO

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.example.donski.leitner.database.entities.Deck

@Dao
interface DeckDao {

    @Query("SELECT * from decks")
    fun getAll(): List<Deck>

    @Update
    fun update(deck: Deck)

    @Insert(onConflict = REPLACE)
    fun insert(deck: Deck)

    @Query("DELETE from decks")
    fun deleteAll()

    @Delete
    fun delete(deck: Deck)
}