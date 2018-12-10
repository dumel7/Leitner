package com.example.donski.leitner.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.donski.leitner.R

class LeitnerDescriptionFragment : Fragment() {

    private val mListener: OnLeitnerDescriptionFragmentInteractionListener? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_leitner_description, container, false)
        activity.title = getString(R.string.title_LeitnerDescription)
        return view
    }


    interface OnLeitnerDescriptionFragmentInteractionListener {
        fun onLeitnerDescriptionFragmentInteraction()
    }

    companion object {
        fun newInstance() = LeitnerDescriptionFragment()
    }
}
