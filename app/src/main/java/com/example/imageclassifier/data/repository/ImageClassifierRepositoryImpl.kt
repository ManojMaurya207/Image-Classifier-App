package com.example.imageclassifier.data.repository

import android.graphics.Bitmap
import com.example.imageclassifier.data.tensorflow.TfLiteClassifier
import com.example.imageclassifier.domain.model.ClassificationResult
import com.example.imageclassifier.domain.repository.ImageClassifierRepository

class ImageClassifierRepositoryImpl(
    private val classifier: TfLiteClassifier
) : ImageClassifierRepository {
    override fun classify(bitmap: Bitmap): ClassificationResult {
        val (label, confidence) = classifier.classify(bitmap)
        return ClassificationResult(label, confidence * 100f)
    }
}
