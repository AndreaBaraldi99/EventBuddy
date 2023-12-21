plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "it.lanos.eventbuddy"
    compileSdk = 34

    defaultConfig {
        applicationId = "it.lanos.eventbuddy"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
            }
        }
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
}

dependencies {

    implementation("androidx.lifecycle:lifecycle-runtime-testing:2.6.2")
    implementation("com.google.firebase:firebase-firestore:24.10.0")
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    implementation ("com.google.code.gson:gson:2.10.1")

    val roomVersion = "2.6.1"
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    implementation ("androidx.room:room-rxjava3:$roomVersion")

    implementation ("androidx.datastore:datastore-preferences:1.0.0")
    implementation ("androidx.datastore:datastore-preferences-rxjava3:1.0.0")

    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")



    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}