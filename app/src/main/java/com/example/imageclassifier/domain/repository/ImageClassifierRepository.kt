package com.example.imageclassifier.domain.repository

import android.graphics.Bitmap
import com.example.imageclassifier.domain.model.ClassificationResult

interface ImageClassifierRepository {
    fun classify(bitmap: Bitmap): ClassificationResult
}
