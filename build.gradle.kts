buildscript {
    repositories{
        google()
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
// Do not change the username below. It should always be "mapbox" (not your username).
            credentials.username = "mapbox"
// Use the secret token stored in gradle.properties as the password
            credentials.password = "sk.eyJ1IjoiYW5kcmVhOTlyIiwiYSI6ImNscXBlbWtrNjNpMnAya3M1em16ajUxZnEifQ.6sLqYWu7D6GDopk4Sg0n4Q"
            authentication.create<BasicAuthentication>("basic")
        }
    }
    dependencies {
        val nav_version="2.7.6"
        classpath("com.google.gms:google-services:4.4.0")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
}