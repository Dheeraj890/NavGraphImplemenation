package com.example.mynavigationtest.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        Log.e("Dashbord Model","Called on Loading Class")
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
}