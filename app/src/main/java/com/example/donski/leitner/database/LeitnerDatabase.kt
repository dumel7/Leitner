package com.example.donski.leitner.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.example.donski.leitner.database.DAO.DeckDao
import com.example.donski.leitner.database.DAO.DeckToSetDao
import com.example.donski.leitner.database.DAO.FlashcardDao
import com.example.donski.leitner.database.DAO.SetDao
import com.example.donski.leitner.database.converters.DateTypeConverter
import com.example.donski.leitner.database.entities.CSet
import com.example.donski.leitner.database.entities.Deck
import com.example.donski.leitner.database.entities.DeckToSet
import com.example.donski.leitner.database.entities.Flashcard

@Database(entities = arrayOf(Deck::class, CSet::class, Flashcard::class, DeckToSet::class),
        version = 5)
@TypeConverters(DateTypeConverter::class)
abstract class LeitnerDatabase :RoomDatabase(){
    abstract fun deckDao(): DeckDao
    abstract fun setDao(): SetDao
    abstract fun flashcardDao(): FlashcardDao
    abstract fun deckToSetDao(): DeckToSetDao

    companion object {
        private var INSTANCE: LeitnerDatabase? = null

        fun getInstance(context: Context): LeitnerDatabase? {
            destroyInstance()
            if(INSTANCE == null){
                synchronized(LeitnerDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            LeitnerDatabase::class.java, "leitner.db")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance(){
            INSTANCE = null
        }
    }
}