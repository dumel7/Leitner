package com.example.donski.leitner.database.entities

import android.arch.persistence.room.*
import com.example.donski.leitner.database.converters.DateTypeConverter
import java.util.*

@Entity(tableName="decks")
data class Deck (@PrimaryKey(autoGenerate = true) var deckId: Int?,
                 var name: String,
                 @TypeConverters(DateTypeConverter::class)
                 @ColumnInfo(name="last_learn") var lastLearn: Date){
    constructor():this(null,"", Date())
}