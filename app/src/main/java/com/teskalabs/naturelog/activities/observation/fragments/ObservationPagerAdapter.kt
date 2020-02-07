package com.teskalabs.naturelog.activities.observation.fragments

import android.content.res.Resources
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.teskalabs.naturelog.R

class ObservationPagerAdapter(fragmentManager: FragmentManager, resources: Resources) :
        FragmentStatePagerAdapter(fragmentManager) {

    private val listOfFragments: ArrayList<Fragment> = arrayListOf(
            ObservationInfoFragment.newInstance("", ""),
            OtherInfoFragment.newInstance("", "")
    )
    private val listOfTitles: ArrayList<String> = arrayListOf(
            resources.getString(R.string.observation_info_fragment_title),
            resources.getString(R.string.other_info_fragment_title)
    )

    override fun getItem(position: Int): Fragment {
        return listOfFragments[position]
    }

    override fun getCount(): Int {
        return listOfFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return listOfTitles[position]
    }
}
