package com.example.donski.leitner
import android.os.Bundle
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener
import android.support.v7.app.AppCompatActivity
import com.example.donski.leitner.contents.DeckContent.DeckItem
import com.example.donski.leitner.contents.FlashcardContent
import com.example.donski.leitner.fragments.DeckFragment
import com.example.donski.leitner.contents.SetContent.SetItem
import com.example.donski.leitner.database.LeitnerDatabase
import com.example.donski.leitner.fragments.FlashcardFragment
import kotlinx.android.synthetic.main.activity_main.*
import com.example.donski.leitner.fragments.SetFragment


class MainActivity : AppCompatActivity(), DeckFragment.OnListFragmentInteractionListener, SetFragment.OnListFragmentInteractionListener, FlashcardFragment.OnListFragmentInteractionListener {

    private var db:LeitnerDatabase? = null

    private val mOnNavigationItemSelectedListener = OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_decks -> {
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.mainFrame, DeckFragment.newInstance(1), getString(R.string.decks_tag))
                        .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_learn -> {
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.mainFrame, DeckFragment.newInstance(1), getString(R.string.learn_tag))
                        .commit()
                setTitle(R.string.title_learn)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                setTitle(R.string.title_settings)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = LeitnerDatabase.getInstance(this)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }


    override fun onListFragmentInteraction(item: DeckItem) {
        when(supportFragmentManager.findFragmentById(R.id.mainFrame).tag){
            getString(R.string.learn_tag) -> null
            getString(R.string.decks_tag) -> supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.mainFrame, SetFragment.newInstance(1, item))
                            .addToBackStack(null)
                            .commit()
        }
    }



    override fun onListFragmentInteraction(item: SetItem) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.mainFrame, FlashcardFragment.newInstance(1, item))
                .addToBackStack(null)
                .commit()
    }

    override fun onListFragmentInteraction(item: FlashcardContent.FlashcardItem) {

    }
}
