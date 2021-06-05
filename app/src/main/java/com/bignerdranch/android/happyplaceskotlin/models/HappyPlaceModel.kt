package com.bignerdranch.android.happyplaceskotlin.models

import java.io.Serializable

data class HappyPlaceModel(
    val id: Int,
    val image: String,
    val title: String,
    val date: String,
    val description: String,
    val location: String,
    val longitude: Double,
    val latitude: Double
) : Serializable
