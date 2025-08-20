package com.example.imageclassifier.data.tensorflow

import android.content.Context
import android.graphics.Bitmap
import com.example.imageclassifier.ml.MobilenetV110224Quant
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class TfLiteClassifier(private val context: Context) {

    private val labels: List<String> by lazy {
        context.assets.open("labels.txt").bufferedReader().readLines()
    }

    fun classify(bitmap: Bitmap): Pair<String, Float> {
        val model = MobilenetV110224Quant.newInstance(context)

        val scaledImage = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
        val tensorImage = TensorImage.fromBitmap(scaledImage)

        val inputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
        inputBuffer.loadBuffer(tensorImage.buffer)

        val outputs = model.process(inputBuffer)
        val probabilities = outputs.outputFeature0AsTensorBuffer.floatArray

        // Normalize (Quantized model)
        val normalized = probabilities.map { it / 255.0f }

        val maxIdx = normalized.indices.maxByOrNull { normalized[it] } ?: -1
        val label = labels[maxIdx]
        val confidence = normalized[maxIdx]

        model.close()
        return label to confidence
    }
}
