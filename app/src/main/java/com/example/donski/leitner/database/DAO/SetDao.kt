package com.example.donski.leitner.database.DAO

import android.arch.persistence.room.*
import com.example.donski.leitner.database.entities.Deck
import com.example.donski.leitner.database.entities.CSet

@Dao
interface SetDao {

    @Query("SELECT * from sets")
    fun getAll(): List<CSet>

    @Query("SELECT * from sets where sets.setId in (select cSet from deck_to_set where deck = :deck)")
    fun getAllSetsByDeck(deck: Int): List<CSet>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(CSet: CSet): Long

    @Query("DELETE from sets")
    fun deleteAll()

    @Delete
    fun delete(cSet: CSet)

    @Delete
    fun delete(vararg cSets: CSet)
}