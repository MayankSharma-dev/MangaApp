# MangaApp

A modern Android application showcasing Clean Architecture + MVVM using Jetpack Compose, Firebase Auth, Room, Paging 3, and MediaPipe Face Detection.

---

<h2 align="start" id="Demo">Demo
<br>
 <video width="320" height="240" src="https://github.com/user-attachments/assets/8714f10a-f7f5-4100-bb0a-77369d630de3" type="video/mp4"> Your browser does not support the video tag. </video>
//Here is the video demo

Note: The uploaded APK is a debug build, which may result in reduced performance and minor glitches. The release version will be smoother, as further optimizations for Jetpack Compose are applied during release builds.

## Features

### 1. User Authentication 

- Sign-in screen with Firebase Email Authentication  
- Integrated Google One Tap Sign-In and Google Sign-In using Firebase  
- Credentials securely stored with Proto DataStore + Android Keystore (preferred over Room for sensitive data)  
- Automatic user session handling:
  - Navigates to Home Screen if the user is already signed in  
  - Falls back to Sign-In Screen otherwise  

### 2. Bottom Navigation

Implements a Bottom Navigation Bar with:

- Manga Screen  
- Face Recognition Screen  

Single Activity architecture powered by Jetpack Navigation Component.

### 3. Manga Screen

- Fetches manga from [MangaVerse API](https://rapidapi.com/sagararofie/api/mangaverse-api)  
- Uses Paging 3 for efficient data loading and smooth scrolling  
- Offline-first experience:
  - Manga data is cached in Room  
  - Automatically refreshes on network availability  
- Clickable items navigate to a detailed Manga Description screen  

### 4. Face Recognition

- Real-time face detection using MediaPipe Vision  
- Live camera preview using CameraX  
- Displays a reference rectangle:
  - Green if face is inside  

### 5. Architecture & Best Practices

- Clean Architecture Layers:
  - **Data** (API, Room, DataStore)  
  - **Domain** (Repository, Entities)  
  - **Presentation** (Jetpack Compose UI, ViewModels)  
- MVVM pattern enforced  
- Single-activity navigation using NavHost  

---

### Screenshots

<div style="display: flex; flex-direction: row; justify-content: space-around; flex-wrap: wrap; gap: 20px;">

  <div style="text-align: center;">
    <strong>Auth Screen</strong><br>
    <img src="https://github.com/user-attachments/assets/dfdb148f-593e-41c6-a1d3-dd9ac1db79d5" width="450" height="350">
  </div>

  <div style="text-align: center;">
    <strong>Home Screen</strong><br>
    <img src="https://github.com/user-attachments/assets/43bc7c85-c274-482f-ab91-bccb59665157" width="450" height="350">
  </div>

  <div style="text-align: center;">
    <strong>Detail Screen</strong><br>
    <img src="https://github.com/user-attachments/assets/ff7d995b-0bd5-4e00-88a9-c4da9d960231" width="450" height="350">
  </div>

  <div style="text-align: center;">
    <strong>Face Scan AI Screen</strong><br>
    <img src="https://github.com/user-attachments/assets/593e5a09-8465-4f42-89c2-2a7ae89a9216" width="450" height="350">
  </div>

</div>

## 🛠 Tech Stack

### 🔧 Libraries & Tools

- Jetpack Compose for UI  
- Navigation Component for routing  
- Hilt for Dependency Injection  
- Paging 3 for efficient list loading  
- Room for local storage  
- Retrofit + Moshi for networking  
- Firebase Auth + Google Sign-In for authentication  
- Proto DataStore + Android Keystore for secure preferences  
- MediaPipe Face Detection for camera-based face recognition  
- Coil 3 for image loading  
- CameraX for camera handling  

---

## 📦 Dependencies

<details>
<summary><strong>Click to view full dependency list</strong></summary>

```kotlin
// Compose
implementation(libs.androidx.core.ktx)
implementation(libs.androidx.lifecycle.runtime.ktx)
implementation(libs.androidx.activity.compose)
implementation(platform(libs.androidx.compose.bom))
implementation(libs.androidx.ui)
implementation(libs.androidx.ui.graphics)
implementation(libs.androidx.ui.tooling.preview)
implementation(libs.androidx.material3)
implementation("androidx.compose.material:material-icons-extended:1.7.8")

// Navigation
implementation("androidx.navigation:navigation-compose:2.8.9")

// Firebase Auth & Google Sign-In
implementation(libs.firebase.auth)
implementation(libs.androidx.credentials)
implementation(libs.androidx.credentials.play.services.auth)
implementation(libs.googleid)
implementation(libs.play.services.auth)

// Coil (Image Loading)
implementation("io.coil-kt.coil3:coil-compose:3.1.0")
implementation("io.coil-kt.coil3:coil-network-okhttp:3.1.0")

// Paging 3
implementation("androidx.paging:paging-runtime:3.3.6")
implementation("androidx.paging:paging-compose:3.3.6")

// Hilt (DI)
implementation(libs.hilt.android)
implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
ksp(libs.hilt.compiler)

// Room
implementation("androidx.room:room-runtime:2.7.0")
ksp("androidx.room:room-compiler:2.7.0")
implementation("androidx.room:room-paging:2.7.0")

// Retrofit & Moshi
implementation("com.squareup.retrofit2:retrofit:2.11.0")
implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
implementation("com.squareup.moshi:moshi-kotlin:1.15.1")
ksp("com.squareup.moshi:moshi-kotlin-codegen:1.15.1")

// DataStore + Proto
implementation("androidx.datastore:datastore-preferences:1.1.4")
implementation(libs.kotlinx.serialization.json)

// MediaPipe & CameraX
implementation("com.google.mediapipe:tasks-vision:0.20230731")
implementation(libs.bundles.camera)
