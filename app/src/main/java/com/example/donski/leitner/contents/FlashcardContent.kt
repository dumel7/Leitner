package com.example.donski.leitner.contents

import com.example.donski.leitner.database.entities.Flashcard
import java.util.*

object FlashcardContent {

    /**
     * An array of flashcard items.
     */
    val ITEMS: MutableList<FlashcardItem> = ArrayList()

    /**
     * A map of flashcard items, by ID.
     */
    val ITEM_MAP: MutableMap<Int, FlashcardItem> = HashMap()


    fun clear(){
        ITEMS.clear()
        ITEM_MAP.clear()
    }

    fun fill(flashcards :List<Flashcard>){
        flashcards.map { flashcard -> addItem(FlashcardContent.FlashcardItem(flashcard))   }
    }

    private fun addItem(item: FlashcardItem) {
        ITEMS.add(item)
        ITEM_MAP[item.flashcard.flashcardId!!] = item
    }


    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0 until position) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

    /**
     * A flashcard item representing a piece of content.
     */
    class FlashcardItem(val flashcard: Flashcard) {

        override fun toString(): String {
            val builder = StringBuilder()
            builder.append(flashcard.term)
            builder.append("\n\t"+flashcard.definition)
            return builder.toString()
        }
    }
}
