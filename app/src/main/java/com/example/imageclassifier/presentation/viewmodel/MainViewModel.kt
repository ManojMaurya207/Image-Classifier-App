package com.example.imageclassifier.presentation.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imageclassifier.domain.model.ClassificationResult
import com.example.imageclassifier.domain.usecase.ClassifyImageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val classifyImageUseCase: ClassifyImageUseCase
) : ViewModel() {

    private val _result = MutableStateFlow<ClassificationResult?>(null)
    val result: StateFlow<ClassificationResult?> = _result

    fun classify(bitmap: Bitmap) {
        viewModelScope.launch {
            _result.value = classifyImageUseCase(bitmap)
        }
    }
}
