package com.example.donski.leitner.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import android.widget.EditText
import android.widget.Toast
import com.example.donski.leitner.R
import com.example.donski.leitner.SwipeToDeleteCallback
import com.example.donski.leitner.contents.DeckContent.DeckItem
import com.example.donski.leitner.contents.SetContent
import com.example.donski.leitner.contents.SetContent.SetItem
import com.example.donski.leitner.database.LeitnerDatabase
import com.example.donski.leitner.database.entities.CSet
import com.example.donski.leitner.database.entities.Deck
import com.example.donski.leitner.database.entities.DeckToSet
import com.example.donski.leitner.recycleViewAdapter.MySetRecyclerViewAdapter

class SetFragment : Fragment() {
    private var mColumnCount = 1
    private var mListener: OnListFragmentInteractionListener? = null
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var mAdapter: MySetRecyclerViewAdapter
    private lateinit var searchView: SearchView
    private lateinit var deck: Deck
    private lateinit var db: LeitnerDatabase

    private val lastVisibleItemPosition: Int
        get() = linearLayoutManager.findLastVisibleItemPosition()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            mColumnCount = arguments.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        activity.setTitle(R.string.title_sets)
        val view = inflater!!.inflate(R.layout.fragment_set_list, container, false)

        // CSet the adapter
        if (view is RecyclerView) {
            db = LeitnerDatabase.getInstance(context)!!
            SetContent.clear()
            SetContent.fill(db.setDao().getAllSetsByDeck(deck.deckId!!))
            view.layoutManager = LinearLayoutManager(view.context)
            mAdapter = MySetRecyclerViewAdapter(SetContent.ITEMS, mListener, this)
            view.adapter = mAdapter
            linearLayoutManager = view.layoutManager as LinearLayoutManager
            setRecyclerViewScrollListener(view)
            val itemTouchHelper = ItemTouchHelper(SwipeHandler())
            itemTouchHelper.attachToRecyclerView(view)
        }
        setHasOptionsMenu(true)
        return view
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    private fun setRecyclerViewScrollListener(view: RecyclerView) {

        view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = recyclerView!!.layoutManager.itemCount
                if (totalItemCount == lastVisibleItemPosition + 1) {
                    //make more objects
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
        //search item
        val searchItem = menu?.findItem(R.id.action_search)
        searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // filter recycler view when query submitted
                mAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                // filter recycler view when text is changed
                mAdapter.filter.filter(query)
                return false
            }
        })

        //add item
        val addItem = menu?.findItem(R.id.action_add)
        addItem?.setOnMenuItemClickListener { addSet(addItem) }
    }

    private fun addSet(item: MenuItem): Boolean {

        val alert = AlertDialog.Builder(view!!.context)
        alert.setTitle("Add Set")
        val input = EditText(view!!.context)
        input.hint = "Title"
        alert.setView(input)
        alert.setPositiveButton("Add") { _, _ ->
            if (input.text.isEmpty()) {
                Toast.makeText(context, "You cannot add empty set", Toast.LENGTH_LONG).show()
            }else{
                val cSet = db.setDao().insert(CSet(null, input.text.toString()))
                db.deckToSetDao().insert(DeckToSet(null, deck.deckId!!, cSet.toInt()))
                refreshAdapter()
            }
            }

        alert.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        alert.show()


        return true
    }

    internal fun changeSet(item: SetContent.SetItem?): Boolean {

        val alert = AlertDialog.Builder(view!!.context)
        alert.setTitle("Change Set")
        val input = EditText(view!!.context)
        input.setText(item.toString())
        input.hint = "Title"

        alert.setView(input)
        alert.setPositiveButton("Change") { _, _ ->
            if (input.text.isEmpty()) {
                Toast.makeText(context, "You cannot change to empty set", Toast.LENGTH_LONG).show()
            }else {
                item!!.cSet.name = input.text.toString()
                db.setDao().update(item.cSet)
                refreshAdapter()
            }
        }
        alert.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        alert.show()

        return true
    }

    private fun refreshAdapter(){
        SetContent.clear()
        SetContent.fill(db.setDao().getAllSetsByDeck(deck.deckId!!))
        mAdapter.notifyDataSetChanged()
    }

    fun getCount(setItem: SetContent.SetItem): CharSequence? {
        return db.flashcardDao().getCountBySetId(setItem.cSet.setId!!).toString()
    }

    inner class SwipeHandler : SwipeToDeleteCallback(context) {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val viewHolder = viewHolder as MySetRecyclerViewAdapter.ViewHolder
            db.setDao().delete(viewHolder.mItem!!.cSet)
            refreshAdapter()

        }
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: SetItem)
    }

    companion object {

        private val ARG_COLUMN_COUNT = "column-count"

        fun newInstance(columnCount: Int, deckItem: DeckItem): SetFragment {
            val fragment = SetFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, columnCount)
            fragment.deck = deckItem.deck
            fragment.arguments = args
            return fragment
        }
    }
}
