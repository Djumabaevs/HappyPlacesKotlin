package com.bignerdranch.android.happyplaceskotlin.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bignerdranch.android.happyplaceskotlin.database.DatabaseHandler
import com.bignerdranch.android.happyplaceskotlin.databinding.ActivityMainBinding
import com.bignerdranch.android.happyplaceskotlin.models.HappyPlaceModel

class MainActivity : AppCompatActivity() {
    private lateinit var mb: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        mb = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(mb.root)

        mb.fabAddHappyPlace.setOnClickListener {
            val intent = Intent(this, AddHappyPlaceActivity::class.java)
            startActivity(intent)
        }
        getHappyPlacesListFromLocalDatabase()
    }

    private fun getHappyPlacesListFromLocalDatabase() {
        val dbHandler = DatabaseHandler(this)
        val getHappyPlaceList: ArrayList<HappyPlaceModel> = dbHandler.getHappyPlacesList()

        if(getHappyPlaceList.size > 0) {
            for(i in getHappyPlaceList) {
                Log.e("Title", i.title)
                Log.e("Description", i.description)
            }
        }

    }
}