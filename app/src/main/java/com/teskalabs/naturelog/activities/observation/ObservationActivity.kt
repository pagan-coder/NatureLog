package com.teskalabs.naturelog.activities.observation

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.nshmura.recyclertablayout.RecyclerTabLayout
import com.teskalabs.naturelog.R
import com.teskalabs.naturelog.activities.observation.fragments.ObservationInfoFragment
import com.teskalabs.naturelog.activities.observation.fragments.ObservationPagerAdapter
import com.teskalabs.naturelog.activities.observation.fragments.OtherInfoFragment

import kotlinx.android.synthetic.main.activity_observation.*

class ObservationActivity : AppCompatActivity(),
        ObservationInfoFragment.OnFragmentInteractionListener,
        OtherInfoFragment.OnFragmentInteractionListener {

    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_observation)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        viewPager = findViewById(R.id.viewPager)
        viewPager.adapter = ObservationPagerAdapter(supportFragmentManager, resources)
        findViewById<RecyclerTabLayout>(R.id.recyclerTabLayout).setUpWithViewPager(viewPager)
    }

    override fun onFragmentInteraction(uri: Uri) {

    }
}
