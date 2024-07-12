package com.example.tk3

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tk3.database.DatabaseHelper
import com.example.tk3.model.DestinationListModel
import com.google.android.material.slider.Slider

class AddTask : AppCompatActivity() {
    lateinit var btn_save: Button
    lateinit var btn_del: Button
    lateinit var et_name: EditText
    lateinit var et_description: EditText
    lateinit var tv_latitude: TextView
    lateinit var tv_longitude: TextView
    lateinit var slider_latitude: Slider
    lateinit var slider_longitude: Slider
    var dbHandler: DatabaseHelper? = null
    var isEditMode: Boolean = false
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_destination)

        btn_save = findViewById(R.id.btn_save)
        btn_del = findViewById(R.id.btn_del)
        et_name = findViewById(R.id.et_name)
        et_description = findViewById(R.id.et_description)
        tv_latitude = findViewById(R.id.tv_latitude)
        tv_longitude = findViewById(R.id.tv_longitude)
        slider_latitude = findViewById(R.id.slider_latitude)
        slider_longitude = findViewById(R.id.slider_longitude)

        dbHandler = DatabaseHelper(this)

        if (intent != null && intent.getStringExtra("Mode") == "E") {
            // Update mode
            isEditMode = true
            btn_save.text = "Update Data"
            btn_del.visibility = View.VISIBLE
            val destinations: DestinationListModel = dbHandler!!.getDestination(intent.getIntExtra("Id", 0))
            et_name.setText(destinations.name)
            et_description.setText(destinations.description)
            latitude = destinations.latitude
            longitude = destinations.longitude
            slider_latitude.value = (latitude + 90).toFloat()
            slider_longitude.value = (longitude + 180).toFloat()
        } else {
            // Add mode
            isEditMode = false
            btn_save.text = "Save Data"
            btn_del.visibility = View.GONE
        }

        tv_latitude.text = "Latitude: $latitude"
        tv_longitude.text = "Longitude: $longitude"

        slider_latitude.addOnChangeListener { slider, value, fromUser ->
            latitude = value.toDouble() - 90.0 // adjust the range
            tv_latitude.text = "Latitude: $latitude"
        }

        slider_longitude.addOnChangeListener { slider, value, fromUser ->
            longitude = value.toDouble() - 180.0 // adjust the range
            tv_longitude.text = "Longitude: $longitude"
        }

        btn_save.setOnClickListener {
            var success: Boolean = false
            val destinations: DestinationListModel = DestinationListModel()
            if (isEditMode) {
                // Update
                destinations.id = intent.getIntExtra("Id", 0)
                destinations.name = et_name.text.toString()
                destinations.description = et_description.text.toString()
                destinations.longitude = longitude
                destinations.latitude = latitude

                success = dbHandler?.updateDestination(destinations) as Boolean
            } else {
                // Insert
                destinations.name = et_name.text.toString()
                destinations.description = et_description.text.toString()
                destinations.longitude = longitude
                destinations.latitude = latitude

                success = dbHandler?.addDestination(destinations) as Boolean
            }
            if (success) {
                val i = Intent(applicationContext, MainActivity::class.java)
                startActivity(i)
                finish()
            } else {
                Toast.makeText(applicationContext, "Something went wrong!", Toast.LENGTH_LONG).show()
            }
        }

        btn_del.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
                .setTitle("Info")
                .setMessage("Click yes if you want to delete")
                .setPositiveButton("YES") { dialog, i ->
                    val success = dbHandler?.deleteDestination(intent.getIntExtra("Id", 0)) as Boolean
                    if (success) {
                        finish()
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
