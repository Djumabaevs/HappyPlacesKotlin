package com.bignerdranch.android.happyplaceskotlin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.bignerdranch.android.happyplaceskotlin.R
import com.bignerdranch.android.happyplaceskotlin.databinding.ActivityAddHappyPlaceDetailsBinding

class AddHappyPlaceDetailsActivity : AppCompatActivity() {
    private lateinit var detailsBinding: ActivityAddHappyPlaceDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        detailsBinding = ActivityAddHappyPlaceDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(detailsBinding.root)


    }
}