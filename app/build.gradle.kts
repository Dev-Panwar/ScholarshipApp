plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    //    for using firebase
    id("com.google.gms.google-services")
}

android {
    namespace = "dev.panwar.scholarshipapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.panwar.scholarshipapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures{
        viewBinding=true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //    for using firebase authentication
    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:32.2.3"))

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth-ktx")
//    end

//    for using firebase cloud fireStore DB
    implementation("com.google.firebase:firebase-firestore-ktx")

//    for using firebase cloud messaging for notifications
    implementation ("com.google.firebase:firebase-messaging:23.4.0")

//    for circular ImageView
    implementation ("de.hdodenhof:circleimageview:3.1.0")

//    for Glide Library, this library helps in downloading image from URL and easily show on device
    implementation ("com.github.bumptech.glide:glide:4.16.0")

//    dependency for using firebase Storage to store our Images
    implementation("com.google.firebase:firebase-storage-ktx")

    //    retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.1")

    implementation ("com.karumi:dexter:6.2.3")

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")

    //    for lottie animation
    implementation ("com.airbnb.android:lottie:6.4.1")


}