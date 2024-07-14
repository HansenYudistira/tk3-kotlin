package com.example.tk3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.tk3.MapsActivity.Companion.EDIT
import com.example.tk3.database.DatabaseHelper
import com.example.tk3.databinding.ActivityAddDestinationBinding
import com.example.tk3.model.DestinationListModel

class AddTask : AppCompatActivity() {

    var dbHandler: DatabaseHelper? = null
    var isEditMode: Boolean = false
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var destinationId: Int = 0

    private var mode = ""

    private lateinit var binding: ActivityAddDestinationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDestinationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getIntentExtra()

        dbHandler = DatabaseHelper(this)

        if (mode == EDIT) {
            // Update mode
            isEditMode = true
            with(binding) {
                btnSave.text = "Update Data"
                btnDel.visibility = View.VISIBLE
                val destinations: DestinationListModel =
                    dbHandler!!.getDestination(destinationId)
                etName.setText(destinations.name)
                etDescription.setText(destinations.description)
                if(latitude.isNullOrZero()) latitude = destinations.latitude
                if(longitude.isNullOrZero()) longitude = destinations.longitude
            }
        } else {
            // Add mode
            isEditMode = false
            with(binding) {
                btnSave.text = "Save Data"
                btnDel.visibility = View.GONE
            }
        }

        with(binding) {
            tvLatitude.text = "Latitude: $latitude"
            tvLongitude.text = "Longitude: $longitude"


            btnSave.setOnClickListener {
                var success: Boolean = false
                val destinations: DestinationListModel = DestinationListModel()
                if (isEditMode) {
                    // Update
                    destinations.id = destinationId
                    destinations.name = etName.text.toString()
                    destinations.description = etDescription.text.toString()
                    destinations.longitude = longitude
                    destinations.latitude = latitude

                    success = dbHandler?.updateDestination(destinations) as Boolean
                } else {
                    // Insert
                    destinations.name = etName.text.toString()
                    destinations.description = etDescription.text.toString()
                    destinations.longitude = longitude
                    destinations.latitude = latitude

                    success = dbHandler?.addDestination(destinations) as Boolean
                }
                if (success) {
                    finishAffinity()
                    MainActivity.open(this@AddTask)
                } else {
                    Toast.makeText(applicationContext, "Something went wrong!", Toast.LENGTH_LONG)
                        .show()
                }
            }

            btnDel.setOnClickListener {
                val dialog = AlertDialog.Builder(this@AddTask)
                    .setTitle("Info")
                    .setMessage("Click yes if you want to delete")
                    .setPositiveButton("YES") { dialog, i ->
                        val success =
                            dbHandler?.deleteDestination(intent.getIntExtra("Id", 0)) as Boolean
                        if (success) {
                            finishAffinity()
                            MainActivity.open(this@AddTask)
                        }
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, i ->
                        dialog.dismiss()
                    }
                dialog.show()
            }
        }
    }

    private fun getIntentExtra() {
        intent?.let {
            mode = it.getStringExtra(MODE).toString()
            latitude = it.getDoubleExtra(LAT_LOCATION, 0.0)
            longitude = it.getDoubleExtra(LONG_LOCATION, 0.0)
            destinationId = it.getIntExtra(DESTINATION_ID, 0)
        }
    }

    private fun Double.isNullOrZero() = this == null || this == 0.0

    companion object {
        const val MODE = "MODE"
        const val EDIT = "EDIT"
        const val NEW_DATA = "NEW_DATA"
        const val DESTINATION_ID = "destination_id"
        const val LAT_LOCATION = "lat_location"
        const val LONG_LOCATION = "long_location"

        fun open(
            context: Context,
            mode: String,
            destinationId: Int? = null,
            lat: Double,
            long: Double
        ) {
            val intent = Intent(context, AddTask::class.java)
            intent.putExtra(LAT_LOCATION, lat)
            intent.putExtra(LONG_LOCATION, long)
            intent.putExtra(DESTINATION_ID, destinationId)
            intent.putExtra(MODE, mode)
            context.startActivity(intent)
        }
    }

}
