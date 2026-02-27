plugins {
    id("com.android.library")
    id("maven-publish")
}

android {
    namespace = "wraphex.anypreference"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
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

    flavorDimensions += "backend"
    productFlavors {
        create("sp") {
            dimension = "backend"
        }
        create("mmkv") {
            dimension = "backend"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    publishing {
        singleVariant("spRelease")
        singleVariant("mmkvRelease")
    }
}

afterEvaluate {
    publishing {
        val pubGroup = project.findProperty("group")?.toString() ?: "com.github.wraphex"
        val pubVersion = project.findProperty("version")?.toString() ?: "SNAPSHOT"
        publications {
            register<MavenPublication>("spRelease") {
                groupId = pubGroup
                artifactId = "any-preference-sp"
                version = pubVersion
                from(components["spRelease"])
            }
            register<MavenPublication>("mmkvRelease") {
                groupId = pubGroup
                artifactId = "any-preference-mmkv"
                version = pubVersion
                from(components["mmkvRelease"])
            }
        }
    }
}

dependencies {
    implementation(libs.gson)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.mmkv)
}
