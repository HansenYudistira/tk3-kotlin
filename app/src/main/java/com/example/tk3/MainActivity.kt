package com.example.tk3

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.provider.ContactsContract.Intents
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tk3.adapter.DestinationListAdapter
import com.example.tk3.database.DatabaseHelper
import com.example.tk3.model.DestinationListModel
import com.example.tk3.ui.theme.Tk3Theme

class MainActivity : AppCompatActivity() {
    lateinit var recycler_destination: RecyclerView
    lateinit var btn_add: Button
    var destinationlistAdapter: DestinationListAdapter ?= null
    var dbHandler: DatabaseHelper ?= null
    var destinationlist: List<DestinationListModel> = ArrayList<DestinationListModel>()
    var linearlayoutManager: LinearLayoutManager ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        print("sampai sini 1")
        recycler_destination = findViewById(R.id.rv_list)
        btn_add = findViewById(R.id.bt_add_items)

        print("sampai sini 2")
        dbHandler = DatabaseHelper(this)
        fetchlist()
        print("sampai sini 3")
        btn_add.setOnClickListener {
            val i = Intent(applicationContext, AddTask::class.java)
            startActivity(i)
        }

        print("sampai sini 4")
    }

    private fun fetchlist() {
        destinationlist = dbHandler!!.getAllDestination()
        destinationlistAdapter = DestinationListAdapter(destinationlist, applicationContext)
        linearlayoutManager = LinearLayoutManager(applicationContext)
        recycler_destination.layoutManager = linearlayoutManager
        recycler_destination.adapter = destinationlistAdapter
        destinationlistAdapter?.notifyDataSetChanged()
    }
}