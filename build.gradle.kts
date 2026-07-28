// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    // ponytail: kotlin-android NOT needed with AGP 9.x (Kotlin is bundled)
    alias(libs.plugins.kotlin.compose) apply false
}