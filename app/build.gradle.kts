val vCode: Int by rootProject.extra
val vName: String by rootProject.extra

plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
}

android {
  namespace = "com.pedro.streamer"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.pedro.streamer"
    minSdk = 21
    targetSdk = 34
    versionCode = vCode
    versionName = vName
    multiDexEnabled = true
  }
  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
  }
  kotlinOptions {
    jvmTarget = "17"
  }
  buildFeatures {
    viewBinding = true
    buildConfig = true
    dataBinding = true
  }
}

dependencies {
  implementation(project(":library"))
  implementation("androidx.constraintlayout:constraintlayout:2.1.4")
  implementation("com.google.android.material:material:1.11.0")
  implementation("androidx.multidex:multidex:2.0.1")
  implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.databinding:databinding-runtime:8.3.1")

    val cameraxVersion = "1.3.1"
  implementation("androidx.camera:camera-core:$cameraxVersion")
  implementation("androidx.camera:camera-camera2:$cameraxVersion")
  implementation("androidx.camera:camera-lifecycle:$cameraxVersion")


  implementation ("com.intuit.sdp:sdp-android:1.0.6")

  implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
  implementation ("io.reactivex.rxjava2:rxjava:2.2.16")
  implementation ("com.squareup.retrofit2:adapter-rxjava2:2.9.0")
  implementation ("io.reactivex.rxjava2:rxandroid:2.1.1")
  implementation ("com.squareup.okhttp3:logging-interceptor:4.7.2")


}
