package com.zenai.preview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _htmlCode = MutableLiveData<String>("")
    val htmlCode: LiveData<String> = _htmlCode

    fun setHtmlCode(code: String) {
        _htmlCode.value = code
    }
}
