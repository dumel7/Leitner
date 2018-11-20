package com.example.donski.leitner.database.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "sets")
data class CSet (
        @PrimaryKey(autoGenerate = true) var setId: Int?,
        var name: String){
    constructor():this(null, "")
}