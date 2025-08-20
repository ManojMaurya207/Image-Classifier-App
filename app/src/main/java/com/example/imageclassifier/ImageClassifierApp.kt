package com.example.imageclassifier

import android.app.Application
import com.example.imageclassifier.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ImageClassifierApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ImageClassifierApp)
            modules(appModule)
        }
    }
}
