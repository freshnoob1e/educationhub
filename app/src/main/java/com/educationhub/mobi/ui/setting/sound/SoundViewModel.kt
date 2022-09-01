package com.educationhub.mobi.ui.setting.sound

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SoundViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is sound Fragment"
    }
    val text: LiveData<String> = _text
}