package com.bignerdranch.android.happyplaceskotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bignerdranch.android.happyplaceskotlin.databinding.ActivityAddHappyPlaceBinding

class AddHappyPlaceActivity : AppCompatActivity() {
    private lateinit var ab: ActivityAddHappyPlaceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        ab = ActivityAddHappyPlaceBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(ab.root)

        setSupportActionBar(ab.toolbarAddPlace)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ab.toolbarAddPlace.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}