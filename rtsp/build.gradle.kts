val libraryGroup: String by rootProject.extra
val vCode: Int by rootProject.extra
val vName: String by rootProject.extra
val coroutinesVersion: String by rootProject.extra
val junitVersion: String by rootProject.extra
val mockitoVersion: String by rootProject.extra

plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  id("maven-publish")
  id("org.jetbrains.dokka")
}

android {
  namespace = "com.pedro.rtsp"
  compileSdk = 34

  defaultConfig {
    minSdk = 16
//    lint.targetSdk = 34
    buildConfigField("int", "VERSION_CODE", "$vCode")
    buildConfigField("String", "VERSION_NAME", "\"${vName}\"")
  }
  buildTypes {
    release {
      isMinifyEnabled = false
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
    buildConfig = true
  }

  publishing {
    singleVariant("release")
  }
}

afterEvaluate {
  publishing {
    publications {
      // Creates a Maven publication called "release".
      create<MavenPublication>("release") {
        // Applies the component for the release build variant.
        from(components["release"])

        // You can then customize attributes of the publication as shown below.
        groupId = libraryGroup
        artifactId = "rtsp"
        version = vName
      }
    }
  }
}

dependencies {
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
  testImplementation("junit:junit:$junitVersion")
  testImplementation("org.mockito.kotlin:mockito-kotlin:$mockitoVersion")
  api(project(":common"))
}
