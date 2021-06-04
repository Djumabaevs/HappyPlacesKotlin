package com.bignerdranch.android.happyplaceskotlin.activities

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.happyplaceskotlin.adapters.HappyPlacesAdapter
import com.bignerdranch.android.happyplaceskotlin.database.DatabaseHandler
import com.bignerdranch.android.happyplaceskotlin.databinding.ActivityMainBinding
import com.bignerdranch.android.happyplaceskotlin.models.HappyPlaceModel
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
    private lateinit var mb: ActivityMainBinding
/*    private val requestPermission  =registerForActivityResult(ActivityResultContracts.RequestPermission()) {granted ->
        viewModel.onPermissionsResult(granted)
    }*/
   /* private val viewModel: MainViewModel by viewModels()*/

    override fun onCreate(savedInstanceState: Bundle?) {
        mb = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(mb.root)

      /*  viewModel.getStatusText().observe(this) {
            mb.fabAddPermission.tooltipText = it
        }*/

       /* mb.fabAddPermission2.setOnClickListener {
            requestPermission.launch(Manifest.permission.CAMERA)
        }*/

        mb.fabAddHappyPlace.setOnClickListener {
            val intent = Intent(this, AddHappyPlaceActivity::class.java)
            startActivity(intent)

        }

        getHappyPlacesListFromLocalDatabase()
    }

    private fun setupHappyPlacesRecyclerView(happyPlacesList: ArrayList<HappyPlaceModel>) {
        mb.rvHappyPlacesList.layoutManager = LinearLayoutManager(this)
        mb.rvHappyPlacesList.setHasFixedSize(true)

        val placesAdapter = HappyPlacesAdapter(this, happyPlacesList)
        mb.rvHappyPlacesList.adapter = placesAdapter
    }

    private fun getHappyPlacesListFromLocalDatabase() {
        val dbHandler = DatabaseHandler(this)
        val getHappyPlaceList = dbHandler.getHappyPlacesList()

        if(getHappyPlaceList.size > 0) {
            mb.rvHappyPlacesList.visibility = View.VISIBLE
            mb.noRecordsAvailable.visibility = View.GONE
            setupHappyPlacesRecyclerView(getHappyPlaceList)
        } else {
            mb.rvHappyPlacesList.visibility = View.GONE
            mb.noRecordsAvailable.visibility = View.VISIBLE
        }

    }
}