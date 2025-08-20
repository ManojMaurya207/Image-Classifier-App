package com.example.imageclassifier.domain.usecase

import android.graphics.Bitmap
import com.example.imageclassifier.domain.model.ClassificationResult
import com.example.imageclassifier.domain.repository.ImageClassifierRepository

class ClassifyImageUseCase(
    private val repository: ImageClassifierRepository
) {
    operator fun invoke(bitmap: Bitmap): ClassificationResult {
        return repository.classify(bitmap)
    }
}
