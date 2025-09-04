# Image Classifier App 🖼️🤖

An Android app built with **Clean Architecture** and **Jetpack Compose** that classifies images using a **TensorFlow Lite model**.  
It follows **MVVM principles**, uses **Kotlin Coroutines + Flow**, and cleanly separates UI, business logic, and data layers.

---

## 📌 Features

- Select or capture an image (CameraX / Gallery picker)  
- Run inference on-device with a `.tflite` model  
- Show classification result with confidence score  
- Built with **Clean Architecture** for maintainability and scalability  
- Modern UI with **Jetpack Compose**  

---

## 🏗️ Tech Stack

- **Language**: Kotlin  
- **UI**: Jetpack Compose  
- **Architecture**: Clean Architecture + MVVM  
- **Dependency Injection**: Hilt  
- **Async**: Kotlin Coroutines + Flow  
- **ML Engine**: TensorFlow Lite  

---

## 🧩 Project Structure

app/
└─ src/main/java/com/example/classifier/
├─ presentation/ # UI Layer (Compose screens, ViewModels, UI state)
│ ├─ components/ # Reusable Compose UI elements
│ └─ screens/ # Feature screens (HomeScreen, ResultScreen)
│
├─ domain/ # Domain Layer (business logic)
│ ├─ model/ # Entities / domain models
│ ├─ repository/ # Repository interfaces
│ └─ usecase/ # Use cases (e.g., ClassifyImageUseCase)
│
├─ data/ # Data Layer (implementation details)
│ ├─ repository/ # Repository implementations
│ └─ ml/ # TensorFlow Lite interpreter & preprocessing
│
└─ di/ # Hilt modules for dependency injection

assets/
├─ model.tflite # ML model
└─ labels.txt # Class labels

---

## 🚀 Getting Started

### Prerequisites
- Android Studio **Giraffe (or newer)**  
- Minimum SDK **24**  
- Gradle 8.x  
- A device/emulator with **Camera** or gallery support  

### Setup
```bash
git clone https://github.com/ManojMaurya207/Image-Classifier-App.git
cd Image-Classifier-App

