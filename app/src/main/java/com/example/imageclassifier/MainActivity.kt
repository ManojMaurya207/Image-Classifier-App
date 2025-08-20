package com.example.imageclassifier

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import com.example.imageclassifier.presentation.ui.MainScreen
import com.example.imageclassifier.presentation.ui.MainScreenAllinOne

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (!hasCameraPermission()) {
            requestPermissions(CAMERA_PERMISSION, 1)
        }
        setContent {
//            MainScreenAllinOne(context = this)
            MainScreen()
        }
    }

    private fun hasCameraPermission() : Boolean {
        return CAMERA_PERMISSION.all {
            ContextCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }

    }

    companion object{
        private val CAMERA_PERMISSION = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO)
    }
}
