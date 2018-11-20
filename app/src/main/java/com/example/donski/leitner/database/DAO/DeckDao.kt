package com.example.donski.leitner.database.DAO

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.example.donski.leitner.database.entities.Deck

@Dao
interface DeckDao {

    @Query("SELECT * from decks")
    fun getAll(): List<Deck>

    @Insert(onConflict = REPLACE)
    fun insert(deck: Deck)

    @Query("DELETE from decks")
    fun deleteAll()

    @Delete
    fun delete(deck: Deck)
}