plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}

android {
    namespace = "com.s23010843.essentialsmonitor"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.s23010843.essentialsmonitor"
        minSdk = 24
        targetSdk = 35
        versionCode = 7
        versionName = "1.0.7"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    
    // Google Maps and Location Services
    implementation(libs.gms.play.services.maps)
    implementation(libs.gms.play.services.location)
    
    // Google Sign-In
    implementation(libs.gms.play.services.auth)
    
    // RecyclerView
    implementation(libs.recyclerview.v132)
    
    // CardView
    implementation(libs.cardview)
    
    // SwipeRefreshLayout
    implementation(libs.swiperefreshlayout)
    implementation(libs.recyclerview)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}