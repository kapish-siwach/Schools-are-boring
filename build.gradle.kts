plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    kotlin("kapt") version "1.9.24" apply false
    id("com.android.library") version "7.4.2" apply false
    alias(libs.plugins.google.gms.google.services) apply false
//    alias(libs.plugins.kotlin.compose) apply false
}