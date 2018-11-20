package com.example.donski.leitner.contents

import com.example.donski.leitner.database.entities.CSet
import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object SetContent {

    /**
     * An array of sample (dummy) items.
     */
    val ITEMS: MutableList<SetItem> = ArrayList()

    /**
     * A map of sample (dummy) items, by ID.
     */
    val ITEM_MAP: MutableMap<Int, SetItem> = HashMap()


    fun clear(){
        ITEMS.clear()
        ITEM_MAP.clear()
    }

    fun fill(sets :List<CSet>){
        sets.map { cSet -> addItem(SetContent.SetItem(cSet))   }
    }

    private fun addItem(item: SetItem) {
        ITEMS.add(item)
        ITEM_MAP.put(item.cSet.setId!!, item)
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
    class SetItem(val cSet: CSet) {

        override fun toString(): String {
            return cSet.name
        }
    }
}
