package com.bignerdranch.android.happyplaceskotlin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bignerdranch.android.happyplaceskotlin.R
import com.bignerdranch.android.happyplaceskotlin.databinding.ActivityMapBinding
import com.bignerdranch.android.happyplaceskotlin.models.HappyPlaceModel

class MapActivity : AppCompatActivity() {
    private lateinit var mapBinding: ActivityMapBinding
    private var mHappyPlaceDetail: HappyPlaceModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        mapBinding = ActivityMapBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(mapBinding.root)

        if(intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)) {
            mHappyPlaceDetail =
                intent.getSerializableExtra(MainActivity.EXTRA_PLACE_DETAILS)
                        as HappyPlaceModel
        }
        if(mHappyPlaceDetail != null) {
            setSupportActionBar(mapBinding.toolbarMap)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = mHappyPlaceDetail!!.title

            mapBinding.toolbarMap.setNavigationOnClickListener {
                onBackPressed()
            }


        }

    }
}