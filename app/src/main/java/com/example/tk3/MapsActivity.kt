package com.example.tk3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tk3.databinding.ActivityMapsBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


/**
 * @author Iga Noviyanti created on 14/07/24 at 07.05
 */
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var placeName: String = ""
    private var placeLat: Double = 0.0
    private var placeLong: Double = 0.0
    private var destinationId: Int = 0

    private var binding: ActivityMapsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {

        setUI(googleMap)

        googleMap.setOnMapClickListener { marker ->
            placeLat = marker.latitude
            placeLong = marker.longitude

            val markerOptions = MarkerOptions()
            markerOptions.position(marker)
            markerOptions.title("$placeLat : $placeLong")
            googleMap.clear()
            googleMap.addMarker(markerOptions)
        }

        googleMap.setOnMarkerClickListener { marker ->
            placeName = marker.title.toString()
            placeLat = marker.position.latitude
            placeLong = marker.position.longitude
            false
        }

    }

    private fun setUI(map: GoogleMap){
        intent?.let {
            when(it.getStringExtra(MODE)){
                EDIT -> {
                    placeLat = it.getDoubleExtra(LAT, 0.0)
                    placeLong = it.getDoubleExtra(LONG, 0.0)
                    placeName = it.getStringExtra(TITLE).toString()
                    destinationId = it.getIntExtra(AddTask.DESTINATION_ID, 0)

                    binding?.btnSave?.text = "Edit Data"
                    map.addMarker(MarkerOptions().apply {
                        position(LatLng(placeLat, placeLong))
                        title(placeName)
                    })
                    binding?.btnSave?.setOnClickListener {
                        AddTask.open(context = this@MapsActivity,mode = EDIT, destinationId = destinationId, lat =  placeLat, long =  placeLong)
                    }
                }
                else ->{
                    binding?.btnSave?.setText("SAVE Data")
                    binding?.btnSave?.setOnClickListener {
                        AddTask.open(context = this@MapsActivity,mode = NEW_DATA, lat =  placeLat, long =  placeLong)
                    }
                }
            }
        }
    }

    companion object{

        const val MODE = "MODE"
        const val EDIT = "EDIT"
        const val NEW_DATA = "NEW_DATA"
        const val LAT = "LAT"
        const val LONG = "LONG"
        const val TITLE = "title"

        fun open(context: Context, mode: String,destinationId: Int? = null, lat: Double? = null, lng: Double? = null){
            val intent = Intent(context, MapsActivity::class.java)
            intent.putExtra(MODE, mode)
            intent.putExtra(LAT, lat)
            intent.putExtra(LONG, lng)
            intent.putExtra(AddTask.DESTINATION_ID, destinationId)
            context.startActivity(intent)
        }
    }
}