package com.example.imageclassifier.presentation.ui

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
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.imageclassifier.R
import com.example.imageclassifier.presentation.viewmodel.MainViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val result by viewModel.result.collectAsState()

    // Pick image from gallery
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            imageBitmap =  BitmapFactory.decodeStream(inputStream)
            // Predict when image is selected
            imageBitmap?.let(viewModel::classify)
        }
    }

    // Capture from camera
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let { imageBitmap = it }
        // Predict when image is captured
        imageBitmap?.let(viewModel::classify)
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (imageBitmap != null) {
            // Show selected or captured bitmap
            Image(
                bitmap = imageBitmap!!.asImageBitmap(),
                contentDescription = "Selected Image",
                modifier = Modifier
                    .size(250.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            // Show default placeholder icon
            Image(
                painter = painterResource(id = R.drawable.baseline_image_search_24),
                contentDescription = "Default Placeholder",
                modifier = Modifier
                    .size(250.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Button(onClick = { galleryLauncher.launch("image/*") }) {
            Text("Upload")
        }
        Button(onClick = { cameraLauncher.launch(null) }) {
            Text("Capture")
        }
//        Button(onClick = { imageBitmap?.let(viewModel::classify) }) {
//            Text("Predict")
//        }

        result?.let {
            Text(text="${it.label} : ${"%.2f".format(it.confidence)}%",
                fontStyle = FontStyle.Normal,
                fontSize = 40.sp,
                modifier = Modifier.padding(16.dp))
        }
    }
}
