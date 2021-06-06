package com.bignerdranch.android.happyplaceskotlin.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.happyplaceskotlin.adapters.HappyPlacesAdapter
import com.bignerdranch.android.happyplaceskotlin.database.DatabaseHandler
import com.bignerdranch.android.happyplaceskotlin.databinding.ActivityMainBinding
import com.bignerdranch.android.happyplaceskotlin.models.HappyPlaceModel
import com.bignerdranch.android.happyplaceskotlin.utils.SwipeToDeleteCallback
import com.bignerdranch.android.happyplaceskotlin.utils.SwipeToEditCallback
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

        placesAdapter.setOnClickListener(object :
            HappyPlacesAdapter.OnClickListener {
            override fun onClick(position: Int, model: HappyPlaceModel) {
                val intent = Intent(this@MainActivity, AddHappyPlaceDetailsActivity::class.java)
                intent.putExtra(EXTRA_PLACE_DETAILS, model)
                startActivity(intent)
            }
        })
        val editSwipeHandler = object: SwipeToEditCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = mb.rvHappyPlacesList.adapter as HappyPlacesAdapter
                adapter.notifyEditItem(this@MainActivity, viewHolder.adapterPosition, ADD_PLACE_ACTIVITY)
            }
        }

        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(mb.rvHappyPlacesList)



        val deleteSwipeHandler = object: SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = mb.rvHappyPlacesList.adapter as HappyPlacesAdapter
                adapter.removeAt(viewHolder.adapterPosition)

                getHappyPlacesListFromLocalDatabase()
            }
        }

        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(mb.rvHappyPlacesList)
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

    companion object {
        var EXTRA_PLACE_DETAILS = "extra_place_details"
        var ADD_PLACE_ACTIVITY = 1
    }
}