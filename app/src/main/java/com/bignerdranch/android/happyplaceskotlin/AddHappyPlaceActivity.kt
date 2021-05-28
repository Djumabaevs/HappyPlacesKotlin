package com.bignerdranch.android.happyplaceskotlin

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bignerdranch.android.happyplaceskotlin.databinding.ActivityAddHappyPlaceBinding
import java.util.*

class AddHappyPlaceActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var ab: ActivityAddHappyPlaceBinding
    private var cal = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

    override fun onCreate(savedInstanceState: Bundle?) {
        ab = ActivityAddHappyPlaceBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(ab.root)

        setSupportActionBar(ab.toolbarAddPlace)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ab.toolbarAddPlace.setNavigationOnClickListener {
            onBackPressed()
        }
        
        dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.et_date -> {
                DatePickerDialog(this@AddHappyPlaceActivity,
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        }
    }
}