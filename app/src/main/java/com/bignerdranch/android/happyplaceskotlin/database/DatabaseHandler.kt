package com.bignerdranch.android.happyplaceskotlin.database

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler (context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


}