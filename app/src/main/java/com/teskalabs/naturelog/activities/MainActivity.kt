package com.teskalabs.naturelog.activities

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.teskalabs.naturelog.R
import com.teskalabs.naturelog.activities.observation.ObservationActivity
import com.teskalabs.naturelog.model.GPSInterface
import com.teskalabs.naturelog.model.GPSPosition
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.osmdroid.mapsforge.MapsForgeTileSource
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver
import org.osmdroid.mapsforge.MapsForgeTileProvider
import org.mapsforge.map.android.rendertheme.AssetsRenderTheme
import org.mapsforge.map.rendertheme.XmlRenderTheme
import org.osmdroid.views.overlay.ScaleBarOverlay
import java.io.File

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, GPSInterface {
    companion object {
        val OBSERVATION_ACTIVITY = 100
    }

    lateinit var map : MapView

    private val gpsPosition: GPSPosition = GPSPosition(this, this)

    // Storage: https://grokonez.com/kotlin/kotlin-convert-kotlin-object-xml-file-xml-string-jackson
    // Network: https://codelabs.developers.google.com/codelabs/kotlin-coroutines/
    // Network: https://stackoverflow.com/questions/46177133/http-request-in-kotlin, https://developer.android.com/training/volley/index.html
    // Marker: https://github.com/osmdroid/osmdroid/wiki/Markers,-Lines-and-Polygons

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == OBSERVATION_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {

            }
            val menuItem = this.nav_view.menu.findItem(R.id.nav_manage)
            menuItem.setChecked(false)
        }
    }

    // TODO: Request permissions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        MapsForgeTileSource.createInstance(this.application)

        var theme: XmlRenderTheme ?= null //null is ok here, uses the default rendering theme if it's not set
        try {
            //this file should be picked up by the mapsforge dependencies
            theme = AssetsRenderTheme(applicationContext, "renderthemes/", "rendertheme-v4.xml")
            //alternative: theme = new ExternalRenderTheme(userDefinedRenderingFile);
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        val maps: ArrayList<File> = arrayListOf()
        File(Environment.getExternalStorageDirectory().absolutePath).walkTopDown().forEach {
            if (it.extension == "map") {
                maps.add(it)
            }
        }

        map = findViewById<MapView>(R.id.map)
        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        map.setMultiTouchControls(true)

        val scaleBar = ScaleBarOverlay(map)
        scaleBar.setAlignBottom(true)
        map.overlays.add(scaleBar)

        if (maps.size > 0) {
            val fromFiles = MapsForgeTileSource.createFromFiles(maps.toArray(arrayOf<File>()), theme, "rendertheme-v4")
            val forge = MapsForgeTileProvider(SimpleRegisterReceiver(this.applicationContext), fromFiles, null)
            map.tileProvider = forge
            map.post({
                map.zoomToBoundingBox(fromFiles.boundsOsmdroid, false)
                map.controller.zoomTo(14.0)
            })
        }
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onStart() {
        super.onStart()
        gpsPosition.startLocationUpdates()
    }

    override fun onStop() {
        super.onStop()
        gpsPosition.stoplocationUpdates()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {
                val intent = Intent(this, ObservationActivity::class.java)
                startActivityForResult(intent, OBSERVATION_ACTIVITY)
            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onLocationChanged(location: Location) {
        val output = location.latitude.toString() + ", " + location.longitude.toString()
        Log.w("GPSPosition", output)
    }
}
