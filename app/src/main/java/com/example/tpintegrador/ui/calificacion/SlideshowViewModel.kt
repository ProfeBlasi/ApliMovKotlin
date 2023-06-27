package com.example.tpintegrador.ui.calificacion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SlideshowViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Soon qualification"
    }
    val text: LiveData<String> = _text
}