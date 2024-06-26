plugins {
    id("com.android.application")

    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.taskforfriends"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.taskforfriends"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }


}



dependencies {

    implementation("androidx.appcompat:appcompat:1.0.1")
//    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.navigation:navigation-fragment:2.7.3")
    implementation("androidx.navigation:navigation-ui:2.7.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-analytics")


    implementation("com.google.firebase:firebase-core:19.0.0")
    implementation("com.google.firebase:firebase-auth:16.0.1")
    implementation("com.google.firebase:firebase-database:16.0.1")

    implementation ("com.google.android.material:material:1.0.0")
    implementation ("androidx.cardview:cardview:1.0.0")

}
