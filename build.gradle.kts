buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.2")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.10.0" apply false
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false
}