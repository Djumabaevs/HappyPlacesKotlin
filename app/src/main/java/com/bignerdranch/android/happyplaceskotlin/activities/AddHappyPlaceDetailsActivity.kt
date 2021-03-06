package com.bignerdranch.android.happyplaceskotlin.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.bignerdranch.android.happyplaceskotlin.R
import com.bignerdranch.android.happyplaceskotlin.databinding.ActivityAddHappyPlaceDetailsBinding
import com.bignerdranch.android.happyplaceskotlin.models.HappyPlaceModel

class AddHappyPlaceDetailsActivity : AppCompatActivity() {
    private lateinit var detailsBinding: ActivityAddHappyPlaceDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        detailsBinding = ActivityAddHappyPlaceDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(detailsBinding.root)

        var happyPlaceDetailModel: HappyPlaceModel? = null
        if(intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)) {
            happyPlaceDetailModel =
                intent.getSerializableExtra(MainActivity.EXTRA_PLACE_DETAILS)
                        as HappyPlaceModel
        }
        if(happyPlaceDetailModel != null) {
            setSupportActionBar(detailsBinding.toolbarHappyPlaceDetail)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = happyPlaceDetailModel.title

            detailsBinding.toolbarHappyPlaceDetail.setNavigationOnClickListener {
                onBackPressed()
            }
            detailsBinding.ivPlaceImage.setImageURI(Uri.parse(happyPlaceDetailModel.image))
            detailsBinding.tvDescription.text = happyPlaceDetailModel.description
            detailsBinding.tvLocation.text = happyPlaceDetailModel.location

            detailsBinding.btnViewOnMap.setOnClickListener {
                val intent = Intent(this, MapActivity::class.java)
                intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS, happyPlaceDetailModel)
                startActivity(intent)
            }
        }
    }
}