import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

fun loadProperties() = Properties().apply {
    val localPropertiesFile = project.rootProject.file("local.properties")
    load(localPropertiesFile.inputStream())
}

val localProperties = loadProperties()

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    id("ru.ok.tracer") version "1.1.7"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.2.21"
}

tracer {
    create("defaultConfig") {
        pluginToken = localProperties.getProperty("pluginTokenTracer")
        appToken = localProperties.getProperty("appTokenTracer")

        uploadMapping = true
        uploadNativeSymbols = true
    }

    create("debug") {
        uploadMapping = false
        uploadNativeSymbols = false
        isDisabled = true
    }
}

android {
    namespace = "ru.pomidorka.weatherapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "ru.pomidorka.weatherapp"
        minSdk = 26
        targetSdk = 36
        versionCode = 11
        versionName = "1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField(
            type = "String",
            name = "WEATHER_API_TOKEN",
            value = localProperties.getProperty("weatherApiToken")
        )

        buildConfigField(
            type = "String",
            name = "GISMETIO_API_TOKEN",
            value = localProperties.getProperty("gismetioApiToken")
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
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

    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.fromTarget("1.8")
        }
    }
    ksp {

    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.mobileads)

    implementation(libs.kotlinx.serialization.json)

    implementation(platform(libs.tracer.platform))
    implementation(libs.tracer.crash.report)

    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.material3)
    implementation(libs.androidx.material.icons.extended)

    // Графики
    implementation(libs.compose.charts)

    // Работа с сетью
    implementation(libs.retrofit)
    implementation(libs.converter.kotlinx.serialization)

    // Навигация
    implementation(libs.androidx.navigation.compose)

    // Интерфейс
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}