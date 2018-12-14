package com.example.donski.leitner
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener
import android.support.v7.app.AppCompatActivity
import com.example.donski.leitner.contents.DeckContent.DeckItem
import com.example.donski.leitner.contents.FlashcardContent
import com.example.donski.leitner.contents.SetContent.SetItem
import com.example.donski.leitner.database.LeitnerDatabase
import com.example.donski.leitner.enums.SettingsEnum
import com.example.donski.leitner.fragments.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), DeckFragment.OnListFragmentInteractionListener, SetFragment.OnListFragmentInteractionListener, FlashcardFragment.OnListFragmentInteractionListener, SettingsFragment.OnSettingsFragmentInteractionListener {

    private var db:LeitnerDatabase? = null
    private val BackStackLimit = 6

    private val mOnNavigationItemSelectedListener = OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_decks -> {
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.mainFrame, DeckFragment.newInstance(1), getString(R.string.decks_tag))
                        .addToBackStack(null)
                        .commit()
                //checkBackStack()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_learn -> {
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.mainFrame, DeckFragment.newInstance(1), getString(R.string.learn_tag))
                        .addToBackStack(null)
                        .commit()
                //checkBackStack()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.mainFrame, SettingsFragment.newInstance())
                        .addToBackStack(null)
                        .commit()
                //checkBackStack()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    private fun checkBackStack(){
        if(supportFragmentManager.backStackEntryCount > BackStackLimit)
            supportFragmentManager.popBackStack(0, Intent.FLAG_ACTIVITY_CLEAR_TOP)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = LeitnerDatabase.getInstance(this)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.mainFrame, DeckFragment.newInstance(1), getString(R.string.decks_tag))
                .addToBackStack(null)
                .commit()
        //checkBackStack()
    }


    override fun onListFragmentInteraction(item: DeckItem) {
        when(supportFragmentManager.findFragmentById(R.id.mainFrame).tag){
            getString(R.string.learn_tag) -> startSwapActivity(item)
            getString(R.string.decks_tag) -> {supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.mainFrame, SetFragment.newInstance(1, item))
                            .addToBackStack(null)
                            .commit()
            }
        }
    }

    private fun startSwapActivity(item: DeckItem){
        val intent = Intent(this, SwapActivity::class.java).apply {
            putExtra("deckId", item.deck.deckId)
        }
        startActivity(intent)
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

    override fun onSettingsFragmentInteraction(setting: SettingsEnum) {
        when(setting){
            SettingsEnum.Description ->{supportFragmentManager
                                            .beginTransaction()
                                            .replace(R.id.mainFrame, LeitnerDescriptionFragment.newInstance())
                                            .addToBackStack(null)
                                            .commit()
            }
        }
    }
}
