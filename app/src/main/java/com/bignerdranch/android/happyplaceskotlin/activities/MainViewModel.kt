package com.bignerdranch.android.happyplaceskotlin.activities

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val statusText = MutableLiveData<CharSequence>("Waiting for result...")

    fun getStatusText(): LiveData<CharSequence> = statusText

    fun onPermissionsResult(result: Boolean) {
        statusText.value = getApplication<Application>().getString(
            if(result) { 1 }
            else { 0 }
        )
    }
//    fun onCustomContractResult(transactionResult: TransactionResult) {
//        statusText.value = "Success: transactionalResult, message: {transac..."
//    }
}