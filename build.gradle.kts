// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hiltAndroid) apply false
    alias(libs.plugins.kotlinAndroidKsp) apply false
    alias(libs.plugins.google.gms.google.services) apply false
//    kotlin("plugin.serialization") version "2.1.20"
//    id("com.google.dagger.hilt.android") version "2.0.20-1.0.23" apply false
}