package com.example.donski.leitner.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.donski.leitner.BuildConfig
import com.example.donski.leitner.R
import com.example.donski.leitner.enums.SettingsEnum


class SettingsFragment : Fragment() {

    private var mListener: OnSettingsFragmentInteractionListener? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        activity.title = getString(R.string.title_settings)
        view.findViewById<TextView>(R.id.ver).text = getString(R.string.ver) + BuildConfig.VERSION_NAME
        view?.findViewById<TextView>(R.id.description)?.setOnClickListener {
            mListener?.onSettingsFragmentInteraction(SettingsEnum.Description)
        }
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnSettingsFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnListFragmentInteractionListener")
        }
    }


    interface OnSettingsFragmentInteractionListener {
        fun onSettingsFragmentInteraction(setting: SettingsEnum)
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}
