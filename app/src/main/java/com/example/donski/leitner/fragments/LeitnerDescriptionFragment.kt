package com.example.donski.leitner.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.donski.leitner.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LeitnerDescriptionFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [LeitnerDescriptionFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
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
