package com.example.donski.leitner.contents

import com.example.donski.leitner.database.entities.Deck
import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object DeckContent {

    /**
     * An array of sample deck items.
     */
    val ITEMS: MutableList<DeckItem> = ArrayList()

    /**
     * A map of sample deck items, by ID.
     */
    val ITEM_MAP: MutableMap<Int, DeckItem> = HashMap()


    fun clear(){
        ITEMS.clear()
        ITEM_MAP.clear()
    }

    fun fill(decks :List<Deck>){
        decks.map { deck -> addItem(DeckContent.DeckItem(deck))   }
    }

    private fun addItem(item: DeckItem) {
        ITEMS.add(item)
        ITEM_MAP.put(item.deck.deckId!!, item)
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
     * A dummy item representing a piece of content.
     */
    class DeckItem(val deck: Deck) {

        override fun toString(): String {
            return deck.name
        }
    }
}
