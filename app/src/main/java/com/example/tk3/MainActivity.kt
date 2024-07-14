package com.example.tk3

import android.content.Context
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
import com.example.tk3.databinding.ActivityMainBinding
import com.example.tk3.model.DestinationListModel
import com.example.tk3.ui.theme.Tk3Theme

class MainActivity : AppCompatActivity() {

    var destinationlistAdapter: DestinationListAdapter ?= null
    var dbHandler: DatabaseHelper ?= null
    var destinationlist: List<DestinationListModel> = ArrayList<DestinationListModel>()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHandler = DatabaseHelper(this)
        fetchlist()

        binding.btAddItems.setOnClickListener {
            MapsActivity.open(context = this@MainActivity, mode = MapsActivity.NEW_DATA)
        }

    }

    private fun fetchlist() {
        destinationlist = dbHandler!!.getAllDestination()
        destinationlistAdapter = DestinationListAdapter(destinationlist, this@MainActivity)
        binding.rvList.adapter = destinationlistAdapter
        destinationlistAdapter?.notifyDataSetChanged()
    }

    companion object{

        fun open(context: Context){
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}