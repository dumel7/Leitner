package com.example.donski.leitner

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.donski.leitner.fragments.SwapFragment

class SwapActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val deckId = intent.getIntExtra("deckId", 0)
        setContentView(R.layout.swap_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SwapFragment.newInstance(deckId))
                    .commitNow()
        }
    }

}
