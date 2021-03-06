package com.example.donski.leitner.database.DAO

import android.arch.persistence.room.*
import com.example.donski.leitner.database.entities.DeckToSet

@Dao
interface DeckToSetDao {

    @Query("SELECT * from deck_to_set")
    fun getAll(): List<DeckToSet>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(deckToSet: DeckToSet)

    @Query("DELETE from deck_to_set")
    fun deleteAll()
}