package com.example.donski.leitner.recycleViewAdapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.example.donski.leitner.R

import com.example.donski.leitner.fragments.DeckFragment.OnListFragmentInteractionListener
import com.example.donski.leitner.contents.DeckContent.DeckItem
import com.example.donski.leitner.fragments.DeckFragment

/**
 * [RecyclerView.Adapter] that can display a DeckItem and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyDeckRecyclerViewAdapter(private val mValues: List<DeckItem>, private val mListener: OnListFragmentInteractionListener?, private val fragment: DeckFragment)
    : RecyclerView.Adapter<MyDeckRecyclerViewAdapter.ViewHolder>(), Filterable {

    private var mFilteredValues = mValues

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_deck, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mFilteredValues[position]
        holder.mContentView.text = mFilteredValues[position].toString()

        holder.mView.setOnClickListener {
            mListener?.onListFragmentInteraction(holder.mItem!!)
        }
        holder.mView.setOnLongClickListener {
            fragment.changeDeck(holder.mItem)
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
                    val filteredList = ArrayList<DeckItem>()
                    mValues.filterTo(filteredList) { it.toString().contains(charSequence) }
                    mFilteredValues = filteredList
                }

                val filterResults = Filter.FilterResults()
                filterResults.values = mFilteredValues
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
                mFilteredValues = filterResults.values as ArrayList<DeckItem>

                // refresh the list with filtered data
                notifyDataSetChanged()
            }
        }
    }


    class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mContentView: TextView
        var mItem: DeckItem? = null

        init {
            mContentView = mView.findViewById<View>(R.id.content) as TextView

        }

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }


        companion object {
            private val DECK_KEY = "DECK"
        }
    }
}


