package com.example.donski.leitner.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import com.example.donski.leitner.R
import com.example.donski.leitner.contents.LearnContent
import com.example.donski.leitner.database.LeitnerDatabase
import kotlinx.android.synthetic.main.swap_fragment.view.*

class SwapFragment : Fragment() {

    private var deckId = 0
    private lateinit var db: LeitnerDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.swap_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        activity.title = "Learn"
        db = LeitnerDatabase.getInstance(context)!!
        LearnContent.fill(db.flashcardDao().getAllByDeckId(deckId))
        setListeners()
        fillBoxesNumber()
        nextFlashcard()
    }

    private fun setListeners(){
        view!!.findViewById<ImageButton>(R.id.swap_button).setOnClickListener { swapListener() }
        view!!.findViewById<ImageButton>(R.id.left_button).setOnClickListener { nextFlashcard(false) }
        view!!.findViewById<ImageButton>(R.id.right_button).setOnClickListener { nextFlashcard(true) }
    }

    private fun nextFlashcard(correct :Boolean): Boolean{
        LearnContent.updateCurrentItem(correct)
        fillBoxesNumber()
        nextFlashcard()
        return true
    }

    private fun swapListener(): Boolean{
        view!!.flashcard_message.text = LearnContent.getCurrentItemText()
        return true
    }

    private fun fillBoxesNumber(){
        view!!.first_box.text = LearnContent.getBoxLength(1)
        view!!.second_box.text = LearnContent.getBoxLength(2)
        view!!.third_box.text = LearnContent.getBoxLength(3)
        view!!.forth_box.text = LearnContent.getBoxLength(4)
        view!!.fifth_box.text = LearnContent.getBoxLength(5)
    }

    private fun nextFlashcard(){
        if(LearnContent.getNextItem() == null)
                finishLearn()
        view!!.flashcard_message.text = LearnContent.getCurrentItemText()
    }

    private fun finishLearn(){
        Toast.makeText(context, "You've finished Learning", Toast.LENGTH_LONG).show()
        activity.finish()
    }

    companion object {
        fun newInstance(deckId :Int): SwapFragment{
            val fragment = SwapFragment()
            fragment.deckId = deckId
            return fragment
        }
    }

    override fun onDestroy() {
        LearnContent.ITEMS.flatten().forEach { item ->  db.flashcardDao().update(item)}
        super.onDestroy()
    }
}
