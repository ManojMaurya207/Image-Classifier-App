package com.example.imageclassifier.presentation.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.imageclassifier.ml.MobilenetV110224Quant
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.BufferedReader

@Composable
fun MainScreenAllinOne(modifier: Modifier = Modifier,context: Context) {
    val context = LocalContext.current
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var result by remember { mutableStateOf("") }

    // Load labels once
    val labels: List<String> = remember {
        context.assets.open("labels.txt").bufferedReader().use(BufferedReader::readLines)
    }

    // Gallery picker
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            imageBitmap = bitmap
        }
    }

    // Camera capture
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let { imageBitmap = it }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Display image
        imageBitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Selected Image",
                modifier = Modifier
                    .size(250.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        }

        // Buttons
        Button(onClick = { galleryLauncher.launch("image/*") }) {
            Text("Pick from Gallery")
        }

        Button(onClick = { cameraLauncher.launch(null) }) {
            Text("Capture from Camera")
        }

        Button(onClick = {
            imageBitmap?.let { bitmap ->
                val model = MobilenetV110224Quant.newInstance(context)

                // Preprocess input
                val scaledImage = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
                val tensorImage = TensorImage.fromBitmap(scaledImage)

                val inputBuffer =
                    TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
                inputBuffer.loadBuffer(tensorImage.buffer)

                // Run inference
                val outputs = model.process(inputBuffer)
                val probabilities = outputs.outputFeature0AsTensorBuffer.floatArray
                // Normalize (0–255 → 0.0–1.0)
                val normalized = probabilities.map { it / 255.0f }

                // Find best label
                val maxIdx = normalized.indices.maxByOrNull { normalized[it] } ?: -1
                val label = labels[maxIdx]
                val confidence = normalized[maxIdx] * 100f

                result = "$label : ${"%.2f".format(confidence)}%"
                model.close()
            } ?: run {
                result = "No image selected!"
            }
        }
        ) {
            Text("Predict")
        }

        Text(text = result)
    }
}