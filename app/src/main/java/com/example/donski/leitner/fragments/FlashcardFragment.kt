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
import com.example.donski.leitner.R
import com.example.donski.leitner.contents.FlashcardContent
import com.example.donski.leitner.contents.SetContent
import com.example.donski.leitner.database.LeitnerDatabase
import com.example.donski.leitner.database.entities.CSet
import com.example.donski.leitner.database.entities.Flashcard
import com.example.donski.leitner.recycleViewAdapter.MyFlashcardRecyclerViewAdapter
import android.widget.LinearLayout
import android.widget.Toast
import com.example.donski.leitner.SwipeToDeleteCallback
import com.example.donski.leitner.recycleViewAdapter.MyDeckRecyclerViewAdapter
import java.time.LocalDateTime
import java.util.*

class FlashcardFragment : Fragment() {
    private var mColumnCount = 1
    private var mListener: OnListFragmentInteractionListener? = null
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var mAdapter: MyFlashcardRecyclerViewAdapter
    private lateinit var searchView: SearchView
    private lateinit var db: LeitnerDatabase
    private lateinit var cSet: CSet

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
        activity.setTitle(R.string.title_flashcard)
        val view = inflater!!.inflate(R.layout.fragment_flashcard_list, container, false)
        if (view is RecyclerView) {
            db = LeitnerDatabase.getInstance(context)!!
            view.layoutManager = LinearLayoutManager(view.context)
            FlashcardContent.clear()
            FlashcardContent.fill(db.flashcardDao().getAllBySetId(cSet.setId!!))
            mAdapter = MyFlashcardRecyclerViewAdapter(FlashcardContent.ITEMS, mListener, this)
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
        val addItem = menu.findItem(R.id.action_add)
        addItem.setOnMenuItemClickListener { addFlashcard(addItem) }

    }

    private fun addFlashcard(item: MenuItem): Boolean {

        val alert = AlertDialog.Builder(view!!.context)
        alert.setTitle("Add Flashcard")

        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL

        val termBox = EditText(context)
        termBox.hint = "Term"
        layout.addView(termBox)

        val definitionBox = EditText(context)
        definitionBox.hint = "Description"
        layout.addView(definitionBox)

        alert.setView(layout)
        alert.setPositiveButton("Add") { _, _ ->
            if (termBox.text.isEmpty() or definitionBox.text.isEmpty()) {
                Toast.makeText(context, "You cannot add empty flashcard", Toast.LENGTH_LONG).show()
            } else {
                db.flashcardDao().insert(Flashcard(null, termBox.text.toString(), definitionBox.text.toString(), cSet.setId!!, 0, Date()))
                refreshAdapter()
            }
        }
        alert.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        alert.show()


        return true
    }

    internal fun changeFlashcard(item: FlashcardContent.FlashcardItem?): Boolean {

        val alert = AlertDialog.Builder(view!!.context)
        alert.setTitle("Add Flashcard")

        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL

        val termBox = EditText(context)
        termBox.setText(item?.flashcard?.term.toString())
        termBox.hint = "Term"
        layout.addView(termBox)

        val definitionBox = EditText(context)
        definitionBox.setText(item?.flashcard?.definition.toString())
        definitionBox.hint = "Description"
        layout.addView(definitionBox)

        alert.setView(layout)
        alert.setPositiveButton("Change") { _, _ ->
            if (termBox.text.isEmpty() or definitionBox.text.isEmpty()) {
                Toast.makeText(context, "You cannot change to empty flashcard", Toast.LENGTH_LONG).show()
            }else {
                item!!.flashcard.term = termBox.text.toString()
                item!!.flashcard.definition = definitionBox.text.toString()
                db.flashcardDao().update(item.flashcard)
                refreshAdapter()
            }
        }
        alert.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        alert.show()

        return true
    }

    private fun refreshAdapter(){
        FlashcardContent.clear()
        FlashcardContent.fill(db.flashcardDao().getAllBySetId(cSet.setId!!))
        mAdapter.notifyDataSetChanged()
    }

    inner class SwipeHandler : SwipeToDeleteCallback(context) {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val viewHolder = viewHolder as MyFlashcardRecyclerViewAdapter.ViewHolder
            db.flashcardDao().delete(viewHolder.mItem!!.flashcard)
            refreshAdapter()

        }
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: FlashcardContent.FlashcardItem)
    }

    companion object {
        private val ARG_COLUMN_COUNT = "column-count"
        fun newInstance(columnCount: Int, setItem: SetContent.SetItem): FlashcardFragment {
            val fragment = FlashcardFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, columnCount)
            fragment.cSet = setItem.cSet
            fragment.arguments = args
            return fragment
        }
    }
}
