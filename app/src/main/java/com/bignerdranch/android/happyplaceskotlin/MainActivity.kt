package com.bignerdranch.android.happyplaceskotlin

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bignerdranch.android.happyplaceskotlin.databinding.ActivityMainBinding

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
    }
}