package com.example.donski.leitner.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.example.donski.leitner.database.DAO.*
import com.example.donski.leitner.database.converters.DateTypeConverter
import com.example.donski.leitner.database.entities.*
import com.example.donski.leitner.database.entities.CSet

@Database(entities = arrayOf(Deck::class, CSet::class, Flashcard::class, DeckToFlashcard::class, DeckToSet::class),
        version = 3)
@TypeConverters(DateTypeConverter::class)
abstract class LeitnerDatabase :RoomDatabase(){
    abstract fun deckDao(): DeckDao
    abstract fun setDao(): SetDao
    abstract fun flashcardDao(): FlashcardDao
    abstract fun deckToFlashcardDao(): DeckToFlashcardDao
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