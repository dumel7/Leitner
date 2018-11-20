package com.example.donski.leitner.recycleViewAdapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.example.donski.leitner.R

import com.example.donski.leitner.fragments.SetFragment.OnListFragmentInteractionListener
import com.example.donski.leitner.contents.SetContent.SetItem
import com.example.donski.leitner.fragments.SetFragment

/**
 * [RecyclerView.Adapter] that can display a [SetItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MySetRecyclerViewAdapter(private val mValues: List<SetItem>,
                               private val mListener: OnListFragmentInteractionListener?,
                               private val fragment: SetFragment) : RecyclerView.Adapter<MySetRecyclerViewAdapter.ViewHolder>(),
                                Filterable{

    private var mFilteredValues = mValues

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_set, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mFilteredValues[position]
        holder.mContentView.text = mFilteredValues[position].toString()

        holder.mView.setOnClickListener {
            mListener?.onListFragmentInteraction(holder.mItem!!)
        }
        holder.mView.setOnLongClickListener {
            fragment.changeSet(holder.mItem)
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
                    val filteredList = ArrayList<SetItem>()
                    mValues.filterTo(filteredList) { it.toString().contains(charSequence) }
                    mFilteredValues = filteredList
                }

                val filterResults = Filter.FilterResults()
                filterResults.values = mFilteredValues
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
                mFilteredValues = filterResults.values as ArrayList<SetItem>

                // refresh the list with filtered data
                notifyDataSetChanged()
            }
        }
    }

    class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mContentView: TextView
        var mItem: SetItem? = null

        init {
            mContentView = mView.findViewById<View>(R.id.content) as TextView
        }

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }

        companion object {
            private val SET_KEY = "SET"
        }
    }
}
