package com.example.imageclassifier.di


import com.example.imageclassifier.data.repository.ImageClassifierRepositoryImpl
import com.example.imageclassifier.data.tensorflow.TfLiteClassifier
import com.example.imageclassifier.domain.repository.ImageClassifierRepository
import com.example.imageclassifier.domain.usecase.ClassifyImageUseCase
import com.example.imageclassifier.presentation.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    // Classifier
    single { TfLiteClassifier(androidContext()) }

    // Repository
    single<ImageClassifierRepository> { ImageClassifierRepositoryImpl(get()) }

    // UseCase
    factory { ClassifyImageUseCase(get()) }

    // ViewModel
    factory { MainViewModel(get()) }
}
