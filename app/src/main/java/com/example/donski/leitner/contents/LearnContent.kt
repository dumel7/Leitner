package com.example.donski.leitner.contents

import com.example.donski.leitner.database.entities.Flashcard
import java.util.*
import kotlin.collections.HashMap

object LearnContent {

    val ITEMS: MutableList<MutableList<Flashcard>> = ArrayList()
    val ITEMS_MAP: MutableMap<Int, Flashcard> = HashMap()
    val MAX = arrayListOf(2,3,4,5,6)

    var currentItem: Flashcard? = null

    enum class Side{
        TERM, DEFINITION
    }

    lateinit var side: Side

    fun fill(list: List<Flashcard>){
        ITEMS.clear()
        for (i in 0..5)
            ITEMS.add(ArrayList())
        list.map { item -> ITEMS[item.box].add(item) }
        ITEMS.forEach { my_list -> my_list.sortBy { item -> item.lastLearn } }
    }

    fun getBoxLength(boxId: Int): String{
        return if (boxId < ITEMS.size) ITEMS[boxId].size.toString() else "0"
    }

    private fun getBoxLengthInt(boxId: Int): Int{
        return if (boxId < ITEMS.size) ITEMS[boxId].size else 0
    }

    fun getNextItem(): Flashcard?{
        side = Side.TERM
        //checking the max
        for(i in 4 downTo 1){
            if(getBoxLengthInt(i) >= MAX[i]) {
                currentItem = ITEMS[i].minBy { item -> item.lastLearn }
                if(currentItem != null)
                    return currentItem
            }
        }
        //checking least
        for (i in 0..4) {
            currentItem = ITEMS[i].minBy { item -> item.lastLearn }
            if(currentItem != null)
                return currentItem
        }
        return null
    }

    fun updateCurrentItem(correct: Boolean){
        for(i in 0..ITEMS.size){
            if(ITEMS[i].contains(currentItem)) {
                ITEMS[i].remove(currentItem)
                break
            }
        }
        currentItem?.box = if(correct) currentItem!!.box + 1 else 1
        currentItem?.lastLearn = Date()
        ITEMS[currentItem!!.box].add(currentItem!!)
    }

    fun getCurrentItemText(): String {
        return when (side){
            Side.TERM -> {          side = Side.DEFINITION
                                    currentItem?.term?:""
            }
            Side.DEFINITION -> {    side = Side.TERM
                                    currentItem?.definition?:""
            }
        }
    }
}