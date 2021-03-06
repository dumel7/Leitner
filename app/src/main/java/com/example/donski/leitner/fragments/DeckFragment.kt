package com.example.donski.leitner.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.example.donski.leitner.R
import android.support.v7.widget.SearchView
import com.example.donski.leitner.contents.DeckContent
import com.example.donski.leitner.database.LeitnerDatabase
import com.example.donski.leitner.recycleViewAdapter.MyDeckRecyclerViewAdapter
import android.support.v7.app.AlertDialog
import android.support.v7.widget.helper.ItemTouchHelper
import android.widget.EditText
import android.widget.Toast
import com.example.donski.leitner.SwipeToDeleteCallback
import com.example.donski.leitner.database.entities.CSet
import com.example.donski.leitner.database.entities.Deck
import java.util.*

class DeckFragment : Fragment() {
    private var mColumnCount = 1
    private var mListener: OnListFragmentInteractionListener? = null
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var mAdapter: MyDeckRecyclerViewAdapter
    private lateinit var searchView: SearchView
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
        activity.title = tag
        val view = inflater!!.inflate(R.layout.fragment_deck_list, container, false)
        if (view is RecyclerView) {
            db = LeitnerDatabase.getInstance(context)!!
            view.layoutManager = LinearLayoutManager(view.context)
            DeckContent.clear()
            DeckContent.fill(db.deckDao().getAll())
            mAdapter = MyDeckRecyclerViewAdapter(DeckContent.ITEMS, mListener, this)
            view.adapter = mAdapter
            linearLayoutManager = view.layoutManager as LinearLayoutManager
            setRecyclerViewScrollListener(view)
            if(tag == view?.resources.getString(R.string.decks_tag)) {
                val itemTouchHelper = ItemTouchHelper(SwipeHandler())
                itemTouchHelper.attachToRecyclerView(view)
            }
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
        val addItem = menu.findItem(R.id.action_add)
        when(this.tag){
            getString(R.string.decks_tag) -> {
                addItem.isVisible = true
                addItem.isEnabled = true
                addItem.setOnMenuItemClickListener { addDeck(addItem) }
            }
            else -> {
                addItem.isVisible = false
                addItem.isEnabled = false}
        }
    }

    private fun addDeck(item: MenuItem): Boolean {

        val alert = AlertDialog.Builder(view!!.context)
        alert.setTitle("Add Deck")
        val input = EditText(view!!.context)
        input.hint = "Title"

        alert.setView(input)
        alert.setPositiveButton("Add") { _, _ ->
            if (input.text.isEmpty()) {
                Toast.makeText(context, "You cannot add empty deck", Toast.LENGTH_LONG).show()
            }else{
                db.deckDao().insert(Deck(null, input.text.toString(), Date(0)))
                refreshAdapter()
        }
        }
        alert.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        alert.show()

        return true
    }

    internal fun changeDeck(item: DeckContent.DeckItem?): Boolean {

        val alert = AlertDialog.Builder(view!!.context)
        alert.setTitle("Change Deck")
        val input = EditText(view!!.context)
        input.setText(item.toString())
        input.hint = "Title"

        alert.setView(input)
        alert.setPositiveButton("Change") { _, _ ->
            if (input.text.isEmpty()) {
                Toast.makeText(context, "You cannot change to empty deck", Toast.LENGTH_LONG).show()
            }else{
                item!!.deck.name = input.text.toString()
                db.deckDao().update(item.deck)
                refreshAdapter()
            }
        }
        alert.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        alert.show()

        return true
    }

    private fun refreshAdapter(){
        DeckContent.clear()
        DeckContent.fill(db.deckDao().getAll())
        mAdapter.notifyDataSetChanged()
    }

    fun resetDeck(deckItem: DeckContent.DeckItem?): Boolean {
        val alertDialog = AlertDialog.Builder(view!!.context)
        alertDialog.setTitle("Reset Deck")
        alertDialog.setPositiveButton("Reset"){ _, _ ->
            db.flashcardDao().resetBoxes(deckItem!!.deck.deckId!!)
        }
        alertDialog.setNegativeButton("Cancel"){ dialog, _ -> dialog.dismiss() }
        alertDialog.show()

        return true
    }

    fun getCount(deckItem: DeckContent.DeckItem): CharSequence? {
        return db.flashcardDao().getCountByDeckId(deckItem.deck.deckId!!).toString()
    }

    inner class SwipeHandler : SwipeToDeleteCallback(context) {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val viewHolder = viewHolder as MyDeckRecyclerViewAdapter.ViewHolder
            db.setDao().getAllSetsByDeck(viewHolder.mItem!!.deck.deckId!!).map { cSet: CSet -> db.setDao().delete(cSet) }
            db.deckDao().delete(viewHolder.mItem!!.deck)
            refreshAdapter()
        }
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: DeckContent.DeckItem)
    }

    companion object {
        private val ARG_COLUMN_COUNT = "column-count"
        fun newInstance(columnCount: Int): DeckFragment {
            val fragment = DeckFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, columnCount)
            fragment.arguments = args
            return fragment
        }
    }
}
