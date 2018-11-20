package com.example.donski.leitner.recycleViewAdapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.example.donski.leitner.R

import com.example.donski.leitner.fragments.FlashcardFragment.OnListFragmentInteractionListener
import com.example.donski.leitner.contents.FlashcardContent.FlashcardItem
import com.example.donski.leitner.fragments.FlashcardFragment


/**
 * [RecyclerView.Adapter] that can display a Flashcard
 *Item and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyFlashcardRecyclerViewAdapter(private val mValues: List<FlashcardItem>,
                                     private val mListener: OnListFragmentInteractionListener?,
                                     private val fragment: FlashcardFragment)
    : RecyclerView.Adapter<MyFlashcardRecyclerViewAdapter.ViewHolder>(), Filterable {

    private var mFilteredValues = mValues

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_flashcard, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mFilteredValues[position]
        holder.mTermView.text = mFilteredValues[position].flashcard.term
        holder.mDefinitionView.text = mFilteredValues[position].flashcard.definition


        holder.mView.setOnClickListener {
            mListener?.onListFragmentInteraction(holder.mItem!!)
        }
        holder.mView.setOnLongClickListener {
            fragment.changeFlashcard(holder.mItem)
        }
    }

    override fun getItemCount(): Int {
        return mFilteredValues.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    mFilteredValues = mValues
                } else {
                    val filteredList = ArrayList<FlashcardItem>()
                    mValues.filterTo(filteredList) { it.toString().contains(charSequence) }
                    mFilteredValues = filteredList
                }

                val filterResults = Filter.FilterResults()
                filterResults.values = mFilteredValues
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
                mFilteredValues = filterResults.values as ArrayList<FlashcardItem>

                // refresh the list with filtered data
                notifyDataSetChanged()
            }
        }
    }

    class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mTermView: TextView
        val mDefinitionView: TextView
        var mItem: FlashcardItem? = null

        init {
            mTermView = mView.findViewById<View>(R.id.term) as TextView
            mDefinitionView = mView.findViewById<View>(R.id.definition) as TextView
        }

        companion object {
            private val Flashcard_KEY = "Flashcard"
        }
    }
}


